package io.github.cd871127.hodgepodge.cloud.gateway;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * @author anthony
 */
@SpringBootApplication
@RestController

public class GatewayApplication {
//    @Value("${spring.cloud.consul.config.prefix}")
    @Value("${test.ccccc}")
    private String bbb;



    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class, args);
    }

    @RequestMapping("test")
    public String test() {
        return bbb;
    }
}
