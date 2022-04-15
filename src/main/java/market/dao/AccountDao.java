package market.dao;

import market.model.Account;

import java.util.List;

public interface AccountDao {
    Boolean existAccountByEmail(String email);

    Account saveAccount(Account account);

    Account findAccountByEmail(String email);

    List<Account> findAll(String search);
}
