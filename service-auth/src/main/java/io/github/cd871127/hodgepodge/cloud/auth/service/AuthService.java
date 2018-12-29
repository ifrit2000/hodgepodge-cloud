package io.github.cd871127.hodgepodge.cloud.auth.service;

import io.github.cd871127.hodgepodge.cloud.auth.mapper.AuthMapper;
import io.github.cd871127.hodgepodge.cloud.auth.mapper.UserMapper;
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
        Map<String, String> passwordMap1 = new HashMap<>();
        passwordMap1.put("keyId", keyId);
        passwordMap1.put("data", password);
        Map<String, String> passwordMap2 = new HashMap<>();
        passwordMap2.put("keyId", userInfo.getPassword());
        return cipherService.comparison(passwordMap1, passwordMap2);
    }
}
