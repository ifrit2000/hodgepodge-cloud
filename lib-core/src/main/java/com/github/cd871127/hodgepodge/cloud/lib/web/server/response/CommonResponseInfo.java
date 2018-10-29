package com.github.cd871127.hodgepodge.cloud.lib.web.server.response;

public enum CommonResponseInfo implements ResponseInfo {
    SUCCESSFUL("000000", "成功"),

    FAILED("999999", "失败");

    private String code;
    private String message;

    CommonResponseInfo(String code, String message) {
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
