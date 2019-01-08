package io.github.cd871127.hodgepodge.cloud.auth.controller;

import io.github.cd871127.hodgepodge.cloud.auth.exception.AuthException;
import io.github.cd871127.hodgepodge.cloud.auth.exception.LoginFailedException;
import io.github.cd871127.hodgepodge.cloud.auth.exception.UserNotExistException;
import io.github.cd871127.hodgepodge.cloud.auth.service.AuthService;
import io.github.cd871127.hodgepodge.cloud.lib.user.UserInfo;
import io.github.cd871127.hodgepodge.cloud.lib.util.ResponseException;
import io.github.cd871127.hodgepodge.cloud.lib.web.server.response.ServerResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

import static io.github.cd871127.hodgepodge.cloud.auth.util.response.UserResponse.INVALID_PASSWORD;
import static io.github.cd871127.hodgepodge.cloud.auth.util.response.UserResponse.USER_NOT_EXIST;
import static io.github.cd871127.hodgepodge.cloud.lib.web.server.response.GeneralHodgepodgeResponse.*;

@RestController
@RequestMapping("/auth")
@Slf4j
public class AuthController {

    @Resource
    private AuthService authService;


    @PostMapping("{userId}/{keyId}")
    public ServerResponse<UserInfo> login(@PathVariable String userId, @PathVariable String keyId, @RequestBody String password) throws ResponseException, UserNotExistException, LoginFailedException {
        ServerResponse<UserInfo> serverResponse = new ServerResponse<>(SUCCESSFUL);
        serverResponse.setData(authService.login(userId, password, keyId));
        return serverResponse;
    }

    @ExceptionHandler(value = {AuthException.class})
    public ServerResponse authExceptionHandler(AuthException exception) {
        ServerResponse serverResponse = new ServerResponse(FAILED);
        log.error(exception.getMessage());
        try {
            throw exception;
        } catch (UserNotExistException e) {
            serverResponse.setHodgepodgeResponse(USER_NOT_EXIST);
        } catch (LoginFailedException e) {
            serverResponse.setHodgepodgeResponse(INVALID_PASSWORD);
        } catch (AuthException e) {
            serverResponse.setHodgepodgeResponse(UNKNOWN_ERROR);
        }
        return serverResponse;
    }
}
