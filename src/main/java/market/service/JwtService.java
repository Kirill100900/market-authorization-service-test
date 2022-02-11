package market.service;

import market.model.Account;

public interface JwtService {
    String createToken(Account account);
}
