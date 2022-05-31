package market.service;

import market.exception.AccountNotExistException;
import market.exception.TokenExpireOrNotFoundException;
import market.model.Account;
import market.model.PasswordResetToken;
import market.repositories.PasswordResetTokenRepository;
import org.springframework.stereotype.Service;


import java.util.Calendar;
import java.util.UUID;

@Service
public class PasswordResetTokenServiceImpl implements PasswordResetTokenService {

    private final AccountService accountService;
    private final PasswordResetTokenRepository passwordResetTokenRepository;

    public PasswordResetTokenServiceImpl(AccountService accountService, PasswordResetTokenRepository passwordResetTokenRepository) {
        this.accountService = accountService;
        this.passwordResetTokenRepository = passwordResetTokenRepository;
    }

    @Override
    public void resetPassword(String email) {
        Account account = accountService.findAccountByEmail(email);
        if (account == null) {
            throw new AccountNotExistException(String.format("Account with email: %s not found", email));
        }
        passwordResetTokenRepository.deleteAllByAccountId(account.getId());
        String token = createPasswordResetTokenForAccount(account);
        /// TODO Отправка письма с токеном
    }

    @Override
    public void verifyToken(String token) {
        PasswordResetToken passwordResetToken = passwordResetTokenRepository.findByToken(token);
        if (passwordResetToken == null || isTokenExpired(passwordResetToken)) {
            throw new TokenExpireOrNotFoundException(String.format("Token: %s expire or not found", token));
        }
    }

    private String createPasswordResetTokenForAccount(Account account) {
        String token = UUID.randomUUID().toString();
        PasswordResetToken passwordResetToken = new PasswordResetToken(account, token);
        passwordResetTokenRepository.save(passwordResetToken);
        return token;
    }

    private boolean isTokenExpired(PasswordResetToken passToken) {
        final Calendar cal = Calendar.getInstance();
        return passToken.getExpiryDate().before(cal.getTime());
    }
}
