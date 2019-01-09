package io.github.cd871127.hodgepodge.cloud.lib.cipher;

import lombok.Data;

@Data
public class CipherConfig {
    private Integer keyLength;
    private String keyAlgorithm;
    private String algorithm;
}
