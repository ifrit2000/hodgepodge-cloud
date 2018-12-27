package io.github.cd871127.hodgepodge.cloud.cipher.configure.properties;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "cipher")
public class CipherProperties {
    private Rsa rsa = new Rsa();
    private Aes aes = new Aes();

    @EqualsAndHashCode(callSuper = true)
    @Data
    private static class Rsa extends CipherConfig {
        private Rsa() {
            setAlgorithm("RSA/ECB/PKCS1Padding");
            setKeyAlgorithm("RSA");
            setKeyLength(2048);
        }
    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    public static class Aes extends CipherConfig {

    }
}
