package io.github.cd871127.hodgepodge.schedule;

import io.github.cd871127.hodgepodge.redis.RedisLock;
import org.aspectj.lang.JoinPoint;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * @author anthony
 */
@Configuration
@ConditionalOnClass({Scheduled.class, JoinPoint.class})
@ConditionalOnBean({RedisLock.class})
public class ScheduleAutoConfiguration {

    @Bean
    public ScheduleAspect scheduleAspect(RedisLock redisLock) {
        return new ScheduleAspect(redisLock);
    }
}
