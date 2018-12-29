package io.github.cd871127.hodgepodge.cloud.auth.service;

import io.github.cd871127.hodgepodge.cloud.auth.mapper.AuthMapper;
import io.github.cd871127.hodgepodge.cloud.auth.mapper.UserMapper;
import io.github.cd871127.hodgepodge.cloud.lib.cipher.CipherDataEntity;
import io.github.cd871127.hodgepodge.cloud.lib.user.UserInfo;
import io.github.cd871127.hodgepodge.cloud.lib.util.ResponseException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@Service
public class AuthService {
    @Resource
    private AuthMapper authMapper;

    @Resource
    private UserMapper userMapper;

    @Resource
    private CipherService cipherService;

    public boolean verifyPassword(String userId, String password) throws ResponseException {
        UserInfo userInfo = userMapper.selectSingleUserInfo(userId);
        String keyId = userInfo.getPasswordKeyId();
        CipherDataEntity data1 = new CipherDataEntity(keyId, password);
        CipherDataEntity data2 = new CipherDataEntity(keyId, userInfo.getPassword());
        return cipherService.comparison(data1, data2);
    }
}
