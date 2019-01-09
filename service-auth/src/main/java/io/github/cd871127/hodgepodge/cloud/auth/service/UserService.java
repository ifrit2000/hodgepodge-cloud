package io.github.cd871127.hodgepodge.cloud.auth.service;

import io.github.cd871127.hodgepodge.cloud.auth.exception.AuthException;
import io.github.cd871127.hodgepodge.cloud.auth.exception.PasswordNotMatchException;
import io.github.cd871127.hodgepodge.cloud.auth.exception.UserExistException;
import io.github.cd871127.hodgepodge.cloud.auth.exception.UserNotExistException;
import io.github.cd871127.hodgepodge.cloud.auth.mapper.UserMapper;
import io.github.cd871127.hodgepodge.cloud.lib.user.UserInfo;
import io.github.cd871127.hodgepodge.cloud.lib.util.ResponseException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
@Slf4j
public class UserService {
    @Resource
    private UserMapper userMapper;

    @Resource
    private AuthService authService;

    @Resource
    private CipherService cipherService;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    private final String USER_ID_REDIS_KEY = "userIdSet";

    public UserInfo addUserInfo(UserInfo userInfo) throws UserExistException {
        //check if User exists
        if (isUserExists(userInfo.getUserId())) {
            throw new UserExistException();
        } else {
            assert userInfo.getUserId() != null;
            userMapper.insertUserInfo(userInfo);
            stringRedisTemplate.opsForSet().add(USER_ID_REDIS_KEY, userInfo.getUserId());
        }
        return userInfo;
    }

    private Boolean isUserExists(String userId) {
        return stringRedisTemplate.opsForSet().isMember(USER_ID_REDIS_KEY, userId);
    }

    public void loadUserIdFromDB() {
        List<String> userIdList = userMapper.selectAllUserId();
        if (!userIdList.isEmpty()) {
            stringRedisTemplate.opsForSet().add(USER_ID_REDIS_KEY, userIdList.toArray(new String[0]));
        }
    }

    public String changePassword(String userId, String newPasswordKeyId, String newPassword, String oldPassword) throws AuthException, ResponseException {
        UserInfo userInfo = userMapper.selectSingleUserInfo(userId);
        if (userInfo == null) {
            throw new UserNotExistException("user not exist");
        }
        Boolean res = authService.verifyPassword(userInfo.getPasswordKeyId(), userInfo.getPassword(), newPasswordKeyId, oldPassword);
        if (res != null && res) {
            //persistent Key id
            if (authService.verifyPassword(newPasswordKeyId, newPassword, newPasswordKeyId, oldPassword)) {
                throw new AuthException("old and new is the same");
            }
            String keyId = cipherService.persistentKeyPair(newPasswordKeyId);
            //update userInfo
            if (keyId != null) {
                //delete old key
                Boolean isDeleted = cipherService.deleteKeyPair(userInfo.getPasswordKeyId());
                if (isDeleted == null || !isDeleted) {
                    log.warn("key id {} delete failed", userInfo.getPasswordKeyId());
                }
                userMapper.updateUserPassword(userId, keyId, newPassword);
                return keyId;
            } else {
                throw new AuthException("persistentKey failed");
            }

        } else {
            throw new PasswordNotMatchException("PasswordNotMatchException");
        }
    }
}
