package io.github.cd871127.hodgepodge.cloud.cipher.crypto;

import io.github.cd871127.hodgepodge.cloud.cipher.crypto.keypair.RsaKeyPair;
import org.springframework.stereotype.Component;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

@Component
public class RsaEncipher implements Encipher {

    private static final int KEY_LENGTH = 2048;

    private static final String KEY_ALGORITHM = "RSA";

    private static final String ALGORITHM = "RSA/ECB/PKCS1Padding";

    private KeyPair getKeyPair() throws NoSuchAlgorithmException {
        KeyPairGenerator keyPairGenerator;
        keyPairGenerator = KeyPairGenerator.getInstance(KEY_ALGORITHM);
        keyPairGenerator.initialize(KEY_LENGTH);
        return keyPairGenerator.generateKeyPair();
    }

    public RsaKeyPair getBase64KeyPair() throws NoSuchAlgorithmException {
        KeyPair keyPair = getKeyPair();
        String publicKey = keyToBase64String(keyPair.getPublic());
        String privateKey = keyToBase64String(keyPair.getPrivate());
        return new RsaKeyPair(publicKey, privateKey);
    }

    public String keyToBase64String(Key key) {
        return Base64.getEncoder().encodeToString(key.getEncoded());
    }

    /**
     * convert key string to key
     *
     * @param keySpec
     * @return
     */
    private Key base64StringToKey(KeySpec keySpec) {
        Key key;
        try {
            KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
            if (keySpec instanceof X509EncodedKeySpec) {
                key = keyFactory.generatePublic(keySpec);
            } else if (keySpec instanceof PKCS8EncodedKeySpec) {
                key = keyFactory.generatePrivate(keySpec);
            } else {
                key = null;
            }
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            e.printStackTrace();
            return null;
        }
        return key;
    }

    public PublicKey base64StringPublicKey(String publicKeyStr) {
        return (PublicKey) base64StringToKey(new X509EncodedKeySpec(Base64.getDecoder().decode(publicKeyStr)));
    }

    public PrivateKey base64StringToPrivateKey(String privateKeyStr) {
        return (PrivateKey) base64StringToKey(new PKCS8EncodedKeySpec(Base64.getDecoder().decode(privateKeyStr)));
    }

    @Override
    public byte[] decode(byte[] bytes, PrivateKey privateKey) {
        return handle(bytes, privateKey);
    }

    @Override
    public byte[] encode(byte[] bytes, PublicKey publicKey) {
        return handle(bytes, publicKey);
    }

    private byte[] handle(byte[] bytes, Key key) {
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM);
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

}
