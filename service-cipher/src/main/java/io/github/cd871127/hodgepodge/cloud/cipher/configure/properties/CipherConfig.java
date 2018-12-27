package io.github.cd871127.hodgepodge.cloud.cipher.configure.properties;

import lombok.Data;

@Data
public class CipherConfig {
    private Integer keyLength;
    private String keyAlgorithm;
    private String algorithm;
}
