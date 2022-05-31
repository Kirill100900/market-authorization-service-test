package market.service;

public interface PasswordResetTokenService {
    public void resetPassword(String email);

    public void verifyToken(String token);
}
