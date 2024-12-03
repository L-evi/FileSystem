package com.levi.service;

import com.github.yulichang.base.MPJBaseService;
import com.levi.model.entity.AccountEntity;
import com.levi.model.request.AccountRequest;
import com.levi.model.view.AccountView;

public interface AccountService extends MPJBaseService<AccountEntity> {


    AccountView login(AccountRequest accountRequest);

    void logout();

    AccountView register(AccountRequest accountRequest);

    AccountView getAccountInfo(Long userId);

}
