package com.github.cd871127.hodgepodge.cloud.authentication.controller;

import com.github.cd871127.hodgepodge.cloud.authentication.service.AuthenticationService;
import com.github.cd871127.hodgepodge.cloud.lib.user.UserInfo;
import com.github.cd871127.hodgepodge.cloud.lib.web.AbstractController;
import com.github.cd871127.hodgepodge.cloud.lib.web.server.response.CommonResponseInfo;
import com.github.cd871127.hodgepodge.cloud.lib.web.server.response.ServerResponse;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

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
    @RequestMapping(value = "register", method = POST)
    public ServerResponse<UserInfo> register(UserInfo userInfo) {
        ServerResponse<UserInfo> serverResponse = new ServerResponse<>(CommonResponseInfo.SUCCESSFUL);
        authenticationService.register(userInfo);
        return serverResponse;
    }

    /**
     * user login controller
     *
     * @param userId
     * @param password encoded password
     * @return
     */
    @RequestMapping(value = "user/{userId}", method = GET)
    public ServerResponse<UserInfo> login(@PathVariable String userId, @RequestParam String password) {
        ServerResponse<UserInfo> serverResponse = new ServerResponse<>(CommonResponseInfo.SUCCESSFUL);
        serverResponse.setData(authenticationService.login(userId, password));
        return serverResponse;
    }

}
