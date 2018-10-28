package com.github.cd871127.hodgepodge.cloud.lib.crypto;

public interface Encript {
    byte[] encode(byte[] bytes);
    byte[] dncode(byte[] bytes);
}
