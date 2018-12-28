package io.github.cd871127.hodgepodge.cloud.cipher.configure;

import io.github.cd871127.hodgepodge.cloud.cipher.algorithm.AsymmetricCipher;
import io.github.cd871127.hodgepodge.cloud.cipher.algorithm.RsaCipher;
import io.github.cd871127.hodgepodge.cloud.cipher.configure.properties.CipherProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties({CipherProperties.class})
public class CipherConfiguration {

    @Bean(name = "rsaCipher")
    public AsymmetricCipher rsaCipher(CipherProperties cipherProperties) {
        return new RsaCipher(cipherProperties.getRsa());
    }

//    @Bean(name = "aesCipher")
//    public AsymmetricCipher aesCipher(CipherProperties cipherProperties) {
//        return new AesCipher(cipherProperties.getAes());
//    }
}
