package market.dao;

import market.model.Role;

public interface RoleDao {
    Role getRoleByName(String role);

    long getRoleIdByRoleName(String role);

    void saveRole(Role role);
}
