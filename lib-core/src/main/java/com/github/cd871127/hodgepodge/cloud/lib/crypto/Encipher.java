package com.github.cd871127.hodgepodge.cloud.lib.crypto;

import java.security.PrivateKey;
import java.security.PublicKey;

public interface Encipher {
    byte[] encode(byte[] bytes, PrivateKey privateKey);

    byte[] decode(byte[] bytes, PublicKey publicKey);
}
