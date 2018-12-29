package io.github.cd871127.hodgepodge.cloud.auth.mapper;

import io.github.cd871127.hodgepodge.cloud.lib.user.UserInfo;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface UserMapper {

    @Insert("insert into USER_INFO(USER_ID, USERNAME, PASSWORD, PASSWORD_KEY_ID, PHONE, E_MAIL) " +
            "VALUES (#{userId},#{username},#{password},#{passwordKeyId},#{phone},#{eMail})")
    int insertUserInfo(UserInfo userInfo);

    @Select("select user_id from USER_INFO")
    List<String> selectAllUserId();

    @Select("select USER_ID, USERNAME, PASSWORD, PASSWORD_KEY_ID, PHONE, E_MAIL from USER_INFO where user_id=#{userId}")
    UserInfo selectSingleUserInfo(String userId);
//    @UpdateProvider(type = AuthMapperSqlProvider.class, method = "updateUserInfo")
//    int updateUserInfo(UserInfo userInfo);

}
