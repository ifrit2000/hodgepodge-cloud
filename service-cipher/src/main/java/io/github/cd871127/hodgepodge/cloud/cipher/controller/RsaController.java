package io.github.cd871127.hodgepodge.cloud.cipher.controller;

import io.github.cd871127.hodgepodge.cloud.cipher.algorithm.CryptoString;
import io.github.cd871127.hodgepodge.cloud.cipher.exception.InvalidKeyIdException;
import io.github.cd871127.hodgepodge.cloud.cipher.exception.KeyIdExpiredException;
import io.github.cd871127.hodgepodge.cloud.cipher.service.impl.RsaService;
import io.github.cd871127.hodgepodge.cloud.cipher.util.response.CipherResponse;
import io.github.cd871127.hodgepodge.cloud.lib.web.server.response.ServerResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Map;

import static io.github.cd871127.hodgepodge.cloud.lib.web.server.response.GeneralHodgepodgeResponse.SUCCESSFUL;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RequestMapping("/rsa")
@RestController
@Slf4j
public class RsaController {

    @Resource
    private RsaService rsaService;

    @RequestMapping(value = {"publicKey/{keyId}", "publicKey"}, method = RequestMethod.GET)
    public ServerResponse<Map<String, String>> publicKey(@PathVariable(required = false) String keyId, @RequestParam(value = "expire", required = false, defaultValue = "300") Long expire) {
        ServerResponse<Map<String, String>> serverResponse = new ServerResponse<>(SUCCESSFUL);
        try {
            serverResponse.setData(rsaService.getPublicKey(keyId, expire));
        } catch (NoSuchAlgorithmException e) {
            serverResponse.setHodgepodgeResponse(CipherResponse.INVALID_KEY_ID);
        } catch (KeyIdExpiredException e) {
            serverResponse.setHodgepodgeResponse(CipherResponse.KEY_ID_EXPIRED);
        }
        return serverResponse;
    }

    @RequestMapping(value = {"decode"}, method = POST)
    public ServerResponse<String> encode(@RequestBody CryptoString cryptoString, HttpServletRequest request) throws NoSuchAlgorithmException, InvalidKeyIdException {

        String keyId = cryptoString.getKeyId();
        String data = cryptoString.getData();
        if (StringUtils.isEmpty(keyId)) {
            throw new InvalidKeyIdException("empty keyId");
        }
        ServerResponse<String> serverResponse = new ServerResponse<>(SUCCESSFUL);
        try {
            byte[] res = rsaService.decode(keyId, Base64.getDecoder().decode(data));
            serverResponse.setData(Base64.getEncoder().encodeToString(res));
        } catch (NoSuchAlgorithmException e) {
            serverResponse.setHodgepodgeResponse(CipherResponse.INVALID_KEY_ID);
        } catch (KeyIdExpiredException e) {
            serverResponse.setHodgepodgeResponse(CipherResponse.KEY_ID_EXPIRED);
        }
        return serverResponse;
    }


}
