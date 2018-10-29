package com.github.cd871127.hodgepodge.cloud.authentication.mapper;

import com.github.cd871127.hodgepodge.cloud.lib.user.UserInfo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AuthenticationMapper {
    int register(UserInfo userInfo);
}
