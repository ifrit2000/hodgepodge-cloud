package io.github.cd871127.hodgepodge.charset;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import sun.nio.cs.UTF_8;

import java.nio.charset.Charset;

@Configuration
public class CharsetAutoConfiguration {
    @Bean
    public Charset charset() {
        return UTF_8.INSTANCE;
    }
}
