package io.github.cd871127.hodgepodge.cloud.gateway.config;

import io.github.cd871127.hodgepodge.cloud.gateway.route.DatabaseRouteDefinitionManager;
import io.github.cd871127.hodgepodge.cloud.gateway.route.DynamicRouteDefinitionRepository;
import io.github.cd871127.hodgepodge.cloud.gateway.route.RedisRouteDefinitionManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * @author Anthony Chen
 * @date 2019/11/26
 **/
@Slf4j
@Configuration
public class RouteDefinitionRepositoryConfiguration {

    @Bean
    @ConditionalOnClass(RedisTemplate.class)
    @SuppressWarnings("unchecked")
    public RedisRouteDefinitionManager redisRouteDefinitionManager(RedisTemplate redisTemplate) {
        return new RedisRouteDefinitionManager(redisTemplate);
    }

    @Bean
    public DatabaseRouteDefinitionManager databaseRouteDefinitionManager() {
        return new DatabaseRouteDefinitionManager();
    }

    @Bean
    @ConditionalOnClass({RedisRouteDefinitionManager.class, DatabaseRouteDefinitionManager.class, RedisTemplate.class})
    public DynamicRouteDefinitionRepository dynamicRouteDefinitionRepository(RedisRouteDefinitionManager redisRouteDefinitionManager,
                                                                             DatabaseRouteDefinitionManager databaseRouteDefinitionManager) {
        log.info("Create DynamicRouteDefinitionRepository");
        return new DynamicRouteDefinitionRepository()
                .setDatabaseRouteDefinitionManager(databaseRouteDefinitionManager)
                .setRedisRouteDefinitionManager(redisRouteDefinitionManager);
    }

}
