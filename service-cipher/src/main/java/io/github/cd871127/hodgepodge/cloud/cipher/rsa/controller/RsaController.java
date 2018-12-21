package io.github.cd871127.hodgepodge.cloud.cipher.rsa.controller;

import io.github.cd871127.hodgepodge.cloud.cipher.exception.InvalidKeyIdException;
import io.github.cd871127.hodgepodge.cloud.cipher.response.CipherResponse;
import io.github.cd871127.hodgepodge.cloud.cipher.rsa.service.RsaService;
import io.github.cd871127.hodgepodge.cloud.lib.web.server.response.ServerResponse;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

import static io.github.cd871127.hodgepodge.cloud.lib.web.server.response.GeneralHodgepodgeResponse.SUCCESSFUL;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RequestMapping("/rsa")
@RestController
public class RsaController {

    @Resource
    private RsaService rsaService;

    @RequestMapping(value = {"publicKey/{keyId}", "publicKey"}, method = RequestMethod.GET)
    public ServerResponse<Map<String, String>> publicKey(@PathVariable(required = false) String keyId) {
        ServerResponse<Map<String, String>> serverResponse = new ServerResponse<>(SUCCESSFUL);
        try {
            serverResponse.setData(rsaService.getPublicKey(keyId));
        } catch (NoSuchAlgorithmException e) {
            serverResponse.setHodgepodgeResponse(CipherResponse.INVALID_KEY_ID);
        } catch (InvalidKeyIdException e) {
            serverResponse.setHodgepodgeResponse(CipherResponse.RSA_NOT_EXISTS);
        }
        return serverResponse;
    }

    @RequestMapping(value = "encode/{keyId}", method = POST)
    public ServerResponse encode(@PathVariable(required = false) String keyId, @RequestBody String data) {
        return null;
    }

    @RequestMapping(value = "decode/{keyId}", method = POST)
    public ServerResponse decode(@PathVariable(required = false) String keyId,@RequestBody String data) {
        return null;
    }

}
