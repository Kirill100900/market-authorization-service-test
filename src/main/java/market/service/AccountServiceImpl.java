package market.service;

import market.dao.AccountDao;
import market.dto.AccountDto;
import market.feign.ProfileFeignClient;
import market.model.Account;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AccountServiceImpl implements AccountService {

    private final AccountDao accountDao;
    private final PasswordEncoder passwordEncoder;
    private final ProfileFeignClient profileFeignClient;

    public AccountServiceImpl(AccountDao accountDao, PasswordEncoder passwordEncoder,
                              @Lazy ProfileFeignClient profileFeignClient) {
        this.accountDao = accountDao;
        this.passwordEncoder = passwordEncoder;
        this.profileFeignClient = profileFeignClient;
    }


    @Override
    public Boolean existAccountByEmail(String email) {
        return accountDao.existAccountByEmail(email);
    }

    @Transactional
    @Override
    public Account saveAccount(Account account) {
        if (account.getPassword() != null) {
            account.setPassword(passwordEncoder.encode(account.getPassword()));
        }
        return accountDao.saveAccount(account);
    }

    @Override
    public Account findAccountByEmail(String email) {
        return accountDao.findAccountByEmail(email);
    }

    @Override
    public List<AccountDto> findAllAccount(String search) {
        List<Account> accountList = accountDao.findAll(search);
        if (accountList == null) {
            return Collections.emptyList();
        }
        return accountList.stream()
                .map(account -> {
                    long profileId = profileFeignClient.findProfileById(account.getId())
                            .id();
                    return new AccountDto(account, profileId);
                })
                .collect(Collectors.toList());

    }

}
