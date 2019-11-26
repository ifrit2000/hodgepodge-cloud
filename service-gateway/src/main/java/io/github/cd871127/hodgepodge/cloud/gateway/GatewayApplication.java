package io.github.cd871127.hodgepodge.cloud.gateway;

import com.alibaba.fastjson.parser.ParserConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author anthony
 */
@SpringBootApplication
@EnableDiscoveryClient
public class GatewayApplication {
    public static void main(String[] args) {
//        GatewayControllerEndpoint;
//        RouteDefinitionRouteLocator;
        SpringApplication.run(GatewayApplication.class, args);
//        GatewayAutoConfiguration
    }


}
