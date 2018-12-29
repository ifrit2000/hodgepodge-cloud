package io.github.cd871127.hodgepodge.cloud.auth.service;

import io.github.cd871127.hodgepodge.cloud.auth.exception.UserExistException;
import io.github.cd871127.hodgepodge.cloud.auth.mapper.UserMapper;
import io.github.cd871127.hodgepodge.cloud.lib.user.UserInfo;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class UserService {
    @Resource
    private UserMapper userMapper;

    @Resource
    private RedisTemplate<String, String> redisTemplate;

    private final String USER_ID_REDIS_KEY = "userIdSet";

    public UserInfo addUserInfo(UserInfo userInfo) throws UserExistException {
        //check if User exists
        if (isUserExists(userInfo.getUserId())) {
            throw new UserExistException();
        } else {
            assert userInfo.getUserId() != null;
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
}
