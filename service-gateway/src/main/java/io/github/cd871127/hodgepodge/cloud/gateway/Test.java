package io.github.cd871127.hodgepodge.cloud.gateway;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "test")
@Data
@RefreshScope
public class Test {
    private String tt;
}
