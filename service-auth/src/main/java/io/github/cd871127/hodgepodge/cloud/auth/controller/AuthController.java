package io.github.cd871127.hodgepodge.cloud.auth.controller;

import io.github.cd871127.hodgepodge.cloud.auth.service.AuthService;
import io.github.cd871127.hodgepodge.cloud.lib.user.UserInfo;
import io.github.cd871127.hodgepodge.cloud.lib.web.server.response.ServerResponse;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

import static io.github.cd871127.hodgepodge.cloud.lib.web.server.response.GeneralHodgepodgeResponse.SUCCESSFUL;

@RestController("/auth")
public class AuthController {

    @Resource
    private AuthService authService;

    @GetMapping("user/{userId}/{password}")
    public ServerResponse<UserInfo> login(@PathVariable String userId, @PathVariable String password) {
        UserInfo userInfo = new UserInfo();
        userInfo.setUserId(userId);
        userInfo.setPassword(password);
        UserInfo resultUserInfo = authService.login(userInfo);
        ServerResponse<UserInfo> serverResponse = new ServerResponse<>(SUCCESSFUL);
        serverResponse.setData(resultUserInfo);
        return serverResponse;
    }

    @PostMapping("user")
    public ServerResponse register(@RequestBody UserInfo userInfo) {
        UserInfo resultUserInfo = authService.register(userInfo);
        ServerResponse<UserInfo> serverResponse = new ServerResponse<>(SUCCESSFUL);
        serverResponse.setData(resultUserInfo);
        return serverResponse;
    }
}
