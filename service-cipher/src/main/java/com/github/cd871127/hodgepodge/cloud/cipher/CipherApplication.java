package com.github.cd871127.hodgepodge.cloud.cipher;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

@SpringBootApplication
@EnableRedisHttpSession
@Configuration
public class CipherApplication {
    public static void main(String[] args) {
        SpringApplication.run(CipherApplication.class, args);
    }

}
