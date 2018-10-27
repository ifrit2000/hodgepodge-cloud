package com.github.cd871127.hodgepodge.cloud.safebox.account.controller;

import com.github.cd871127.hodgepodge.cloud.lib.server.response.ServerResponse;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.github.cd871127.hodgepodge.cloud.lib.server.response.CommonResponseInfo.SUCCESSFUL;

@RestController
@RequestMapping("/account")
public class AccountController {

    @RequestMapping("test")
    public ServerResponse test() {
        return new ServerResponse(SUCCESSFUL);
    }

}
