package com.levi.controller;

import com.levi.api.AccountApi;
import com.levi.model.request.AccountRequest;
import com.levi.model.view.AccountView;
import com.levi.service.AccountService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@CrossOrigin("*")
public class AccountController implements AccountApi {

    @Resource
    private AccountService accountService;

    @Override
    public AccountView login(AccountRequest accountRequest) {
        return accountService.login(accountRequest);
    }

    @Override
    public void logout() {
        accountService.logout();
    }

    @Override
    public AccountView register(AccountRequest accountRequest) {
        return accountService.register(accountRequest);
    }

    @Override
    public AccountView getUserInfo(Long userId) {
        return accountService.getAccountInfo(userId);
    }
}
