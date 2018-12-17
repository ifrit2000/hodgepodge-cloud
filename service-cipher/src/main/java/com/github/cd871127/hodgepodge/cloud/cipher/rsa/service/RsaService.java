package com.github.cd871127.hodgepodge.cloud.cipher.rsa.service;

import com.github.cd871127.hodgepodge.cloud.cipher.crypto.RsaEncipher;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.security.KeyPair;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class RsaService {

    @Resource
    private RsaEncipher rsaEncipher;

    @Resource
    private RedisTemplate<String, KeyPair> redisTemplate;

    public Map<String, String> getPublicKey(String keyId) {
        KeyPair keyPair;
        if (keyId == null) {
            keyPair = rsaEncipher.getKeyPair();
            keyId = UUID.randomUUID().toString().replaceAll("-", "");
            redisTemplate.opsForValue()
                    .set(keyId, keyPair, 3, TimeUnit.MINUTES);
        } else {
            keyPair = redisTemplate.opsForValue().get(keyId);
        }
        if (keyPair == null) {
            return null;
        }
        Map<String, String> res = new HashMap<>();
        res.put("keyId", keyId);
        res.put("publicKey", rsaEncipher.keyToString(keyPair.getPublic()));
        return res;
    }

}
