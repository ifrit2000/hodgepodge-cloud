package com.github.cd871127.hodgepodge.cloud.lib.web.rest;

import lombok.Data;

@Data
public class ServerResponse<T> {
    private T data;
    private String code;
    private String message;

    public ServerResponse() {
    }

    public ServerResponse(ResponseInfo responseInfo) {
        setResponseInfo(responseInfo);
    }

    public void setResponseInfo(ResponseInfo responseInfo) {
        setCode(responseInfo.getCode());
        setMessage(responseInfo.getMessage());
    }
}
