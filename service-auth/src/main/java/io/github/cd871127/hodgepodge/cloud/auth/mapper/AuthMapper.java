package io.github.cd871127.hodgepodge.cloud.auth.mapper;

import io.github.cd871127.hodgepodge.cloud.auth.mapper.provider.AuthMapperSqlProvider;
import io.github.cd871127.hodgepodge.cloud.lib.user.UserInfo;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.UpdateProvider;

@Mapper
public interface AuthMapper {

    @Insert("insert into USER_INFO(USER_ID, USERNAME, PASSWORD, PASSWORD_KEY_ID, PHONE, E_MAIL) " +
            "VALUES (#{userId},#{username},#{password},#{passwordKeyId},#{phone},#{eMail})")
    int insertUserInfo(UserInfo userInfo);

    @UpdateProvider(type = AuthMapperSqlProvider.class, method = "updateUserInfo")
    int updateUserInfo(UserInfo userInfo);

}
