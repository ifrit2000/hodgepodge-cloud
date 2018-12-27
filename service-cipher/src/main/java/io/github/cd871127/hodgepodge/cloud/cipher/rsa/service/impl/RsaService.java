package io.github.cd871127.hodgepodge.cloud.cipher.rsa.service.impl;

import io.github.cd871127.hodgepodge.cloud.cipher.crypto.RsaEncipher;
import io.github.cd871127.hodgepodge.cloud.cipher.crypto.keypair.RsaKeyPair;
import io.github.cd871127.hodgepodge.cloud.cipher.exception.InvalidKeyIdException;
import io.github.cd871127.hodgepodge.cloud.cipher.rsa.mapper.RsaMapper;
import io.github.cd871127.hodgepodge.cloud.cipher.rsa.service.CipherService;
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
public class RsaService implements CipherService {

    @Resource
    private RsaEncipher rsaEncipher;

    @Resource
    private RedisTemplate<String, RsaKeyPair> redisTemplate;

    @Resource
    private RsaMapper rsaMapper;

    private RsaKeyPair getRsaKeyPair(String keyId, Long expire) throws NoSuchAlgorithmException, InvalidKeyIdException {
        RsaKeyPair rsaKeyPair;
        if (StringUtils.isEmpty(keyId)) {
            rsaKeyPair = rsaEncipher.getBase64KeyPair();
            keyId = UUID.randomUUID().toString().replaceAll("-", "");
            rsaKeyPair.setKeyId(keyId);
            redisTemplate.opsForValue().set(keyId, rsaKeyPair, expire, TimeUnit.SECONDS);
        } else {
            rsaKeyPair = redisTemplate.opsForValue().get(keyId);
            if (rsaKeyPair == null) {
                throw new InvalidKeyIdException("Invalid RSA keyId");
            }
        }
        System.out.println(keyId.length());
        System.out.println(rsaKeyPair.getPublicKey().length());
        System.out.println(rsaKeyPair.getPrivateKey().length());
        if (0 == expire) {
            rsaMapper.insertRsaKeyPair(rsaKeyPair);
        }
        return rsaKeyPair;
    }

    private RsaKeyPair getRsaKeyPair(String keyId) throws InvalidKeyIdException, NoSuchAlgorithmException {
        return getRsaKeyPair(keyId, 0L);
    }

    public Map<String, String> getPublicKey(String keyId, Long expire) throws NoSuchAlgorithmException, InvalidKeyIdException {
        RsaKeyPair rsaKeyPair = getRsaKeyPair(keyId, expire);
        Map<String, String> res = new HashMap<>();
        res.put("keyId", rsaKeyPair.getKeyId());
        res.put("publicKey", rsaKeyPair.getPublicKey());
        return res;
    }

    public byte[] encode(String keyId, byte[] data) throws InvalidKeyIdException, NoSuchAlgorithmException {
        RsaKeyPair rsaKeyPair = getRsaKeyPair(keyId);
        return rsaEncipher.encode(data, rsaEncipher.base64StringPublicKey(rsaKeyPair.getPublicKey()));

    }

    public byte[] decode(String keyId, byte[] data) throws InvalidKeyIdException, NoSuchAlgorithmException {
        RsaKeyPair rsaKeyPair = getRsaKeyPair(keyId);
        return rsaEncipher.decode(data, rsaEncipher.base64StringToPrivateKey(rsaKeyPair.getPrivateKey()));
    }

}
