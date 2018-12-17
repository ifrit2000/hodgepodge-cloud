package com.github.cd871127.hodgepodge.redis.session.autoconfigure;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;

@Slf4j
@Configuration
public class RedisSessionAutoConfiguration {
    @Bean(name = "springSessionDefaultRedisSerializer")
    public RedisSerializer springSessionDefaultRedisSerializer() {
        log.info("222222");
        return new GenericJackson2JsonRedisSerializer();
    }

    @Bean
    public String testStr() {
        log.info("111111111111");
        return "1";
    }
}
