package io.github.cd871127.hodgepodge.cloud.cipher.algorithm.keypair;

import io.github.cd871127.hodgepodge.cloud.cipher.algorithm.CipherAlgorithm;

public class RsaKeyPair extends CipherKeyPair {

    public RsaKeyPair() {
        setCipherAlgorithm(CipherAlgorithm.RSA);
    }

    public RsaKeyPair(CipherKeyPair cipherKeyPair) {
        setKeyId(cipherKeyPair.getKeyId());
        setPrivateKey(cipherKeyPair.getPrivateKey());
        setPublicKey(cipherKeyPair.getPublicKey());
        setCipherAlgorithm(CipherAlgorithm.RSA);
    }

    public RsaKeyPair(String publicKey, String privateKey) {
        super(publicKey, privateKey);
        setCipherAlgorithm(CipherAlgorithm.RSA);
    }


}
