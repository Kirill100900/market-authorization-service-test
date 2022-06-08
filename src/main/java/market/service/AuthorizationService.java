package market.service;

import market.dto.AuthResponse;
import market.dto.AuthVkUser;
import market.dto.SignUpRequest;
import market.dto.UserDtoAuthorization;

public interface AuthorizationService {
    AuthResponse signIn(UserDtoAuthorization loginRequestCredentials);

    void signUp(SignUpRequest request);

    AuthResponse authorizationByVk(AuthVkUser user);
}
