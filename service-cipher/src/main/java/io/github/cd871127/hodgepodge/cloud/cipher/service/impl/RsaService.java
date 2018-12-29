package io.github.cd871127.hodgepodge.cloud.cipher.service.impl;

import io.github.cd871127.hodgepodge.cloud.cipher.algorithm.AsymmetricCipher;
import io.github.cd871127.hodgepodge.cloud.cipher.algorithm.CipherAlgorithm;
import io.github.cd871127.hodgepodge.cloud.cipher.algorithm.DataEntity;
import io.github.cd871127.hodgepodge.cloud.cipher.algorithm.keypair.RsaKeyPair;
import io.github.cd871127.hodgepodge.cloud.cipher.exception.KeyIdExpiredException;
import io.github.cd871127.hodgepodge.cloud.cipher.service.CipherKeyService;
import io.github.cd871127.hodgepodge.cloud.cipher.service.CipherService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class RsaService implements CipherService {

    @Resource
    private AsymmetricCipher rsaCipher;


    @Resource
    private CipherKeyService cipherKeyService;


    private RsaKeyPair getRsaKeyPair(String keyId, Long expire) throws NoSuchAlgorithmException, KeyIdExpiredException {
        RsaKeyPair rsaKeyPair;
        if (StringUtils.isEmpty(keyId)) {
            //empty keyId
            rsaKeyPair = rsaCipher.getBase64KeyPair();
            //TODO change keyId generate method
            keyId = UUID.randomUUID().toString().replaceAll("-", "");
            rsaKeyPair.setKeyId(keyId);
            if (0 == expire) {
                cipherKeyService.insertCipherKeyPair(rsaKeyPair);
            } else {
                cipherKeyService.cacheCipherKeyPair(rsaKeyPair, expire);
            }
        } else {
            if (cipherKeyService.isKeyIdExists(keyId, CipherAlgorithm.RSA)) {
                rsaKeyPair = (RsaKeyPair) cipherKeyService.selectKeyPairByKeyId(keyId);
            } else {
                rsaKeyPair = (RsaKeyPair) cipherKeyService.restoreCipherKeyPair(keyId);
                if (rsaKeyPair == null) {
                    //key died
                    throw new KeyIdExpiredException();
                }
            }
        }
        return rsaKeyPair;
    }

    private RsaKeyPair getRsaKeyPair(String keyId) throws KeyIdExpiredException, NoSuchAlgorithmException {
        return getRsaKeyPair(keyId, 0L);
    }

    public Map<String, String> getPublicKey(String keyId, Long expire) throws NoSuchAlgorithmException, KeyIdExpiredException {
        RsaKeyPair rsaKeyPair = getRsaKeyPair(keyId, expire);
        Map<String, String> res = new HashMap<>();
        res.put("keyId", rsaKeyPair.getKeyId());
        res.put("publicKey", rsaKeyPair.getPublicKey());
        return res;
    }

    public byte[] encode(DataEntity dataEntity) throws KeyIdExpiredException, NoSuchAlgorithmException {
        RsaKeyPair rsaKeyPair = getRsaKeyPair(dataEntity.getKeyId());
        return rsaCipher.encode(dataEntity.getBytes(), rsaCipher.base64StringPublicKey(rsaKeyPair.getPublicKey()));

    }

    public byte[] decode(DataEntity dataEntity) throws KeyIdExpiredException, NoSuchAlgorithmException {
        RsaKeyPair rsaKeyPair = getRsaKeyPair(dataEntity.getKeyId());
        PrivateKey privateKey = rsaCipher.base64StringToPrivateKey(rsaKeyPair.getPrivateKey());
        return rsaCipher.decode(dataEntity.getBytes(), privateKey);
    }

    public boolean compare(DataEntity dataEntity1, DataEntity dataEntity2) throws KeyIdExpiredException, NoSuchAlgorithmException {
        return Arrays.equals(decode(dataEntity1), decode(dataEntity2));
    }

}
