package io.github.cd871127.hodgepodge.cloud.auth.service;

import io.github.cd871127.hodgepodge.cloud.auth.exception.UserExistException;
import io.github.cd871127.hodgepodge.cloud.auth.mapper.AuthMapper;
import io.github.cd871127.hodgepodge.cloud.auth.mapper.UserMapper;
import io.github.cd871127.hodgepodge.cloud.lib.cipher.CipherDataEntity;
import io.github.cd871127.hodgepodge.cloud.lib.user.UserInfo;
import io.github.cd871127.hodgepodge.cloud.lib.util.ResponseException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class AuthService {
    @Resource
    private AuthMapper authMapper;

    @Resource
    private UserMapper userMapper;

    @Resource
    private CipherService cipherService;

    public boolean verifyPassword(String keyId, String password1, String password2) throws ResponseException {
        CipherDataEntity data1 = new CipherDataEntity(keyId, password1);
        CipherDataEntity data2 = new CipherDataEntity(keyId, password2);
        return cipherService.comparison(data1, data2);
    }

    public UserInfo login(String userId, String password) throws ResponseException, UserExistException {
        UserInfo userInfo = userMapper.selectSingleUserInfo(userId);
        Boolean res = verifyPassword(userInfo.getPasswordKeyId(), password, userInfo.getPassword());
        if (res) {
            //TODO generate token
            System.out.println(1);
        } else {
            throw new UserExistException();
        }
        return userInfo;
    }
}
