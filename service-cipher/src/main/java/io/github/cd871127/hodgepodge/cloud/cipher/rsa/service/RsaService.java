package io.github.cd871127.hodgepodge.cloud.cipher.rsa.service;

import io.github.cd871127.hodgepodge.cloud.cipher.crypto.RsaEncipher;
import io.github.cd871127.hodgepodge.cloud.cipher.crypto.RsaKeyPair;
import io.github.cd871127.hodgepodge.cloud.cipher.exception.InvalidKeyIdException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class RsaService {

    @Resource
    private RsaEncipher rsaEncipher;

    @Resource
    private RedisTemplate<String, RsaKeyPair> redisTemplate;

    private RsaKeyPair getRsaKeyPair(String keyId) throws NoSuchAlgorithmException, InvalidKeyIdException {
        RsaKeyPair rsaKeyPair;
        if (StringUtils.isEmpty(keyId)) {
            rsaKeyPair = rsaEncipher.getStringKeyPair();
            keyId = UUID.randomUUID().toString().replaceAll("-", "");
            rsaKeyPair.setKeyId(keyId);
            redisTemplate.opsForValue().set(keyId, rsaKeyPair, 3, TimeUnit.MINUTES);
        } else {
            rsaKeyPair = redisTemplate.opsForValue().get(keyId);
            if (rsaKeyPair == null) {
                throw new InvalidKeyIdException("Invalid RSA keyId");
            }
        }
        return rsaKeyPair;
    }

    public Map<String, String> getPublicKey(String keyId) throws NoSuchAlgorithmException, InvalidKeyIdException {
        RsaKeyPair rsaKeyPair = getRsaKeyPair(keyId);
        Map<String, String> res = new HashMap<>();
        res.put("keyId", rsaKeyPair.getKeyId());
        res.put("publicKey", rsaKeyPair.getPublicKey());
        return res;
    }

    public byte[] encode(String keyId, byte[] data) throws InvalidKeyIdException, NoSuchAlgorithmException {
        RsaKeyPair rsaKeyPair = getRsaKeyPair(keyId);
        return rsaEncipher.encode(data, rsaEncipher.stringToPublicKey(rsaKeyPair.getPublicKey()));

    }

    public byte[] decode(String keyId, byte[] data) throws InvalidKeyIdException, NoSuchAlgorithmException {
        RsaKeyPair rsaKeyPair = getRsaKeyPair(keyId);
        return rsaEncipher.decode(data, rsaEncipher.stringToPrivateKey(rsaKeyPair.getPrivateKey()));
    }

}
