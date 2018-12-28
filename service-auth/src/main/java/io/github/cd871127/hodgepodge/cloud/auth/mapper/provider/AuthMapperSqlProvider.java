package io.github.cd871127.hodgepodge.cloud.auth.mapper.provider;

import io.github.cd871127.hodgepodge.cloud.lib.user.UserInfo;
import org.apache.ibatis.jdbc.SQL;

public class AuthMapperSqlProvider {
    String updateUserInfo(UserInfo userInfo) {
        return new SQL().UPDATE("").SET("a").toString();
    }
}
