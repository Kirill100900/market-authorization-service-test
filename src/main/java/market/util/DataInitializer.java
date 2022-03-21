package market.util;

import market.dao.RoleDao;
import market.dto.ProfileDto;
import market.feign.ProfileFeignClient;
import market.model.Account;
import market.model.Role;
import market.service.AccountService;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public record DataInitializer(AccountService accountService, RoleDao roleDao, ProfileFeignClient profile) {

    @PostConstruct
    private void init() {
        Roles[] values = Roles.values();
        Account user = new Account("user@mail.ru", "user", roleDao.getRoleByName("ROLE_USER"), false);
        Account admin = new Account("admin@mail.ru", "admin", roleDao.getRoleByName("ROLE_ADMIN"), false);

        for (Roles value :
                values) {
            if (roleDao.getRoleByName(value.getRoleName()) == null) {
                roleDao.saveRole(new Role(value.getRoleName()));
            }
        }

        if (Boolean.FALSE.equals(accountService.existAccountByEmail("user@mail.ru"))) {
            accountService.saveAccount(user);
            profile.saveProfile(new ProfileDto(null, user.getId(), user.getEmail(), "User", "User"));
        }
        if (Boolean.FALSE.equals(accountService.existAccountByEmail("admin@mail.ru"))) {
            accountService.saveAccount(admin);
            profile.saveProfile(new ProfileDto(null, admin.getId(), admin.getEmail(), "Admin", "Admin"));
        }
    }
}
