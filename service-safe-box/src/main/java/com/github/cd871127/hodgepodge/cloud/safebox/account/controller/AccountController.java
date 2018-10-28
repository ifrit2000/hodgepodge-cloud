package com.github.cd871127.hodgepodge.cloud.safebox.account.controller;

import com.github.cd871127.hodgepodge.cloud.lib.server.response.ServerResponse;
import com.github.cd871127.hodgepodge.cloud.safebox.account.dto.AccountInfoDTO;
import com.github.cd871127.hodgepodge.cloud.safebox.account.service.AccountService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

import static com.github.cd871127.hodgepodge.cloud.lib.server.response.CommonResponseInfo.SUCCESSFUL;

@RestController
@RequestMapping("/account")
public class AccountController {

    @Resource
    private AccountService accountService;

    @RequestMapping(value = "{userId}", method = RequestMethod.POST)
    public ServerResponse addUserAccount(@PathVariable String userId, @RequestBody AccountInfoDTO accountInfoDTO) {
        return new ServerResponse(SUCCESSFUL);
    }

    @RequestMapping(value = "{userId}/{accountId}", method = RequestMethod.GET)
    public ServerResponse getUserAccount(@PathVariable String userId, @PathVariable(required = false) String accountId) {
        return new ServerResponse(SUCCESSFUL);
    }

}
