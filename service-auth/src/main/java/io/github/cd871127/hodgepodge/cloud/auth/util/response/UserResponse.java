package io.github.cd871127.hodgepodge.cloud.auth.util.response;

import io.github.cd871127.hodgepodge.cloud.lib.web.server.response.HodgepodgeResponse;

public enum UserResponse implements HodgepodgeResponse {

    USER_ERROR("000201","nonono"),
    USER_EXIST("000202", "USER_EXIST");

    private String code;
    private String message;

    UserResponse(String code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public String getCode() {
        return this.code;
    }

    @Override
    public String getMessage() {
        return this.message;
    }
}
