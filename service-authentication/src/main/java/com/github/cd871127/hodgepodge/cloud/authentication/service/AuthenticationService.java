package com.github.cd871127.hodgepodge.cloud.authentication.service;

import com.github.cd871127.hodgepodge.cloud.authentication.mapper.AuthenticationMapper;
import com.github.cd871127.hodgepodge.cloud.lib.user.UserInfo;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service
@Transactional(rollbackFor = Exception.class)
public class AuthenticationService {

    @Resource
    private AuthenticationMapper authenticationMapper;

    @Resource
    private RedisTemplate<String, String> redisTemplate;

    public int register(UserInfo userInfo) {
        //TODO verify if username exist and password valid
        return authenticationMapper.register(userInfo);
    }

    public UserInfo login(String userId, String password) {
        return null;
    }
}
