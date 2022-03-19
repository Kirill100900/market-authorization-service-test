package market.dao;

import market.model.Role;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

@Repository
public class RoleDaoImpl implements RoleDao {
    @PersistenceContext
    private EntityManager em;

    @Override
    public Role getRoleByName(String role) {
        String query = """
                SELECT r
                FROM Role r
                WHERE r.authority = :authority
                """;
        try {
            return em.createQuery(query, Role.class)
                    .setParameter("authority", role)
                    .getSingleResult();
        } catch (NoResultException ignored) {
            return null;
        }
    }

    @Override
    public long getRoleIdByRoleName(String role) {
        String query = """
                SELECT id
                FROM Role
                WHERE authority = :authority
                """;
        try {
            return em.createQuery(query, Long.class)
                    .setParameter("authority", role)
                    .getSingleResult();
        } catch (NoResultException ignored) {
            return 0L;
        }
    }

    @Override
    @Transactional
    public void saveRole(Role role) {
        em.persist(role);
    }
}
