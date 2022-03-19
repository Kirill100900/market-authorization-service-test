package market.util;

import market.dao.AccountDao;
import market.dao.RoleDao;
import market.model.Account;
import market.model.Role;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public record DataInitializer(AccountDao accountDao, RoleDao roleDao) {

    @PostConstruct
    private void init() {
        Roles[] values = Roles.values();

        for (Roles value :
                values) {
            if (roleDao.getRoleByName(value.getRoleName()) == null) {
                roleDao.saveRole(new Role(value.getRoleName()));
            }
        }

        if (Boolean.FALSE.equals(accountDao.existAccountByEmail("user@mail.ru"))) {
            accountDao.saveAccount(new Account("user@mail.ru", "user", roleDao.getRoleByName("ROLE_USER"), false));
        }
        if (Boolean.FALSE.equals(accountDao.existAccountByEmail("admin@mail.ru"))) {
            accountDao.saveAccount(new Account("admin@mail.ru", "admin", roleDao.getRoleByName("ROLE_ADMIN"), false));
        }
    }
}
