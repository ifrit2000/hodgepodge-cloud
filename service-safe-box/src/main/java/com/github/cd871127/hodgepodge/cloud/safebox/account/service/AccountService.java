package com.github.cd871127.hodgepodge.cloud.safebox.account.service;

import com.github.cd871127.hodgepodge.cloud.safebox.account.dto.AccountInfoDTO;
import com.github.cd871127.hodgepodge.cloud.safebox.account.mapper.AccountMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service
public class AccountService {

    @Resource
    private AccountMapper accountMapper;

    @Transactional(rollbackFor = Exception.class)
    public AccountInfoDTO addUserAccount() {
        return null;
    }
}
