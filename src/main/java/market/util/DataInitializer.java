package market.util;

import market.dao.RoleDao;
import market.model.Account;
import market.model.Role;
import market.service.AccountService;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

//@Component
//public record DataInitializer(AccountService accountService, RoleDao roleDao) {
//
//    @PostConstruct
//    private void init() {
//        Roles[] values = Roles.values();
//
//        for (Roles value :
//                values) {
//            if (roleDao.getRoleByName(value.getRoleName()) == null) {
//                roleDao.saveRole(new Role(value.getRoleName()));
//            }
//        }
//
//        if (Boolean.FALSE.equals(accountService.existAccountByEmail("user@mail.ru"))) {
//            accountService.saveAccount(new Account("user@mail.ru", "user", roleDao.getRoleByName("ROLE_USER"), false));
//        }
//        if (Boolean.FALSE.equals(accountService.existAccountByEmail("admin@mail.ru"))) {
//            accountService.saveAccount(new Account("admin@mail.ru", "admin", roleDao.getRoleByName("ROLE_ADMIN"), false));
//        }
//    }
//}
