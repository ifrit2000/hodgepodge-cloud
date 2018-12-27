package io.github.cd871127.hodgepodge.cloud.cipher.crypto;

import java.security.PrivateKey;
import java.security.PublicKey;

public interface Encipher {

    byte[] encode(byte[] bytes, PublicKey publicKey);

    byte[] decode(byte[] bytes, PrivateKey privateKey);
}
