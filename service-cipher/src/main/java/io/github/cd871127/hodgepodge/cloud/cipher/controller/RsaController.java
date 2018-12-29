package io.github.cd871127.hodgepodge.cloud.cipher.controller;

import io.github.cd871127.hodgepodge.cloud.cipher.algorithm.CryptoString;
import io.github.cd871127.hodgepodge.cloud.cipher.exception.CipherException;
import io.github.cd871127.hodgepodge.cloud.cipher.exception.InvalidKeyIdException;
import io.github.cd871127.hodgepodge.cloud.cipher.exception.KeyIdExpiredException;
import io.github.cd871127.hodgepodge.cloud.cipher.service.impl.RsaService;
import io.github.cd871127.hodgepodge.cloud.lib.web.server.response.ServerResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
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
    public ServerResponse<String> encode(@RequestBody CryptoString cryptoString, HttpServletRequest request) throws NoSuchAlgorithmException, InvalidKeyIdException, KeyIdExpiredException {

        String keyId = cryptoString.getKeyId();
        String data = cryptoString.getData();
        if (StringUtils.isEmpty(keyId)) {
            throw new InvalidKeyIdException("empty keyId");
        }
        ServerResponse<String> serverResponse = new ServerResponse<>(SUCCESSFUL);
        byte[] res = rsaService.decode(keyId, Base64.getDecoder().decode(data));
        serverResponse.setData(Base64.getEncoder().encodeToString(res));
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
