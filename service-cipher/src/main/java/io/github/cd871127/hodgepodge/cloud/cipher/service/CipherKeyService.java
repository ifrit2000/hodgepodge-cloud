package io.github.cd871127.hodgepodge.cloud.cipher.service;

import io.github.cd871127.hodgepodge.cloud.cipher.mapper.CipherKeyMapper;
import io.github.cd871127.hodgepodge.cloud.lib.cipher.CipherAlgorithm;
import io.github.cd871127.hodgepodge.cloud.lib.cipher.keypair.CipherKeyPair;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class CipherKeyService {
    @Resource
    private CipherKeyMapper cipherKeyMapper;

    @Resource
    private RedisTemplate redisTemplate;

    private RedisTemplate<String, CipherKeyPair> keyPairRedisTemplate;

    private RedisTemplate<String, String> stringRedisTemplate;

    @PostConstruct
    @SuppressWarnings("unchecked")
    private void init() {
        keyPairRedisTemplate = redisTemplate;
        stringRedisTemplate = redisTemplate;
    }

    /**
     * load key id to redis for search
     */
    public void loadKeyIdFromDB() {
        loadKeyIdFromDBByAlgorithm(CipherAlgorithm.RSA);
        loadKeyIdFromDBByAlgorithm(CipherAlgorithm.AES);
    }

    private void loadKeyIdFromDBByAlgorithm(CipherAlgorithm cipherAlgorithm) {
        List<String> keyIdList = cipherKeyMapper.selectAllKeyId(cipherAlgorithm);
        log.debug("{} {} keyIds in DB", cipherAlgorithm, keyIdList.size());
        if (keyIdList.isEmpty()) {
            return;
        }
        Long count = stringRedisTemplate.boundSetOps(cipherAlgorithm + ".keyId").add(keyIdList.toArray(new String[0]));
        log.debug("load {} {} keyIds to Redis", count, cipherAlgorithm);
    }

    /**
     * add key id to redis
     *
     * @param keyId
     * @param cipherAlgorithm
     * @return
     */
    public Long cacheKeyId(String keyId, CipherAlgorithm cipherAlgorithm) {
        return stringRedisTemplate.boundSetOps(cipherAlgorithm + ".keyId").add(keyId);
    }

    /**
     * judge if key id exists
     *
     * @param keyId
     * @param cipherAlgorithm
     * @return
     */
    public Boolean isKeyIdExists(String keyId, CipherAlgorithm cipherAlgorithm) {
        return stringRedisTemplate.boundSetOps(cipherAlgorithm + ".keyId").isMember(keyId);
    }


    public CipherKeyPair selectKeyPairByKeyId(String keyId) {
        return cipherKeyMapper.selectKeyPairByKeyId(keyId);
    }

    public int insertCipherKeyPair(CipherKeyPair cipherKeyPair) {
        cacheKeyId(cipherKeyPair.getKeyId(), cipherKeyPair.getCipherAlgorithm());
        if (cipherKeyMapper.isKeyPairInDb(cipherKeyPair.getKeyId()) == 0) {
            return cipherKeyMapper.insertCipherKeyPair(cipherKeyPair);
        }
        return 0;
    }

    public void cacheCipherKeyPair(CipherKeyPair cipherKeyPair, Long expire) {
        keyPairRedisTemplate.opsForValue()
                .set(cipherKeyPair.getKeyId(), cipherKeyPair, expire, TimeUnit.SECONDS);
    }

    public CipherKeyPair restoreCipherKeyPair(String keyId) {
        return keyPairRedisTemplate.opsForValue().get(keyId);
    }
}
