package io.github.cd871127.hodgepodge.schedule;

import io.github.cd871127.hodgepodge.redis.RedisLock;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

import java.util.UUID;

/**
 * @author anthony
 */
@Aspect
@Slf4j
public class ScheduleAspect {

    private RedisLock redisLock;

    public ScheduleAspect(RedisLock redisLock) {
        this.redisLock = redisLock;
    }

    @Pointcut("@annotation(org.springframework.scheduling.annotation.Scheduled)")
    public void schedule() {
    }

    @Around("schedule()")
    public Object around(ProceedingJoinPoint proceedingJoinPoint) {
        Object res = null;
        String lockName = proceedingJoinPoint.getSignature().getDeclaringTypeName() + "." + proceedingJoinPoint.getSignature().getName();
        String lockValue = UUID.randomUUID().toString();
        log.debug("Lock value: {}", lockValue);
        try {
            if (redisLock.lock(lockName, lockValue)) {
                log.info("Get lock: {}", lockName);
                res = proceedingJoinPoint.proceed();
            } else {
                log.info("Fail to get lock: {}", lockName);
            }
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        } finally {
            redisLock.unlock(lockName, lockValue);
            log.info("Release lock: {}", lockName);
        }
        return res;
    }

}
