package io.github.cd871127.hodgepodge.cloud.cipher.crypto;

import io.github.cd871127.hodgepodge.cloud.cipher.configure.properties.CipherConfig;

import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class RsaCipher extends AsymmetricCipher {

    public RsaCipher(CipherConfig cipherConfig) {
        super(cipherConfig);
    }

    protected Key base64StringToKey(KeySpec keySpec) {
        Key key;
        try {
            KeyFactory keyFactory = KeyFactory.getInstance(getKeyAlgorithm());
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
}
