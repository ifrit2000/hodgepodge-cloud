package io.github.cd871127.hodgepodge.cloud.cipher.util.response;

import io.github.cd871127.hodgepodge.cloud.lib.web.server.response.HodgepodgeResponse;

public enum CipherResponse implements HodgepodgeResponse {

    RSA_NOT_EXISTS("000101", "获取RSA公钥失败"),
    INVALID_KEY_ID("000102", "INVALID_KEY_ID"),
    KEY_ID_EXPIRED("000103", "KEY_ID_EXPIRED");

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
