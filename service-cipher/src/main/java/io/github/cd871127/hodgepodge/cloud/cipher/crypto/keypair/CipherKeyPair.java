package io.github.cd871127.hodgepodge.cloud.cipher.crypto.keypair;

public interface CipherKeyPair<K, V> {

    String getKeyId();

    K getPublicKey();

    V getPrivateKey();

    void setKeyId(String keyId);

    void setPublicKey(K publicKey);

    void setPrivateKey(V privateKey);
}
