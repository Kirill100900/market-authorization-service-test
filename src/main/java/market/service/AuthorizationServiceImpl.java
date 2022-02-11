package market.service;

import market.dto.*;
import market.feign.ProfileFeignClient;
import market.model.Account;
import market.model.Role;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthorizationServiceImpl implements AuthorizationService {
    private final AccountService accountService;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final ProfileFeignClient profileFeignClient;

    @Value("${jwt.token.expiration}")
    private long jwtExpiration;

    public AuthorizationServiceImpl(AccountService accountService, PasswordEncoder passwordEncoder, JwtService jwtService, ProfileFeignClient profileFeignClient) {
        this.accountService = accountService;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.profileFeignClient = profileFeignClient;
    }

    @Override
    public AuthResponse signIn(UserDtoAuthorization loginRequestCredentials) {
        Account account = accountService.findAccountByEmail(loginRequestCredentials.email());
        if (account == null) {
            throw new UsernameNotFoundException("User not found");
        }

        if (passwordEncoder.matches(loginRequestCredentials.password(), account.getPassword())) {
            ProfileDto profile = profileFeignClient.findProfileByEmail(account.getEmail());
            return new AuthResponse(new UserResponse(profile.id(), profile.firstName(), profile.lastName(), profile.email(), account.getRole().getAuthority(), account.getBlocked()),
                    new AuthToken(jwtService.createToken(account), jwtExpiration));
        }

        throw new AuthenticationCredentialsNotFoundException("Invalid password or email");
    }

    @Override
    public AuthResponse signUp(SignUpRequest request) {
        Account account = new Account(request.email(), request.password(), false);
        account.setRole(new Role("ROLE_USER"));
        accountService.saveAccount(account);

        ProfileDto profile = new ProfileDto(null, account.getId(), request.email(), request.firstName(), request.lastName());
        profile = profileFeignClient.saveProfile(profile);
        return new AuthResponse(new UserResponse(profile.id(), profile.firstName(), profile.lastName(), profile.email(), account.getRole().getAuthority(), account.getBlocked()),
                new AuthToken(jwtService.createToken(account), jwtExpiration));
    }
}
