package com.github.cd871127.hodgepodge.cloud.template;

import com.github.cd871127.hodgepodge.cloud.lib.server.ServerResponse;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.web.bind.annotation.RestController;


@SpringBootApplication
@RestController
@EnableFeignClients
public class TemplateApplication {

    public static void main(String[] args) {
        ServerResponse a;
        SpringApplication.run(TemplateApplication.class, args);
    }

}


