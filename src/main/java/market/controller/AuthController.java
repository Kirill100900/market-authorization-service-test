package market.controller;

import market.dto.AuthResponse;
import market.dto.SignUpRequest;
import market.dto.UserDtoAuthorization;

import market.service.AuthorizationService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthorizationService authorizationService;

    public AuthController(AuthorizationService authorizationService) {
        this.authorizationService = authorizationService;
    }

    @PostMapping("/signin")
    public AuthResponse signIn(@RequestBody UserDtoAuthorization user) {
        return authorizationService.signIn(user);
    }

    @PostMapping("/signup")
    public AuthResponse signUp(@RequestBody SignUpRequest request) {
        return authorizationService.signUp(request);
    }

}
