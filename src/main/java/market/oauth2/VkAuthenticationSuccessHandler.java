package market.oauth2;

import com.google.gson.Gson;
import market.dto.AuthResponse;
import market.dto.AuthToken;
import market.dto.ProfileDto;
import market.dto.UserResponse;
import market.feign.ProfileFeignClient;
import market.model.Account;
import market.service.AccountService;
import market.service.JwtService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Component
public class VkAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final JwtService jwtService;
    private final ProfileFeignClient profileFeignClient;
    private final AccountService accountService;

    @Value("${jwt.token.expiration}")
    private long jwtExpiration;

    public VkAuthenticationSuccessHandler(JwtService jwtService, ProfileFeignClient profileFeignClient, AccountService accountService) {
        this.jwtService = jwtService;
        this.profileFeignClient = profileFeignClient;
        this.accountService = accountService;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws ServletException, IOException {
        OAuth2AuthenticatedPrincipal oAuth2user = (OAuth2AuthenticatedPrincipal) authentication.getPrincipal();
        ProfileDto profile = profileFeignClient.findProfileByEmail(oAuth2user.getAttribute("email"));
        Account account = accountService.findAccountByEmail(profile.email());

        String authResponse = new Gson().toJson(new AuthResponse(
                new UserResponse(profile.id(), profile.firstName(), profile.lastName(), profile.email(), account.getRole().getAuthority(), account.getBlocked()),
                new AuthToken(jwtService.createToken(account), jwtExpiration))
        );

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        out.print(authResponse);
        out.flush();
        super.onAuthenticationSuccess(request, response, authentication);
    }
}
