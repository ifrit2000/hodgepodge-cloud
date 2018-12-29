package io.github.cd871127.hodgepodge.cloud.auth.service;

import io.github.cd871127.hodgepodge.cloud.auth.client.CipherClient;
import io.github.cd871127.hodgepodge.cloud.lib.util.Pair;
import io.github.cd871127.hodgepodge.cloud.lib.util.ResponseException;
import io.github.cd871127.hodgepodge.cloud.lib.util.ResponseHandler;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;

@Service
public class CipherService {
    @Resource
    private CipherClient cipherClient;

    public Map<String, String> publicKey(Long expire) throws ResponseException {
        return ResponseHandler.handleResponse(cipherClient.publicKey(expire));
    }

    public Boolean comparison(Map<String, String> data1, Map<String, String> data2) throws ResponseException {
        Pair<Map<String, String>, Map<String, String>> pair = new Pair<>();
        pair.setLeft(data1);
        pair.setRight(data2);
        return ResponseHandler.handleResponse(cipherClient.comparison(pair));
    }

    public Map<String, String> publicKey(String keyId) throws ResponseException {
        return ResponseHandler.handleResponse(cipherClient.publicKey(keyId));
    }


}
