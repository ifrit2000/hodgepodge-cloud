package io.github.cd871127.hodgepodge.cloud.auth.service;

import io.github.cd871127.hodgepodge.cloud.auth.exception.LoginFailedException;
import io.github.cd871127.hodgepodge.cloud.auth.exception.UserNotExistException;
import io.github.cd871127.hodgepodge.cloud.auth.mapper.AuthMapper;
import io.github.cd871127.hodgepodge.cloud.auth.mapper.UserMapper;
import io.github.cd871127.hodgepodge.cloud.lib.cipher.CipherDataEntity;
import io.github.cd871127.hodgepodge.cloud.lib.user.UserInfo;
import io.github.cd871127.hodgepodge.cloud.lib.util.IdGenerator;
import io.github.cd871127.hodgepodge.cloud.lib.util.ResponseException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
@Slf4j
public class AuthService {
    @Resource
    private AuthMapper authMapper;

    @Resource
    private UserMapper userMapper;

    @Resource
    private CipherService cipherService;

    public Boolean verifyPassword(String keyId1, String password1, String keyId2, String password2) throws ResponseException {
        CipherDataEntity data1 = new CipherDataEntity(keyId1, password1);
        CipherDataEntity data2 = new CipherDataEntity(keyId2, password2);
        return cipherService.comparison(data1, data2);
    }

    public UserInfo login(String userId, String password, String keyId) throws ResponseException, UserNotExistException, LoginFailedException {
        UserInfo userInfo = userMapper.selectSingleUserInfo(userId);
        if (userInfo == null) {
            throw new UserNotExistException("user not exist");
        }
        Boolean res = verifyPassword(keyId, password, userInfo.getPasswordKeyId(), userInfo.getPassword());
        if (res != null && res) {
            log.info("user {} login, password ok", userId);
        } else {
            throw new LoginFailedException("login failed");
        }
        //add token
        userInfo.setToken(IdGenerator.getId());
        return userInfo;
    }
}