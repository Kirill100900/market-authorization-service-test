package market.service;

public interface PasswordResetTokenService {
    public boolean resetPassword(String email);

    public boolean verifyToken(String token);
}
