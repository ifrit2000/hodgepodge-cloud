package io.github.cd871127.hodgepodge.cloud.cipher.algorithm;

import io.github.cd871127.hodgepodge.cloud.cipher.algorithm.keypair.CipherKeyPair;
import io.github.cd871127.hodgepodge.cloud.cipher.algorithm.keypair.RsaKeyPair;
import io.github.cd871127.hodgepodge.cloud.cipher.configure.properties.CipherConfig;
import lombok.Data;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.*;
import java.security.spec.KeySpec;
import java.util.Base64;

@Data
public abstract class AsymmetricCipher {

    public AsymmetricCipher() {
    }

    public AsymmetricCipher(CipherConfig cipherConfig) {
        this();
        init(cipherConfig);
    }

    private Integer keyLength;
    private String keyAlgorithm;
    private String algorithm;

    public void init(CipherConfig cipherConfig) {
        setAlgorithm(cipherConfig.getAlgorithm());
        setKeyAlgorithm(cipherConfig.getKeyAlgorithm());
        setKeyLength(cipherConfig.getKeyLength());
    }

    public byte[] decode(byte[] bytes, PrivateKey privateKey) {
        return handle(bytes, privateKey);
    }

    public byte[] encode(byte[] bytes, PublicKey publicKey) {
        return handle(bytes, publicKey);
    }

    private byte[] handle(byte[] bytes, Key key) {
        try {
            Cipher cipher = Cipher.getInstance(algorithm);
            if (key instanceof PublicKey) {
                cipher.init(Cipher.ENCRYPT_MODE, key);
            } else if (key instanceof PrivateKey) {
                cipher.init(Cipher.DECRYPT_MODE, key);
            }
            return cipher.doFinal(bytes);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | IllegalBlockSizeException | BadPaddingException | InvalidKeyException e) {
            e.printStackTrace();
            return null;
        }
    }

    protected abstract Key base64StringToKey(KeySpec keySpec);

    public abstract PublicKey base64StringPublicKey(String publicKeyStr);

    public abstract PrivateKey base64StringToPrivateKey(String privateKeyStr);

    public String keyToBase64String(Key key) {
        return Base64.getEncoder().encodeToString(key.getEncoded());
    }

    public CipherKeyPair getBase64KeyPair() throws NoSuchAlgorithmException {
        KeyPair keyPair = getKeyPair();
        String publicKey = keyToBase64String(keyPair.getPublic());
        String privateKey = keyToBase64String(keyPair.getPrivate());
        return new CipherKeyPair(publicKey, privateKey);
    }

    private KeyPair getKeyPair() throws NoSuchAlgorithmException {
        KeyPairGenerator keyPairGenerator;
        keyPairGenerator = KeyPairGenerator.getInstance(keyAlgorithm);
        keyPairGenerator.initialize(keyLength);
        return keyPairGenerator.generateKeyPair();
    }
}
