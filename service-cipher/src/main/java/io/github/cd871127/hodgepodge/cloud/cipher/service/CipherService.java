package io.github.cd871127.hodgepodge.cloud.cipher.service;

import io.github.cd871127.hodgepodge.cloud.cipher.algorithm.CipherDataEntity;
import io.github.cd871127.hodgepodge.cloud.cipher.exception.KeyIdExpiredException;

import java.security.NoSuchAlgorithmException;
import java.util.Map;

/**
 *
 */
public interface CipherService {

    Map<String, String> getPublicKey(String keyId, Long expire) throws NoSuchAlgorithmException, KeyIdExpiredException;

    byte[] encode(CipherDataEntity dataEntity) throws KeyIdExpiredException, NoSuchAlgorithmException;

    byte[] decode(CipherDataEntity dataEntity) throws KeyIdExpiredException, NoSuchAlgorithmException;
}
