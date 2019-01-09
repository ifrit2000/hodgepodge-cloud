package io.github.cd871127.hodgepodge.cloud.cipher.controller;

import io.github.cd871127.hodgepodge.cloud.cipher.configure.properties.CipherProperties;
import io.github.cd871127.hodgepodge.cloud.cipher.exception.CipherException;
import io.github.cd871127.hodgepodge.cloud.cipher.exception.KeyIdExpiredException;
import io.github.cd871127.hodgepodge.cloud.cipher.service.CommonService;
import io.github.cd871127.hodgepodge.cloud.lib.cipher.CipherConfig;
import io.github.cd871127.hodgepodge.cloud.lib.web.server.response.ServerResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

import static io.github.cd871127.hodgepodge.cloud.cipher.util.response.CipherResponse.ALGORITHM_ERROR;
import static io.github.cd871127.hodgepodge.cloud.cipher.util.response.CipherResponse.KEY_ID_EXPIRED;
import static io.github.cd871127.hodgepodge.cloud.lib.web.server.response.GeneralHodgepodgeResponse.FAILED;
import static io.github.cd871127.hodgepodge.cloud.lib.web.server.response.GeneralHodgepodgeResponse.SUCCESSFUL;

@RestController
@RequestMapping("/cipher")
@Slf4j
public class CommonController {

    @Resource
    private CommonService commonService;

    /**
     * @param cipherProperties
     * @return
     */
    @GetMapping(value = "config/{algorithm}")
    public ServerResponse<CipherConfig> cipherConfig(@Autowired CipherProperties cipherProperties, @PathVariable String algorithm) {
        ServerResponse<CipherConfig> serverResponse = new ServerResponse<>(SUCCESSFUL);
        CipherConfig cipherConfig = null;
        if ("RSA".equalsIgnoreCase(algorithm)) {
            cipherConfig = cipherProperties.getRsa();
        } else if ("AES".equalsIgnoreCase(algorithm)) {
            cipherConfig = cipherProperties.getAes();
        }
        serverResponse.setData(cipherConfig);
        if (cipherConfig == null) {
            serverResponse.setHodgepodgeResponse(ALGORITHM_ERROR);
        }
        return serverResponse;
    }

    @PatchMapping("keyPair/{keyId}")
    public ServerResponse<String> persistentKeyPair(@PathVariable String keyId) throws KeyIdExpiredException {
        ServerResponse<String> serverResponse = new ServerResponse<>(SUCCESSFUL);
        commonService.persistentKey(keyId);
        serverResponse.setData(keyId);
        return serverResponse;
    }

    @DeleteMapping(value="keyPair/{keyId}")
    public ServerResponse<Boolean> deleteKeyPair(@PathVariable String keyId){
        ServerResponse<Boolean> serverResponse = new ServerResponse<>(SUCCESSFUL);
        serverResponse.setData(commonService.deleteKeyPair(keyId));
        return serverResponse;
    }

    @ExceptionHandler({CipherException.class})
    public ServerResponse cipherExceptionHandler(CipherException exception) {
        ServerResponse serverResponse = new ServerResponse(FAILED);
        log.error(exception.getMessage());
        try {
            throw exception;
        } catch (CipherException e) {
            serverResponse.setHodgepodgeResponse(KEY_ID_EXPIRED);
        }
        return serverResponse;
    }
}

