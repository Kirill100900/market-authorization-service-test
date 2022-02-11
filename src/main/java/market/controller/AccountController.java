package market.controller;

import market.dto.AccountDto;
import market.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping(value = "/api")
public class AccountController {

    private AccountService accountService;


    @Autowired
    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping("/admin/accounts")
    public ResponseEntity<List<AccountDto>> getAccounts() {
        return new ResponseEntity<>(accountService.findAllAccount(), HttpStatus.OK);
    }
}
