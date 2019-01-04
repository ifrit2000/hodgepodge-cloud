package io.github.cd871127.hodgepodge.cloud.cipher;

import io.github.cd871127.hodgepodge.cloud.cipher.service.CipherKeyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.core.annotation.Order;

import javax.annotation.Resource;

/**
 * @author anthony
 */
@SpringBootApplication
@EnableDiscoveryClient
@Order(2)
@Slf4j
public class CipherApplication implements ApplicationRunner {
    public static void main(String[] args) {
        SpringApplication.run(CipherApplication.class, args);
    }

    @Resource
    private CipherKeyService cipherKeyService;

    @Override
    public void run(ApplicationArguments args) {
        cipherKeyService.loadKeyIdFromDB();
    }
}
