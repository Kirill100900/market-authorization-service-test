package market.service;

import market.dto.ProfileDto;
import market.feign.ProfileFeignClient;
import market.model.Account;
import market.model.Role;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.stereotype.Service;
import java.util.stream.Collectors;

@Service
public class VkOAuth2AuthorizedClientService implements OAuth2AuthorizedClientService {
    private final AccountService accountService;
    private final ProfileFeignClient profileFeignClient;

    public VkOAuth2AuthorizedClientService(AccountService accountService, @Lazy ProfileFeignClient profileFeignClient) {
        this.accountService = accountService;
        this.profileFeignClient = profileFeignClient;
    }

    @Override
    public <T extends OAuth2AuthorizedClient> T loadAuthorizedClient(String s, String s1) {
        return null;
    }

    @Override
    public void saveAuthorizedClient(OAuth2AuthorizedClient oAuth2AuthorizedClient, Authentication authentication) {

        OAuth2AuthenticatedPrincipal oAuth2user = (OAuth2AuthenticatedPrincipal) authentication.getPrincipal();
        if (!accountService.existAccountByEmail(oAuth2user.getAttribute("email"))) {

            GrantedAuthority authority = oAuth2user.getAuthorities().stream().collect(Collectors.toList()).get(0);
            Account account = new Account(oAuth2user.getAttribute("email"),
                    null,
                    new Role(null, authority.getAuthority()),
                    false);
            accountService.saveAccount(account);

            profileFeignClient.saveProfile(new ProfileDto(null,
                    account.getId(),
                    oAuth2user.getAttribute("email"),
                    oAuth2user.getAttribute("first_name"),
                    oAuth2user.getAttribute("last_name")
            ));
        }
    }

    @Override
    public void removeAuthorizedClient(String s, String s1) {
    }
}
