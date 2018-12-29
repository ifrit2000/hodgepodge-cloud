package io.github.cd871127.hodgepodge.cloud.auth;

import io.github.cd871127.hodgepodge.cloud.auth.service.UserService;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

import javax.annotation.Resource;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
public class AuthApplication implements ApplicationRunner {

    @Resource
    private UserService userService;

    public static void main(String[] args) {
        SpringApplication.run(AuthApplication.class, args);
    }

    @Override
    public void run(ApplicationArguments args) {
        userService.loadUserIdFromDB();
    }
}
