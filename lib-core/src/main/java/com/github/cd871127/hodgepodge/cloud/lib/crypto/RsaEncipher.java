package com.github.cd871127.hodgepodge.cloud.lib.crypto;

import com.github.cd871127.hodgepodge.cloud.lib.util.Pair;

import java.security.*;
import java.security.spec.KeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class RsaEncipher implements Encipher {

    private static final int KEY_LENGTH = 2048;

    private static final String KEY_ALGORITHM = "RSA";


    public Pair<String, String> keyPair() {
        KeyPairGenerator keyPairGenerator;
        try {
            keyPairGenerator = KeyPairGenerator.getInstance(KEY_ALGORITHM);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
        keyPairGenerator.initialize(KEY_LENGTH);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        String publicKey = Base64.getEncoder().encodeToString(keyPair.getPublic().getEncoded());
        String privateKey = Base64.getEncoder().encodeToString(keyPair.getPrivate().getEncoded());
        return new Pair<>(publicKey, privateKey);
    }

    private Key stringToKey(String keyStr, KeySpec keySpec) {

    }

    public PublicKey publicKey(String publicKeyStr) {
        byte[] keyBytes;
        keyBytes =;

        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PublicKey publicKey = keyFactory.generatePublic(keySpec);
        return publicKey;


    }

    @Override
    public byte[] encode(byte[] bytes, PrivateKey privateKey) {
        return new byte[0];
    }

    @Override
    public byte[] decode(byte[] bytes, PublicKey publicKey) {
        return new byte[0];
    }

    public static void main(String[] args) {
        RsaEncipher rsaEncipher = new RsaEncipher();
        Pair<String, String> pair = rsaEncipher.keyPair();
        String test = "123";
        byte[] encoded = rsaEncipher.encode(test.getBytes(), pair.getKey());
    }
}
