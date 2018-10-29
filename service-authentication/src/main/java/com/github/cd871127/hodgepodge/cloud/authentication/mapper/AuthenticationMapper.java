package com.github.cd871127.hodgepodge.cloud.authentication.mapper;

import com.github.cd871127.hodgepodge.cloud.lib.user.UserInfo;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AuthenticationMapper {

    @Insert("insert into USER_INFO_TBL(USER_ID,PASSWORD,PHONE,E_MAIL) VALUES(#{userId},PASSWORD(#{password}),#{phone},#{eMail})")
    int register(UserInfo userInfo);

    UserInfo queryUserInfoByUserIdAndPassword(String userId, String password);
}
