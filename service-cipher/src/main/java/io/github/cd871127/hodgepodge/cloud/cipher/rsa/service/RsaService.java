package io.github.cd871127.hodgepodge.cloud.cipher.rsa.service;

import io.github.cd871127.hodgepodge.cloud.cipher.crypto.RsaEncipher;
import io.github.cd871127.hodgepodge.cloud.cipher.crypto.RsaKeyPair;
import io.github.cd871127.hodgepodge.cloud.cipher.exception.InvalidKeyIdException;
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

    public Map<String, String> getPublicKey(String keyId) throws NoSuchAlgorithmException, InvalidKeyIdException {
        RsaKeyPair rsakeyPair;
        if (keyId == null) {
            rsakeyPair = rsaEncipher.getStringKeyPair();
            keyId = UUID.randomUUID().toString().replaceAll("-", "");
            redisTemplate.opsForValue().set(keyId, rsakeyPair, 3, TimeUnit.MINUTES);
        } else {
            rsakeyPair = redisTemplate.opsForValue().get(keyId);
            if (rsakeyPair == null) {
                throw new InvalidKeyIdException("Invalid RSA keyId");
            }
        }
        Map<String, String> res = new HashMap<>();
        res.put("keyId", keyId);
        res.put("publicKey", rsakeyPair.getPublicKey());
        return res;
    }

}
