package io.github.cd871127.hodgepodge.cloud.cipher.controller;

import io.github.cd871127.hodgepodge.cloud.cipher.algorithm.CipherDataEntity;
import io.github.cd871127.hodgepodge.cloud.cipher.exception.CipherException;
import io.github.cd871127.hodgepodge.cloud.cipher.exception.InvalidKeyIdException;
import io.github.cd871127.hodgepodge.cloud.cipher.exception.KeyIdExpiredException;
import io.github.cd871127.hodgepodge.cloud.cipher.service.impl.RsaService;
import io.github.cd871127.hodgepodge.cloud.lib.util.Pair;
import io.github.cd871127.hodgepodge.cloud.lib.web.server.response.ServerResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Map;

import static io.github.cd871127.hodgepodge.cloud.cipher.util.response.CipherResponse.*;
import static io.github.cd871127.hodgepodge.cloud.lib.web.server.response.GeneralHodgepodgeResponse.FAILED;
import static io.github.cd871127.hodgepodge.cloud.lib.web.server.response.GeneralHodgepodgeResponse.SUCCESSFUL;

@RequestMapping("/rsa")
@RestController
@Slf4j
public class RsaController {

    @Resource
    private RsaService rsaService;

    @GetMapping(value = {"publicKey/{keyId}", "publicKey"})
    public ServerResponse<Map<String, String>> publicKey(@PathVariable(required = false) String keyId, @RequestParam(value = "expire", required = false, defaultValue = "300") Long expire) throws KeyIdExpiredException, NoSuchAlgorithmException {
        ServerResponse<Map<String, String>> serverResponse = new ServerResponse<>(SUCCESSFUL);
        serverResponse.setData(rsaService.getPublicKey(keyId, expire));

        return serverResponse;
    }

    @PostMapping(value = {"decode"})
    public ServerResponse<String> decode(@RequestBody CipherDataEntity dataEntity) throws NoSuchAlgorithmException, InvalidKeyIdException, KeyIdExpiredException {
        if (StringUtils.isEmpty(dataEntity.getKeyId())) {
            throw new InvalidKeyIdException("empty keyId");
        }
        ServerResponse<String> serverResponse = new ServerResponse<>(SUCCESSFUL);
        byte[] res = rsaService.decode(dataEntity);
        serverResponse.setData(Base64.getEncoder().encodeToString(res));
        return serverResponse;
    }

    @PostMapping(value = "comparison")
    public ServerResponse<Boolean> comparison(@RequestBody Pair<CipherDataEntity, CipherDataEntity> dataEntityPair) throws NoSuchAlgorithmException, InvalidKeyIdException, KeyIdExpiredException {
        boolean result = rsaService.compare(dataEntityPair.getLeft(), dataEntityPair.getRight());
        ServerResponse<Boolean> serverResponse = new ServerResponse<>(SUCCESSFUL);
        serverResponse.setData(result);
        return serverResponse;
    }

    @ExceptionHandler({CipherException.class})
    public ServerResponse cipherExceptionHandler(CipherException exception) {
        ServerResponse serverResponse = new ServerResponse(FAILED);
        log.error(exception.getMessage());
        try {
            throw exception;
        } catch (KeyIdExpiredException e) {
            serverResponse.setHodgepodgeResponse(KEY_ID_EXPIRED);
        } catch (InvalidKeyIdException e) {
            serverResponse.setHodgepodgeResponse(INVALID_KEY_ID);
        } catch (CipherException e) {
            serverResponse.setHodgepodgeResponse(CIPHER_ERROR);
        }
        return serverResponse;
    }

    @ExceptionHandler({NoSuchAlgorithmException.class})
    public ServerResponse noSuchAlgorithmExceptionHandler(NoSuchAlgorithmException exception) {
        ServerResponse serverResponse = new ServerResponse(ALGORITHM_ERROR);
        log.error(exception.getMessage());
        return serverResponse;
    }
}
