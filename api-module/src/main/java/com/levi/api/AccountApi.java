package com.levi.api;

import com.dtflys.forest.annotation.Get;
import com.dtflys.forest.annotation.Post;
import com.levi.model.request.AccountRequest;
import com.levi.model.view.AccountView;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/account")
public interface AccountApi {
    @PostMapping("/login")
    @Post("/account/login")
    AccountView login(@RequestBody AccountRequest accountRequest);

    @GetMapping("/logout")
    @Get("/account/logout")
    void logout();

    @PostMapping("/register")
    @Post("/account/register")
    AccountView register(@RequestBody AccountRequest accountRequest);

    @GetMapping("/user-info")
    @Get("/account/user-info")
    AccountView getUserInfo(@RequestParam(required = false) Long userId);
}
