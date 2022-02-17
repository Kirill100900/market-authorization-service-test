package market.controller;

import market.dto.AccountDto;
import market.response.Response;
import market.response.SuccessResponse;
import market.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

// TODO @Константин
// РЕСПОНС везде
// где пагинация? должна быть тут
@RestController
@RequestMapping(value = "/api/admin/accounts")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping
    public Response<List<AccountDto>> getAccounts() {
        SuccessResponse<List<AccountDto>> response = Response.success(accountService.findAllAccount());
        response.setStatus(200);
        return response;
    }
}
