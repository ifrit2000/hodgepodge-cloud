package io.github.cd871127.hodgepodge.cloud.cipher.algorithm;

import lombok.Data;

@Data
public class CryptoObject<T> {
    private String keyId;
    private T data;
    private DataConvert<T> dataConvert;

    public CryptoObject(DataConvert<T> dataConvert) {
        setDataConvert(dataConvert);
    }

    public byte[] data2Bytes() {
        return dataConvert.rawTypeToBytes(this.data);
    }

    public void bytes2Data(byte[] bytes) {
        setData(dataConvert.bytesToRawType(bytes));
    }
}
