package market.controller;

import market.dto.AccountDto;
import market.dto.PageDto;
import market.model.Account;
import market.response.Response;
import market.response.SuccessResponse;
import market.service.AccountService;
import market.service.PageDtoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

// TODO @Константин
// РЕСПОНС везде
// где пагинация? должна быть тут
@RestController
@RequestMapping(value = "/api/admin/accounts")
public class AccountController {

    private final AccountService accountService;
    private final PageDtoService pageDtoService;

    @Value("${countOnPage}")
    private int countOnPage;


    public AccountController(AccountService accountService, PageDtoService pageDtoService) {
        this.accountService = accountService;
        this.pageDtoService = pageDtoService;
    }

    @GetMapping
    public Response<List<AccountDto>> getAccounts(
            @RequestParam(value = "search", defaultValue = "") String search
    ) {
        SuccessResponse<List<AccountDto>> response = Response.success(accountService.findAllAccount(search));
        response.setStatus(200);
        return response;
    }

    @GetMapping("/page")
    public Response<PageDto> getAccountsPage(
            @RequestParam int currentPage,
            @RequestParam(value = "search", defaultValue = "") String search
    ) {
        SuccessResponse<PageDto> response = Response.success(pageDtoService.getPageDto(currentPage,
                this.countOnPage,
                accountService.findAllAccount(search)));
        response.setStatus(200);
        return response;
    }


}
