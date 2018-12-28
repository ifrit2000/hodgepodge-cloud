package io.github.cd871127.hodgepodge.cloud.auth.mapper;

import io.github.cd871127.hodgepodge.cloud.lib.user.UserInfo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AuthMapper {


    int insertUserInfo(UserInfo userInfo);

    int updateUserInfo(UserInfo userInfo);


}
