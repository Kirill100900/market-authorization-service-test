package market.controller;

import market.dto.AuthResponse;
import market.dto.SignUpRequest;
import market.dto.UserDtoAuthorization;

import market.service.AuthorizationService;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity signUp(@RequestBody SignUpRequest request) {
        authorizationService.signUp(request);
        return ResponseEntity.ok().build();
    }

}
