package market.service;

import market.dao.RoleDao;
import market.model.Role;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RoleServiceImpl implements RoleService {
    private final RoleDao roleDao;

    public RoleServiceImpl(RoleDao roleDao) {
        this.roleDao = roleDao;
    }

    @Override
    public Role getRoleByName(String role) {
        return roleDao.getRoleByName(role);
    }

    @Override
    public long getRoleIdByRoleName(String role) {
        return roleDao.getRoleIdByRoleName(role);
    }
}
