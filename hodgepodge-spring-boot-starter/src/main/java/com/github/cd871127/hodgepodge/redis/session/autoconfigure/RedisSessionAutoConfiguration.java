package com.github.cd871127.hodgepodge.redis.session.autoconfigure;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;

@Slf4j
@Configuration
public class RedisSessionAutoConfiguration {
    /**
     * 用json的序列化器替换掉默认的session序列化器
     *
     * @return
     */
    @ConditionalOnClass({GenericJackson2JsonRedisSerializer.class, RedisSerializer.class})
    @Bean(name = "springSessionDefaultRedisSerializer")
    public RedisSerializer springSessionDefaultRedisSerializer() {
        log.info("init jsonSerializer for session");
        return new GenericJackson2JsonRedisSerializer();
    }

}
