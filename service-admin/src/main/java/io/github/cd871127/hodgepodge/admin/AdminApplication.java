package io.github.cd871127.hodgepodge.admin;

import de.codecentric.boot.admin.server.config.EnableAdminServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author Anthony Chen
 * @date 2019/11/22
 **/
@SpringBootApplication
@EnableAdminServer
@EnableDiscoveryClient
public class AdminApplication {
    public static void main(String[] args) {
        SpringApplication.run(AdminApplication.class, args);
    }
}
