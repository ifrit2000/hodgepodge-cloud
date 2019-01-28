package io.github.cd871127.hodgepodge.cloud.auth.mapper.provider;

import io.github.cd871127.hodgepodge.cloud.lib.user.UserInfo;
import org.apache.ibatis.jdbc.SQL;

public class AuthMapperSqlProvider {


    public String updateUserInfo(UserInfo userInfo) {
        return new SQL() {{
            UPDATE("USER_INFO");
            SET("USER_ID=#{userId}", "USERNAME=#{username}");
            WHERE("1=1", "2=2");
        }}.toString();
    }
}
