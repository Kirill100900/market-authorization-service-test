package market.controller;

import market.service.PasswordResetTokenService;
import org.springframework.http.HttpStatus;
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
        return passwordResetTokenService.resetPassword(email) ?
                ResponseEntity.ok().build() :
                ResponseEntity.notFound().build();
    }

    @PostMapping("/verify")
    public ResponseEntity verifyToken(@RequestParam("token") String token) {
        return passwordResetTokenService.verifyToken(token) ?
                ResponseEntity.ok().build() :
                ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
    }
}
