package market.controller;

import market.service.PasswordResetTokenService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/password")
public class PasswordResetTokenController {

    private final PasswordResetTokenService passwordResetTokenService;

    public PasswordResetTokenController(PasswordResetTokenService passwordResetTokenService) {
        this.passwordResetTokenService = passwordResetTokenService;
    }

    @PostMapping("/reset")
    public ResponseEntity resetPassword(@RequestParam("email") String email) {
        passwordResetTokenService.resetPassword(email);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/verify")
    public ResponseEntity verifyToken(@RequestParam("token") String token) {
        passwordResetTokenService.verifyToken(token);
        return ResponseEntity.ok().build();
    }
}
