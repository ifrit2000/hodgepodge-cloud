package com.github.cd871127.hodgepodge.cloud.authentication.controller;

import com.github.cd871127.hodgepodge.cloud.authentication.mapper.AuthenticationMapper;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RequestMapping("/authentication")
@RestController
public class AuthenticationController {

    @Resource
    private AuthenticationMapper authenticationMapper;

    @RequestMapping("test")
    public int test() {
        return authenticationMapper.test();
    }



}
