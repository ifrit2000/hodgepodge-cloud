package io.github.cd871127.hodgepodge.redis;


import org.springframework.data.redis.core.BoundValueOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;

import java.util.Collections;
import java.util.concurrent.TimeUnit;

/**
 * @author anthony
 */
public class RedisLock {

    public RedisLock(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
        this.unlockScript = new DefaultRedisScript<>();
        this.unlockScript.setScriptText("if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end");
        unlockScript.setResultType(Boolean.class);
    }

    private RedisTemplate<String, String> redisTemplate;

    private DefaultRedisScript<Boolean> unlockScript;

    public Boolean lock(String lockName, String lockValue) {
        return lock(lockName, lockValue, 0);
    }

    public Boolean lock(String lockName, String lockValue, long expiration) {
        BoundValueOperations<String, String> redis = redisTemplate.boundValueOps(lockName(lockName));
        if (expiration != 0) {
            return redis.setIfAbsent(lockValue, expiration, TimeUnit.MILLISECONDS);
        } else {
            return redis.setIfAbsent(lockValue);
        }
    }

    public Boolean unlock(String lockName, String lockValue) {
        return redisTemplate.execute(unlockScript, Collections.singletonList(lockName(lockName)), lockValue);
    }

    private String lockName(String lockName) {
        return "Lock." + lockName;
    }

}
