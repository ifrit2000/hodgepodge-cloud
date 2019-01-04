package io.github.cd871127.hodgepodge.cloud.cipher.service.impl;

import io.github.cd871127.hodgepodge.cloud.cipher.algorithm.AsymmetricCipher;
import io.github.cd871127.hodgepodge.cloud.cipher.algorithm.CipherAlgorithm;
import io.github.cd871127.hodgepodge.cloud.cipher.algorithm.CipherDataEntity;
import io.github.cd871127.hodgepodge.cloud.cipher.algorithm.keypair.CipherKeyPair;
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


    private CipherKeyPair getRsaKeyPair(String keyId, Long expire) throws NoSuchAlgorithmException, KeyIdExpiredException {
        CipherKeyPair cipherKeyPair;
        if (StringUtils.isEmpty(keyId)) {
            //empty keyId
            cipherKeyPair = rsaCipher.getBase64KeyPair();
            cipherKeyPair.setCipherAlgorithm(CipherAlgorithm.RSA);
            //TODO change keyId generate method
            keyId = UUID.randomUUID().toString().replaceAll("-", "");
            cipherKeyPair.setKeyId(keyId);
            if (0 == expire) {
                cipherKeyService.insertCipherKeyPair(cipherKeyPair);
            } else {
                cipherKeyService.cacheCipherKeyPair(cipherKeyPair, expire);
            }
        } else {
            if (cipherKeyService.isKeyIdExists(keyId, CipherAlgorithm.RSA)) {
//                rsaKeyPair = (RsaKeyPair) cipherKeyService.selectKeyPairByKeyId(keyId);
                cipherKeyPair = new RsaKeyPair(cipherKeyService.selectKeyPairByKeyId(keyId));
            } else {
                cipherKeyPair = cipherKeyService.restoreCipherKeyPair(keyId);
                if (cipherKeyPair == null) {
                    //key died
                    throw new KeyIdExpiredException();
                }
            }
        }
        return cipherKeyPair;
    }

    private CipherKeyPair getRsaKeyPair(String keyId) throws KeyIdExpiredException, NoSuchAlgorithmException {
        return getRsaKeyPair(keyId, 0L);
    }

    @Override
    public Map<String, String> getPublicKey(String keyId, Long expire) throws NoSuchAlgorithmException, KeyIdExpiredException {
        CipherKeyPair rsaKeyPair = getRsaKeyPair(keyId, expire);
        Map<String, String> res = new HashMap<>();
        res.put("keyId", rsaKeyPair.getKeyId());
        res.put("publicKey", rsaKeyPair.getPublicKey());
        return res;
    }

    @Override
    public byte[] encode(CipherDataEntity dataEntity) throws KeyIdExpiredException, NoSuchAlgorithmException {
        CipherKeyPair rsaKeyPair = getRsaKeyPair(dataEntity.getKeyId());
        return rsaCipher.encode(dataEntity.getBytes(), rsaCipher.base64StringPublicKey(rsaKeyPair.getPublicKey()));

    }

    @Override
    public byte[] decode(CipherDataEntity dataEntity) throws KeyIdExpiredException, NoSuchAlgorithmException {
        CipherKeyPair rsaKeyPair = getRsaKeyPair(dataEntity.getKeyId());
        PrivateKey privateKey = rsaCipher.base64StringToPrivateKey(rsaKeyPair.getPrivateKey());
        return rsaCipher.decode(dataEntity.getBytes(), privateKey);
    }

    public boolean compare(CipherDataEntity dataEntity1, CipherDataEntity dataEntity2) throws KeyIdExpiredException, NoSuchAlgorithmException {
        return Arrays.equals(decode(dataEntity1), decode(dataEntity2));
    }

}
