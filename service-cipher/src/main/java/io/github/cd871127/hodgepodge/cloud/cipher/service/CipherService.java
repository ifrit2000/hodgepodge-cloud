package io.github.cd871127.hodgepodge.cloud.cipher.service;

import io.github.cd871127.hodgepodge.cloud.cipher.exception.InvalidKeyIdException;

import java.security.NoSuchAlgorithmException;
import java.util.Map;

/**
 *
 */
public interface CipherService {

    Map<String, String> getPublicKey(String keyId, Long expire) throws NoSuchAlgorithmException, InvalidKeyIdException;

    byte[] encode(String keyId, byte[] data) throws InvalidKeyIdException, NoSuchAlgorithmException;

    byte[] decode(String keyId, byte[] data) throws InvalidKeyIdException, NoSuchAlgorithmException;
}
