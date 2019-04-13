package io.github.cd871127.hodgepodge.charset;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.nio.charset.Charset;

/**
 * @author anthony
 */
@Configuration
public class CharsetAutoConfiguration {
    @Bean
    public Charset charset() {
        return Charset.forName("UTF-8");
    }
}
