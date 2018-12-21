package io.github.cd871127.hodgepodge.cloud.authentication.mapper;

import io.github.cd871127.hodgepodge.cloud.lib.user.UserInfo;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface AuthenticationMapper {

    @Insert("insert into USER_INFO_TBL(USERNAME,PASSWORD,NICKNAME,PHONE,E_MAIL) VALUES(#{username},PASSWORD(#{password}),#{nickname},#{phone},#{eMail})")
    void register(UserInfo userInfo);

    @Select("select count(1) from USER_INFO_TBL where username=#{username}")
    Boolean isUserExist(@Param("username") String username);

    @Select("select count(1) from USER_INFO_TBL where username=#{username}")
    UserInfo queryUserInfo(String username, String password);
}
