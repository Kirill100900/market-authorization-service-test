package market.service;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2UserAuthority;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class VkOAuth2UserService extends DefaultOAuth2UserService {

    @Override
    public OAuth2User loadUser(OAuth2UserRequest oAuth2UserRequest) throws OAuth2AuthenticationException {
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add("Content-Type", "application/json");
        headers.add("Authorization",
                oAuth2UserRequest.getAccessToken().getTokenType().getValue() + " " + oAuth2UserRequest.getAccessToken().getTokenValue());
        HttpEntity<?> httpRequest = new HttpEntity<>(headers);

        String uri = oAuth2UserRequest
                .getClientRegistration()
                .getProviderDetails()
                .getUserInfoEndpoint()
                .getUri();
        String userNameAttributeName = oAuth2UserRequest
                .getClientRegistration()
                .getProviderDetails()
                .getUserInfoEndpoint()
                .getUserNameAttributeName();
        uri = uri.replace("{user_id}",
                userNameAttributeName + "=" + oAuth2UserRequest.getAdditionalParameters().get(userNameAttributeName));

        RestTemplate template = new RestTemplate();
        ResponseEntity<Object> entity = template.exchange(uri, HttpMethod.GET, httpRequest, Object.class);

        Map<String, Object> response = (Map<String, Object>) entity.getBody();
        ArrayList<Map<String, Object>> users = (ArrayList<Map<String, Object>>) response.get("response");
        Map<String, Object> userAttributes = users.get(0);
        String email = (String) oAuth2UserRequest.getAdditionalParameters().get("email");
        if (email != null) {
            userAttributes.put("email", email);
        } else {
            throw new OAuth2AuthenticationException("Email required.");
        }
        Set<GrantedAuthority> authorities = Collections.singleton(new OAuth2UserAuthority(userAttributes));
        return new DefaultOAuth2User(authorities, userAttributes, "id");
    }
}
