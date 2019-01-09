package io.github.cd871127.hodgepodge.cloud.auth.service;

import io.github.cd871127.hodgepodge.cloud.auth.exception.LoginFailedException;
import io.github.cd871127.hodgepodge.cloud.auth.exception.UserNotExistException;
import io.github.cd871127.hodgepodge.cloud.auth.mapper.UserMapper;
import io.github.cd871127.hodgepodge.cloud.lib.cipher.CipherDataEntity;
import io.github.cd871127.hodgepodge.cloud.lib.user.UserInfo;
import io.github.cd871127.hodgepodge.cloud.lib.util.IdGenerator;
import io.github.cd871127.hodgepodge.cloud.lib.util.ResponseException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class AuthService {

    @Resource
    private UserMapper userMapper;

    @Resource
    private RedisTemplate<String, UserInfo> redisTemplate;

    private static final String TOKEN_PREFIX = "TOKEN.";
    private static final String USER_INFO_PREFIX = "USER_INFO.";

    @Resource
    private CipherService cipherService;

    Boolean verifyPassword(String keyId1, String password1, String keyId2, String password2) throws ResponseException {
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
        userInfo.setPassword(null);
        userInfo.setPasswordKeyId(null);
        redisTemplate.opsForValue().set(TOKEN_PREFIX + userInfo.getToken(), userInfo, 30, TimeUnit.MINUTES);
        redisTemplate.opsForValue().set(USER_INFO_PREFIX + userInfo.getUserId(), userInfo, 30, TimeUnit.MINUTES);
        return userInfo;
    }
}