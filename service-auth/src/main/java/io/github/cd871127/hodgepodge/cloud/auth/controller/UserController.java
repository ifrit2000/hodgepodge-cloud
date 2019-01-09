package io.github.cd871127.hodgepodge.cloud.auth.controller;

import io.github.cd871127.hodgepodge.cloud.auth.exception.PasswordNotMatchException;
import io.github.cd871127.hodgepodge.cloud.auth.exception.UserException;
import io.github.cd871127.hodgepodge.cloud.auth.exception.UserExistException;
import io.github.cd871127.hodgepodge.cloud.auth.exception.UserNotExistException;
import io.github.cd871127.hodgepodge.cloud.auth.service.UserService;
import io.github.cd871127.hodgepodge.cloud.lib.user.UserInfo;
import io.github.cd871127.hodgepodge.cloud.lib.util.ResponseException;
import io.github.cd871127.hodgepodge.cloud.lib.web.server.response.ServerResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;

import static io.github.cd871127.hodgepodge.cloud.auth.util.response.UserResponse.*;
import static io.github.cd871127.hodgepodge.cloud.lib.web.server.response.GeneralHodgepodgeResponse.FAILED;
import static io.github.cd871127.hodgepodge.cloud.lib.web.server.response.GeneralHodgepodgeResponse.SUCCESSFUL;

@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    @Resource
    private UserService userService;

    @PostMapping("")
    public ServerResponse<UserInfo> register(@RequestBody UserInfo userInfo) throws UserExistException, ResponseException {
        UserInfo resultUserInfo = userService.addUserInfo(userInfo);
        ServerResponse<UserInfo> serverResponse = new ServerResponse<>(SUCCESSFUL);
        serverResponse.setData(resultUserInfo);
        return serverResponse;
    }

    @PatchMapping("{userId}")
    public ServerResponse changePassword(@PathVariable String userId, @RequestBody Map<String, String> paraMap) throws UserNotExistException, PasswordNotMatchException, ResponseException {
        userService.changePassword(userId, paraMap.get("newPasswordKeyId"), paraMap.get("newPassword"), paraMap.get("oldPassword"));
        return new ServerResponse<>(SUCCESSFUL);
    }


    @ExceptionHandler(UserException.class)
    public ServerResponse userExceptionHandler(UserException userException) {
        ServerResponse serverResponse = new ServerResponse(FAILED);
        log.error(userException.getMessage());
        try {
            throw userException;
        } catch (UserExistException e) {
            serverResponse.setHodgepodgeResponse(USER_EXIST);
        } catch (PasswordNotMatchException e) {
            serverResponse.setHodgepodgeResponse(INVALID_PASSWORD);
        } catch (UserException e) {
            serverResponse.setHodgepodgeResponse(USER_ERROR);
        }
        return serverResponse;
    }
}
