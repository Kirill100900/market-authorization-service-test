package market.dao;

import market.dto.AccountDto;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

@Repository
public class PageDtoDaoImpl implements PageDtoDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public long getTotalEntitiesCount() {
        Query query = entityManager.createQuery(
                """
                        SELECT COUNT (a)
                        FROM Account a
                        """
        );
        return (long) query.getSingleResult();
    }

    @Override
    @Transactional
    public List<AccountDto> getEntitiesList(int currentPage, int countOnPage, List<AccountDto> accounts) {
        int fromIndex = (currentPage - 1) * countOnPage;
        return accounts.subList(fromIndex, Math.min(fromIndex + countOnPage, accounts.size()));
    }
}

