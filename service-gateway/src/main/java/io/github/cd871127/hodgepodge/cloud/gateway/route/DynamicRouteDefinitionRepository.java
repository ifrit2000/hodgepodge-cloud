package io.github.cd871127.hodgepodge.cloud.gateway.route;

import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionRepository;
import org.springframework.cloud.gateway.support.NotFoundException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @author Anthony Chen
 * @date 2019/11/26
 **/
@Slf4j
@Setter
@Accessors(chain = true)
public class DynamicRouteDefinitionRepository implements RouteDefinitionRepository {

    private RedisRouteDefinitionManager redisRouteDefinitionManager;

    private DatabaseRouteDefinitionManager databaseRouteDefinitionManager;

    @Override
    public Flux<RouteDefinition> getRouteDefinitions() {
        log.info("getRouteDefinitions");
        return Flux.fromIterable(redisRouteDefinitionManager.all());
    }

    @Override
    public Mono<Void> save(Mono<RouteDefinition> route) {
        log.info("save");
        return route.flatMap(r -> {
            redisRouteDefinitionManager.save(r);
            return Mono.empty();
        });
    }

    @Override
    public Mono<Void> delete(Mono<String> routeId) {
        log.info("delete");
        return routeId.flatMap(id -> {
            if (redisRouteDefinitionManager.contains(id)) {
                redisRouteDefinitionManager.delete(id);
                return Mono.empty();
            }
            return Mono.defer(() -> Mono.error(
                    new NotFoundException("RouteDefinition not found: " + routeId)));
        });
    }


}
