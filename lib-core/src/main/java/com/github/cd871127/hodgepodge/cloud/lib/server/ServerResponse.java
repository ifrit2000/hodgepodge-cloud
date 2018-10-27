package com.github.cd871127.hodgepodge.cloud.lib.server;

import lombok.Data;

@Data
public class ServerResponse<T> {
    private T data;
    private String code;
    private String message;

    public ServerResponse() {
    }

    public ServerResponse(ResponseInfo responseInfo) {
        this();
        setCode(responseInfo.getCode());
        setMessage(responseInfo.getMessage());
    }

}
