package com.github.cd871127.hodgepodge.cloud.authentication.controller;

import com.github.cd871127.hodgepodge.cloud.authentication.service.AuthenticationService;
import com.github.cd871127.hodgepodge.cloud.lib.user.UserInfo;
import com.github.cd871127.hodgepodge.cloud.lib.web.AbstractController;

import com.github.cd871127.hodgepodge.cloud.lib.web.server.response.CommonResponseInfo;
import com.github.cd871127.hodgepodge.cloud.lib.web.server.response.ResponseInfo;
import com.github.cd871127.hodgepodge.cloud.lib.web.server.response.ServerResponse;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RequestMapping("/authentication")
@RestController
public class AuthenticationController extends AbstractController {

    @Resource
    private AuthenticationService authenticationService;

    /**
     * add new user
     *
     * @param userInfo include username & password
     * @return userInfo include valid token and info from parameter
     */
    @RequestMapping("register")
    public ServerResponse<UserInfo> register(UserInfo userInfo) {
        ServerResponse<UserInfo> serverResponse = new ServerResponse<>(CommonResponseInfo.SUCCESSFUL);
        authenticationService.register(userInfo);
        return null;
    }

}
