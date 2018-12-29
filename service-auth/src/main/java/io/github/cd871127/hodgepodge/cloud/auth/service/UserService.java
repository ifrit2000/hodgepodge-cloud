package io.github.cd871127.hodgepodge.cloud.auth.service;

import io.github.cd871127.hodgepodge.cloud.auth.exception.UserExistException;
import io.github.cd871127.hodgepodge.cloud.auth.mapper.UserMapper;
import io.github.cd871127.hodgepodge.cloud.lib.user.UserInfo;
import io.github.cd871127.hodgepodge.cloud.lib.util.ResponseException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service
public class UserService {
    @Resource
    private UserMapper userMapper;
    @Resource
    private AuthService authService;
    @Resource
    private CipherService cipherService;

    @Resource
    private RedisTemplate<String, String> redisTemplate;

    private final String USER_ID_REDIS_KEY = "userIdSet";

    public UserInfo addUserInfo(UserInfo userInfo) throws UserExistException, ResponseException {
        //check if User exists
        if (isUserExists(userInfo.getUserId())) {
            throw new UserExistException();
        } else {
            assert userInfo.getUserId() != null;
            Map<String, String> publicKeyMap = cipherService.publicKey(0L);
            userInfo.setPasswordKeyId(publicKeyMap.get("keyId"));
            userMapper.insertUserInfo(userInfo);
            redisTemplate.opsForSet().add(USER_ID_REDIS_KEY, userInfo.getUserId());
        }
        return userInfo;
    }

    public Boolean isUserExists(String userId) {
        return redisTemplate.opsForSet().isMember(USER_ID_REDIS_KEY, userId);
    }

    public void loadUserIdFromDB() {
        List<String> userIdList = userMapper.selectAllUserId();
        redisTemplate.opsForSet().add(USER_ID_REDIS_KEY, userIdList.toArray(new String[0]));
    }

    public void changePassword(String userId, String newPassword, String oldPassword) throws ResponseException {
//        if (authService.verifyPassword(userId, oldPassword)) {
//
//        }
    }
}
