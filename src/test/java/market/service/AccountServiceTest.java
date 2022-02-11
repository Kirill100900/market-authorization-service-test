package market.service;

import market.AuthorizationStarter;
import market.model.Account;
import market.model.Role;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Service;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {AuthorizationStarter.class})
public class AccountServiceTest {

    @Autowired
    AccountService accountService;

    @Test
    public void add(){
        Role role = new Role("ROLE_BARMEN");
        Account account = new Account();
        account.setEmail("test1@mail.ru");
        account.setRole(role);
        accountService.saveAccount(account);
    }
}
