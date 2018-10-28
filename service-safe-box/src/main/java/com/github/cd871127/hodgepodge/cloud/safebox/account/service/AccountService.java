package com.github.cd871127.hodgepodge.cloud.safebox.account.service;

import com.github.cd871127.hodgepodge.cloud.safebox.account.dto.AccountDTO;
import com.github.cd871127.hodgepodge.cloud.safebox.account.mapper.AccountMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class AccountService {

    @Resource
    private AccountMapper accountMapper;

    public AccountDTO addUserAccount() {
        return null;
    }
}
