package io.github.cd871127.hodgepodge.cloud.cipher.crypto;

public interface DataConvert<T> {
    byte[] rawTypeToBytes(T data);

    T bytesToRawType(byte[] bytes);
}
