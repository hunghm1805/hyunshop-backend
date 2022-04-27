package com.hyunshop.service.account;

import com.hyunshop.entity.account.AccountEntity;

public interface AccountService {
    AccountEntity findByUsername(String username);
}
