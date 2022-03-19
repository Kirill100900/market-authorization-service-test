package market.service;

import market.model.Role;

public interface RoleService {
    Role getRoleByName(String role);

    long getRoleIdByRoleName(String role);
}
