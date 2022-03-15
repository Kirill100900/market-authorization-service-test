package market.util;

import market.model.Account;
import market.model.Role;
import market.service.AccountService;
import market.service.RoleService;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public record DataInitializer(AccountService accountService, RoleService roleService) {

    @PostConstruct
    private void init() {
        if (roleService.getRoleByName("ROLE_USER") == null) {
            roleService.saveRole(new Role("ROLE_USER"));
        }
        if (roleService.getRoleByName("ROLE_ADMIN") == null) {
            roleService.saveRole(new Role("ROLE_ADMIN"));
        }
        if (Boolean.FALSE.equals(accountService.existAccountByEmail("user@mail.ru"))) {
            accountService.saveAccount(new Account("user@mail.ru", "user", roleService.getRoleByName("ROLE_USER"), false));
        }
        if (Boolean.FALSE.equals(accountService.existAccountByEmail("admin@mail.ru"))) {
            accountService.saveAccount(new Account("admin@mail.ru", "admin", roleService.getRoleByName("ROLE_ADMIN"), false));
        }
    }
}
