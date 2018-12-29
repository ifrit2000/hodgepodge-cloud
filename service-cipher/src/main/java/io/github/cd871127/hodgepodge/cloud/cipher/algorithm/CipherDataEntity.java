package io.github.cd871127.hodgepodge.cloud.cipher.algorithm;

import lombok.Data;

import java.util.Base64;

@Data
public class CipherDataEntity {

    private String keyId;
    /**
     * base64 String
     */
    private String data;

    public byte[] getBytes() {
        return Base64.getDecoder().decode(this.data);
    }

    public void setBytes(byte[] bytes) {
        setData(Base64.getEncoder().encodeToString(bytes));
    }

}
