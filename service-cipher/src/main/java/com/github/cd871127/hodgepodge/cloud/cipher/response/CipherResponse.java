package com.github.cd871127.hodgepodge.cloud.cipher.response;

import com.github.cd871127.hodgepodge.cloud.lib.web.server.response.HodgepodgeResponse;

public enum CipherResponse implements HodgepodgeResponse {

    GET_RSA_PUBLIC_KEY_FAILED("000101", "获取RSA公钥失败");

    private String code;
    private String message;

    CipherResponse(String code, String message) {
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
