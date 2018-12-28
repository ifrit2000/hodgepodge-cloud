package io.github.cd871127.hodgepodge.cloud.auth.service;

import io.github.cd871127.hodgepodge.cloud.auth.mapper.AuthMapper;
import io.github.cd871127.hodgepodge.cloud.lib.user.UserInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class AuthService {
    @Resource
    private AuthMapper authMapper;

    public UserInfo login(UserInfo userInfo) {
        return userInfo;
    }

    public UserInfo register(UserInfo userInfo) {
        return userInfo;
    }
}
