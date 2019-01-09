package io.github.cd871127.hodgepodge.cloud.safebox.account.controller;

import io.github.cd871127.hodgepodge.cloud.lib.web.server.response.ServerResponse;
import io.github.cd871127.hodgepodge.cloud.safebox.account.dto.AccountInfoDTO;
import io.github.cd871127.hodgepodge.cloud.safebox.account.service.AccountService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

import static io.github.cd871127.hodgepodge.cloud.lib.web.server.response.GeneralHodgepodgeResponse.SUCCESSFUL;

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
