package market.service;

import market.dto.AccountDto;
import market.model.Account;

import java.util.List;

public interface AccountService {
    Boolean existAccountByEmail(String email);

    Account saveAccount(Account account);

    Account findAccountByEmail(String email);

    List<AccountDto> findAllAccount(String search);

}
