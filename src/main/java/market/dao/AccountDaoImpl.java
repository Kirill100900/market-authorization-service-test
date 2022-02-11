package market.dao;

import market.model.Account;
import market.service.RoleService;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class AccountDaoImpl implements AccountDao {

    @PersistenceContext
    private EntityManager em;
    private final RoleService roleService;

    public AccountDaoImpl(RoleService roleService) {
        this.roleService = roleService;
    }

    @Override
    public Boolean existAccountByEmail(String email) {
        String query = """
                SELECT COUNT(a.id) 
                FROM Account a
                WHERE a.email = :email
                """;
        return em.createQuery(query, Long.class).setParameter("email", email).getSingleResult() > 0;
    }

    @Override
    public Account saveAccount(Account account) {
        long roleId = roleService.getRoleIdByRoleName(account.getRole().getAuthority());

        if (roleId == 0L) {
            em.persist(account.getRole());
        } else {
            account.getRole().setId(roleId);
        }

        em.persist(account);
        return account;
    }

    @Override
    public Account findAccountByEmail(String email) {
        String query = """
                SELECT a
                FROM Account a
                WHERE a.email = :email
                """;
        try {
            return em.createQuery(query, Account.class)
                    .setParameter("email", email)
                    .getSingleResult();
        } catch (NoResultException ignored) {
            return null;
        }
    }

    @Override
    public List<Account> findAll() {
        String query = """
                SELECT a
                FROM Account a
                """;
        try {
            return em.createQuery(query, Account.class)
                    .getResultList();
        } catch (NoResultException ignored) {
            return null;
        }
    }
}
