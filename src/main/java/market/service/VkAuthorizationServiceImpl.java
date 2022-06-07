package market.service;

import com.github.scribejava.apis.vk.VKOAuth2AccessToken;
import com.github.scribejava.core.model.OAuth2AccessToken;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Response;
import com.github.scribejava.core.model.Verb;
import com.github.scribejava.core.oauth.AccessTokenRequestParams;
import com.github.scribejava.core.oauth.OAuth20Service;
import market.dto.AuthVkUser;
import market.exception.VkAuthException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

@Service
public class VkAuthorizationServiceImpl implements VkAuthorizationService {

    private final OAuth20Service auth20Service;

    @Value("${vk.client.resource}")
    private String vkResourceUrl;

    @Value("${vk.client.scope}")
    private String scope;

    public VkAuthorizationServiceImpl(OAuth20Service auth20Service) {
        this.auth20Service = auth20Service;
    }

    @Override
    public String createAuthorizationUrl() {
        return auth20Service.createAuthorizationUrlBuilder().scope(scope).build();
    }

    @Override
    public AuthVkUser authorizationByCode(String code) throws IOException, ExecutionException, InterruptedException {
        OAuth2AccessToken accessToken = auth20Service.getAccessToken(AccessTokenRequestParams.create(code)
                .scope(scope));

        if (!(accessToken instanceof VKOAuth2AccessToken) ||
                (((VKOAuth2AccessToken) accessToken).getEmail() == null)) {
//            почта лежит вместе с токеном, а не в response.getBody()
//            почта может быть не привязан к аккаунту
            throw new VkAuthException("email not linked to VK account");
        }

        final OAuthRequest request = new OAuthRequest(Verb.GET, vkResourceUrl);
        auth20Service.signRequest(accessToken, request);
        try (Response response = auth20Service.execute(request)) {
            if (response.isSuccessful()) {
                // формат response.getBody()
                // {"response":[{"id":732833411,"first_name":"testvk","last_name":"testvk","can_access_closed":true,"is_closed":false}]}

                JSONObject jsonObject = new JSONObject(response.getBody());
                jsonObject = jsonObject.getJSONArray("response").getJSONObject(0);
                return new AuthVkUser(jsonObject.getString("first_name"),
                        jsonObject.getString("last_name"), (((VKOAuth2AccessToken) accessToken).getEmail()));
            }
        }
        throw new VkAuthException("Failed VK authorization");
    }
}
