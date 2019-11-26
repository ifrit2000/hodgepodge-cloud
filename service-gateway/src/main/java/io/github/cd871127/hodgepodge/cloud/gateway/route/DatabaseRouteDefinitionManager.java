package io.github.cd871127.hodgepodge.cloud.gateway.route;

import org.springframework.cloud.gateway.route.RouteDefinition;

import java.util.List;

/**
 * @author Anthony Chen
 * @date 2019/11/26
 **/
public class DatabaseRouteDefinitionManager implements RouteDefinitionManager {


    @Override
    public void save(RouteDefinition routeDefinition) {

    }

    @Override
    public RouteDefinition load(String routeId) {
        return null;
    }

    @Override
    public long delete(String routeId) {
        return 0;
    }

    @Override
    public List<RouteDefinition> all() {
        return null;
    }

    @Override
    public boolean contains(String routeId) {
        return false;
    }
}
