package com.github.cd871127.hodgepodge.cloud.safebox.account.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/account")
public class AccountController {

    @RequestMapping("test")
    public String test() {
        return "123";
    }

}
