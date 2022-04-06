package market.service;

import market.dao.PageDtoDao;
import market.dto.AccountDto;
import market.dto.PageDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PageDtoServiceImpl implements PageDtoService {

    private PageDtoDao pageDtoDao;

    @Autowired
    public PageDtoServiceImpl(PageDtoDao pageDtoDao) {
        this.pageDtoDao = pageDtoDao;
    }

    @Override
    public PageDto getPageDto(int currentPage, int countOnPage, List<AccountDto> accounts) {
        PageDto pageDto = new PageDto();
        pageDto.setTotalEntitiesCount(pageDtoDao.getTotalEntitiesCount());
        pageDto.setCurrentPage(currentPage);
        pageDto.setCountOnPage(countOnPage);
        pageDto.setPageCount((int) (pageDto.getTotalEntitiesCount() / countOnPage) + 1);
        pageDto.setEntities(pageDtoDao.getEntitiesList(currentPage, countOnPage, accounts));
        return pageDto;
    }
}
