package market.service;

import market.dto.AuthResponse;
import market.dto.SignUpRequest;
import market.dto.UserDtoAuthorization;

public interface AuthorizationService {
    AuthResponse signIn(UserDtoAuthorization loginRequestCredentials);

    AuthResponse signUp(SignUpRequest request);
}
