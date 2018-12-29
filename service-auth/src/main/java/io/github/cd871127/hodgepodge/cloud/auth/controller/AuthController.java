package io.github.cd871127.hodgepodge.cloud.auth.controller;

import io.github.cd871127.hodgepodge.cloud.auth.exception.UserExistException;
import io.github.cd871127.hodgepodge.cloud.auth.mapper.AuthMapper;
import io.github.cd871127.hodgepodge.cloud.auth.service.AuthService;
import io.github.cd871127.hodgepodge.cloud.lib.user.UserInfo;
import io.github.cd871127.hodgepodge.cloud.lib.util.Pair;
import io.github.cd871127.hodgepodge.cloud.lib.util.ResponseException;
import io.github.cd871127.hodgepodge.cloud.lib.web.server.response.ServerResponse;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

import static io.github.cd871127.hodgepodge.cloud.lib.web.server.response.GeneralHodgepodgeResponse.SUCCESSFUL;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Resource
    private AuthService authService;


    @PostMapping("{userId}")
    public ServerResponse<UserInfo> login(@PathVariable String userId, @RequestBody String password) throws ResponseException, UserExistException {
        ServerResponse<UserInfo> serverResponse = new ServerResponse<>(SUCCESSFUL);
        serverResponse.setData(authService.login(userId,password));
        return serverResponse;
    }
}
