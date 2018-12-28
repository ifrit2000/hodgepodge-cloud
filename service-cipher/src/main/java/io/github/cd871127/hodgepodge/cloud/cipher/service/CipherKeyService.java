package io.github.cd871127.hodgepodge.cloud.cipher.service;

import io.github.cd871127.hodgepodge.cloud.cipher.algorithm.CipherAlgorithm;
import io.github.cd871127.hodgepodge.cloud.cipher.mapper.CipherKeyMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.List;

@Service
@Slf4j
public class CipherKeyService {
    @Resource
    private CipherKeyMapper cipherKeyMapper;

    @Resource
    private RedisTemplate<String, String> redisTemplate;

    @PostConstruct
    private void init() {
        System.out.println(111);
    }

    public void loadKeyIdFromDB() {
        log.debug("load {} {} keyId", loadKeyIdFromDBByAlgorithm(CipherAlgorithm.RSA), CipherAlgorithm.RSA);
        log.debug("load {} {} keyId", loadKeyIdFromDBByAlgorithm(CipherAlgorithm.AES), CipherAlgorithm.AES);
    }

    private Long loadKeyIdFromDBByAlgorithm(CipherAlgorithm cipherAlgorithm) {
        List<String> keyIdList = cipherKeyMapper.selectAllKeyId(cipherAlgorithm);
        if (keyIdList.isEmpty()) {
            return 0L;
        }
        return redisTemplate.boundSetOps(cipherAlgorithm + ".keyId").add(keyIdList.toArray(new String[0]));
    }

    public Long cacheKeyId(String keyId, CipherAlgorithm cipherAlgorithm) {
        return redisTemplate.boundSetOps(cipherAlgorithm + ".keyId").add(keyId);
    }

    public Boolean isKeyIdExists(String keyId, CipherAlgorithm cipherAlgorithm) {
        return redisTemplate.boundSetOps(cipherAlgorithm + ".keyId").isMember(keyId);
    }
}
