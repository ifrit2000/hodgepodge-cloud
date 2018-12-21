package io.github.cd871127.hodgepodge.cloud.cipher.crypto;

import io.github.cd871127.hodgepodge.cloud.lib.util.ByteArrayConversion;

public class CryptoString extends CryptoObject<String> {
    public CryptoString() {
        super(new DataConvert<String>() {
            @Override
            public byte[] rawTypeToBytes(String data) {
                return ByteArrayConversion.hexString2ByteArray(data);
            }

            @Override
            public String bytesToRawType(byte[] bytes) {
                return ByteArrayConversion.byteArray2HexString(bytes);
            }
        });
    }
}
