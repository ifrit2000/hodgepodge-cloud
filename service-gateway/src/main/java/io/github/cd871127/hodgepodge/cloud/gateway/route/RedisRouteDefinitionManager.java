package io.github.cd871127.hodgepodge.cloud.gateway.route;

import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Anthony Chen
 * @date 2019/11/26
 **/
public class RedisRouteDefinitionManager implements RouteDefinitionManager {
    private static final String REDIS_ROUTE_DEFINITION_KEY = "redisRouteDefinition";

    private BoundHashOperations<String, String, RouteDefinition> hashOperations;

    public RedisRouteDefinitionManager(RedisTemplate<String, RouteDefinition> redisTemplate) {
        hashOperations = redisTemplate.boundHashOps(REDIS_ROUTE_DEFINITION_KEY);
    }

    @Override
    public void save(RouteDefinition routeDefinition) {
        hashOperations.put(routeDefinition.getId(), routeDefinition);
    }

    @Override
    public RouteDefinition load(String routeId) {
        return hashOperations.get(routeId);
    }

    @Override
    public long delete(String routeId) {
        Long result = hashOperations.delete(routeId);
        if (result == null) {
            return 0L;
        }
        return result;
    }

    @Override
    public List<RouteDefinition> all() {
        List<RouteDefinition> result = hashOperations.values();
        if (result == null) {
            return new ArrayList<>();
        }
        return result;
    }

    @Override
    public boolean contains(String routeId) {
        Boolean result = hashOperations.hasKey(routeId);
        if (result == null) {
            return false;
        }
        return result;
    }
}
