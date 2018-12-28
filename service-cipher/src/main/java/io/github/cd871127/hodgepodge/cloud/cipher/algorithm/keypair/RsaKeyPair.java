package io.github.cd871127.hodgepodge.cloud.cipher.algorithm.keypair;

import io.github.cd871127.hodgepodge.cloud.cipher.algorithm.CipherAlgorithm;
import io.github.cd871127.hodgepodge.cloud.lib.util.Pair;

/**
 *
 */
public class RsaKeyPair implements CipherKeyPair<String, String> {

    private Pair<String, String> pair = new Pair<>();

    private String keyId;

    public RsaKeyPair() {
    }

    public RsaKeyPair(String publicKey, String privateKey) {
        setPublicKey(publicKey);
        setPrivateKey(privateKey);
    }

    public CipherAlgorithm getCipherAlgorithm() {
        return cipherAlgorithm;
    }

    public void setCipherAlgorithm(CipherAlgorithm cipherAlgorithm) {
        this.cipherAlgorithm = cipherAlgorithm;
    }

    private CipherAlgorithm cipherAlgorithm = CipherAlgorithm.RSA;

    @Override
    public String getPublicKey() {
        return pair.getKey();
    }

    @Override
    public void setPublicKey(String publicKey) {
        pair.setKey(publicKey);
    }

    @Override
    public String getPrivateKey() {
        return pair.getValue();
    }

    @Override
    public void setPrivateKey(String privateKey) {
        pair.setValue(privateKey);
    }

    @Override
    public String getKeyId() {
        return keyId;
    }

    @Override
    public void setKeyId(String keyId) {
        this.keyId = keyId;
    }
}
