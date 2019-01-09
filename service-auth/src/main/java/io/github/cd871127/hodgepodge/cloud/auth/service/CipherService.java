package io.github.cd871127.hodgepodge.cloud.auth.service;

import io.github.cd871127.hodgepodge.cloud.auth.client.CipherClient;
import io.github.cd871127.hodgepodge.cloud.lib.cipher.CipherConfig;
import io.github.cd871127.hodgepodge.cloud.lib.cipher.CipherDataEntity;
import io.github.cd871127.hodgepodge.cloud.lib.cipher.RsaCipher;
import io.github.cd871127.hodgepodge.cloud.lib.util.Pair;
import io.github.cd871127.hodgepodge.cloud.lib.util.ResponseException;
import io.github.cd871127.hodgepodge.cloud.lib.util.ResponseHandler;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.Map;

@Service
public class CipherService {
    @Resource
    private CipherClient cipherClient;

    private RsaCipher rsaCipher = null;

    @PostConstruct
    private void init() {
        try {
            rsaCipher = new RsaCipher(rsaCipherConfig());
        } catch (ResponseException e) {
            e.printStackTrace();
            rsaCipher = null;
        }
    }

    public Map<String, String> publicKey(Long expire) throws ResponseException {
        return ResponseHandler.handleResponse(cipherClient.publicKey(expire));
    }

    public Boolean comparison(CipherDataEntity data1, CipherDataEntity data2) throws ResponseException {
        Pair<CipherDataEntity, CipherDataEntity> pair = new Pair<>();
        pair.setLeft(data1);
        pair.setRight(data2);
        return ResponseHandler.handleResponse(cipherClient.comparison(pair));
    }

    public Map<String, String> publicKey(String keyId) throws ResponseException {
        return ResponseHandler.handleResponse(cipherClient.publicKey(keyId));
    }

    private CipherConfig rsaCipherConfig() throws ResponseException {
        return ResponseHandler.handleResponse(cipherClient.rsaCipherConfig());
    }

    public String encode(String data, String publicKey) {
        if (rsaCipher == null) {
            return null;
        }
        CipherDataEntity cipherDataEntity = new CipherDataEntity();
        cipherDataEntity.setData(data);
        byte[] bytes = rsaCipher.encode(cipherDataEntity.getBytes(), rsaCipher.base64StringPublicKey(publicKey));
        cipherDataEntity.setBytes(bytes);
        return cipherDataEntity.getData();
    }
}
