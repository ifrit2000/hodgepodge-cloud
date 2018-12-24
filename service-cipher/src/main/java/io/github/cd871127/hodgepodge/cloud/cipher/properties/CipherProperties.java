package io.github.cd871127.hodgepodge.cloud.cipher.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "cipher")
public class CipherProperties {
    private Long keyExpire=3000L;
}
