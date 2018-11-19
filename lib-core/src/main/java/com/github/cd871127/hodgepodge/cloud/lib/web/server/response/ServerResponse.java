package com.github.cd871127.hodgepodge.cloud.lib.web.server.response;

import lombok.Data;

@Data
public class ServerResponse<T> {
    private T data;
    private String code;
    private String message;

    public ServerResponse() {
    }

    public ServerResponse(HodgepodgeResponse hodgepodgeResponse) {
        this();
        setHodgepodgeResponse(hodgepodgeResponse);
    }

    public void setHodgepodgeResponse(HodgepodgeResponse hodgepodgeResponse) {
        setCode(hodgepodgeResponse.getCode());
        setMessage(hodgepodgeResponse.getMessage());
    }

}
