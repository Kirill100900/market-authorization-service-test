package market.service;

import market.dto.AccountDto;
import market.dto.PageDto;

import java.util.List;

public interface PageDtoService{
    PageDto getPageDto(int currentPage, int countOnPage, List<AccountDto> accounts);
}
