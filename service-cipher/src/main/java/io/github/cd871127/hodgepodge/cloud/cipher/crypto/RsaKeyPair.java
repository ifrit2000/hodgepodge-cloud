package io.github.cd871127.hodgepodge.cloud.cipher.crypto;

import io.github.cd871127.hodgepodge.cloud.lib.util.Pair;

public class RsaKeyPair {

    private Pair<String, String> pair = new Pair<>();

    private String keyId;

    public RsaKeyPair() {
    }

    public RsaKeyPair(String publicKey, String privateKey) {
        setPublicKey(publicKey);
        setPrivateKey(privateKey);
    }

    public String getPublicKey() {
        return pair.getKey();
    }

    public void setPublicKey(String publicKey) {
        pair.setKey(publicKey);
    }

    public String getPrivateKey() {
        return pair.getValue();
    }

    public void setPrivateKey(String privateKey) {
        pair.setValue(privateKey);
    }

    public String getKeyId() {
        return keyId;
    }

    public void setKeyId(String keyId) {
        this.keyId = keyId;
    }
}
