package io.github.cd871127.hodgepodge.cloud.gateway.route;

import org.springframework.cloud.gateway.route.RouteDefinition;

import java.util.List;

/**
 * @author Anthony Chen
 * @date 2019/11/26
 **/
public interface RouteDefinitionManager {
    /**
     * save RouteDefinition
     *
     * @param routeDefinition @see org.springframework.cloud.gateway.route.RouteDefinition
     */
    void save(RouteDefinition routeDefinition);

    /**
     * load
     *
     * @param routeId
     * @return
     */
    RouteDefinition load(String routeId);

    /**
     * delete
     *
     * @param routeId
     * @return
     */
    long delete(String routeId);

    /**
     * return all
     *
     * @return
     */
    List<RouteDefinition> all();

    /**
     * @param routeId
     * @return
     */
    boolean contains(String routeId);
}
