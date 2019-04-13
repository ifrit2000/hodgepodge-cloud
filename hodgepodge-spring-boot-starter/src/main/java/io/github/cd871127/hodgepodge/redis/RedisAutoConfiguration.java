package io.github.cd871127.hodgepodge.redis;

import com.alibaba.fastjson.support.spring.GenericFastJsonRedisSerializer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;

/**
 * @author anthony
 */
@Slf4j
@Configuration
@ConditionalOnClass({RedisTemplate.class})
@EnableConfigurationProperties(RedisProperties.class)
public class RedisAutoConfiguration {
    /**
     * 用json的序列化器替换掉默认的session序列化器
     *
     * @return
     */
    @Bean
    @ConditionalOnClass({GenericJackson2JsonRedisSerializer.class, RedisSerializer.class})
    public RedisSerializer springSessionDefaultRedisSerializer() {
        log.info("Init jsonSerializer for session");
        return new GenericJackson2JsonRedisSerializer();
    }

    @Bean
    public RedisTemplate<Object, Object> redisTemplate(
            RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<Object, Object> template = new RedisTemplate<>();
        template.setDefaultSerializer(new GenericFastJsonRedisSerializer());
        template.setConnectionFactory(redisConnectionFactory);
        return template;
    }

    @Bean
    public RedisLock redisLock(RedisTemplate<String,String> redisTemplate) {
        return new RedisLock(redisTemplate);
    }

}
