package market.controller;

import com.github.scribejava.apis.VkontakteApi;
import com.github.scribejava.apis.vk.VKOAuth2AccessToken;
import com.github.scribejava.core.model.OAuth2AccessToken;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Response;
import com.github.scribejava.core.model.Verb;
import com.github.scribejava.core.oauth.AccessTokenRequestParams;
import com.github.scribejava.core.oauth.OAuth20Service;
import market.dto.AuthResponse;
import market.dto.AuthVkUser;
import market.dto.SignUpRequest;
import market.dto.UserDtoAuthorization;

import market.exception.VkAuthException;
import market.service.AuthorizationService;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthorizationService authorizationService;
    private final OAuth20Service auth20Service;

    private static final String PROTECTED_RESOURCE_URL = "https://api.vk.com/method/users.get?v="
            + VkontakteApi.VERSION;

    @Value("${vk.client.scope}")
    private String scope;

    public AuthController(AuthorizationService authorizationService, OAuth20Service auth20Service) {
        this.authorizationService = authorizationService;
        this.auth20Service = auth20Service;
    }

    @PostMapping("/signin")
    public AuthResponse signIn(@RequestBody UserDtoAuthorization user) {
        return authorizationService.signIn(user);
    }

    @PostMapping("/signup")
    public ResponseEntity signUp(@RequestBody SignUpRequest request) {
        authorizationService.signUp(request);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/vkauth")
    public void vkAuthorization(HttpServletResponse response) throws IOException {
        String url = auth20Service.createAuthorizationUrlBuilder().scope(scope).build();
        response.sendRedirect(url);
    }

    @GetMapping("/vksigin")
    public AuthResponse vkSigIn(@RequestParam(value = "code") String code) throws IOException, ExecutionException, InterruptedException {
        OAuth2AccessToken accessToken = auth20Service.getAccessToken(AccessTokenRequestParams.create(code)
                .scope(scope));

        if (!(accessToken instanceof VKOAuth2AccessToken) ||
                (((VKOAuth2AccessToken) accessToken).getEmail() == null)) {
//            почта лежит вместе с токеном, а не в response.getBody()
//            почта может быть не привязан к аккаунту
            throw new VkAuthException("email not linked to VK account");
        }

        final OAuthRequest request = new OAuthRequest(Verb.GET, PROTECTED_RESOURCE_URL);
        auth20Service.signRequest(accessToken, request);
        try (Response response = auth20Service.execute(request)) {
            if(response.isSuccessful()) {
                // формат response.getBody()
                // {"response":[{"id":732833411,"first_name":"testvk","last_name":"testvk","can_access_closed":true,"is_closed":false}]}

                JSONObject jsonObject = new JSONObject(response.getBody());
                jsonObject = jsonObject.getJSONArray("response").getJSONObject(0);
                return authorizationService.vkSigIn(new AuthVkUser(jsonObject.getString("first_name"),
                        jsonObject.getString("last_name"), (((VKOAuth2AccessToken) accessToken).getEmail())));
            }
        }
        throw new VkAuthException("Failed VK authorization");
    }


}
