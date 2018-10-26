package com.github.cd871127.hodgepodge.cloud.lib.web.rest;

import lombok.Data;


public enum ResponseInfo {
    SUCCESS("000000", "成功");
    private String code;
    private String message;

    ResponseInfo(String code, String message) {
        this.code=code;
        this.message=message;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
