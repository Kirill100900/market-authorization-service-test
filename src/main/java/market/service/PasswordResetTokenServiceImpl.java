package market.service;

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
    public boolean resetPassword(String email) {
        Account account = accountService.findAccountByEmail(email);
        if (account == null) {
            return false;
        }

        passwordResetTokenRepository.deleteAllByAccountId(account.getId());

        String token = createPasswordResetTokenForAccount(account);
        System.out.println("Token:" + token);
        /// TODO Отправка письма с токеном

        return true;
    }

    @Override
    public boolean verifyToken(String token) {
        PasswordResetToken passwordResetToken = passwordResetTokenRepository.findByToken(token);
        return passwordResetToken != null ? isTokenNotExpired(passwordResetToken) : false;
    }

    private String createPasswordResetTokenForAccount(Account account) {
        String token = UUID.randomUUID().toString();
        PasswordResetToken passwordResetToken = new PasswordResetToken(account, token);
        passwordResetTokenRepository.save(passwordResetToken);
        return token;
    }

    private boolean isTokenNotExpired(PasswordResetToken passToken) {
        final Calendar cal = Calendar.getInstance();
        return !passToken.getExpiryDate().before(cal.getTime());
    }
}
