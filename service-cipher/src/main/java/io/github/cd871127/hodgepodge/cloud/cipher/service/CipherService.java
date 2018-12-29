package io.github.cd871127.hodgepodge.cloud.cipher.service;

import io.github.cd871127.hodgepodge.cloud.cipher.algorithm.DataEntity;
import io.github.cd871127.hodgepodge.cloud.cipher.exception.KeyIdExpiredException;

import java.security.NoSuchAlgorithmException;
import java.util.Map;

/**
 *
 */
public interface CipherService {

    Map<String, String> getPublicKey(String keyId, Long expire) throws NoSuchAlgorithmException, KeyIdExpiredException;

    byte[] encode(DataEntity dataEntity) throws KeyIdExpiredException, NoSuchAlgorithmException;

    byte[] decode(DataEntity dataEntity) throws KeyIdExpiredException, NoSuchAlgorithmException;
}
