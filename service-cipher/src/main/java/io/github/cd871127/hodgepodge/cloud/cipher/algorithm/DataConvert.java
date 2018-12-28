package io.github.cd871127.hodgepodge.cloud.cipher.algorithm;

public interface DataConvert<T> {
    byte[] rawTypeToBytes(T data);

    T bytesToRawType(byte[] bytes);
}
