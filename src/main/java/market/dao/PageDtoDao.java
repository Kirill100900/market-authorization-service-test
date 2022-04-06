package market.dao;

import market.dto.AccountDto;

import java.util.List;

public interface PageDtoDao {
    long getTotalEntitiesCount();

    List<AccountDto> getEntitiesList(int currentPage, int countOnPage, List<AccountDto> accounts);
}

