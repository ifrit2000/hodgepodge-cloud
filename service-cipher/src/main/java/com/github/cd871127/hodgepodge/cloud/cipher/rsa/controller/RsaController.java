package com.github.cd871127.hodgepodge.cloud.cipher.rsa.controller;

import com.github.cd871127.hodgepodge.cloud.cipher.rsa.service.RsaService;
import com.github.cd871127.hodgepodge.cloud.lib.web.server.response.ServerResponse;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Map;

import static com.github.cd871127.hodgepodge.cloud.cipher.response.CipherResponse.GET_RSA_PUBLIC_KEY_FAILED;

@RequestMapping("/rsa")
@RestController
public class RsaController {

    @Resource
    private RsaService rsaService;

    @RequestMapping(value = "publicKey/{keyId}", method = RequestMethod.GET)
    public ServerResponse<Map<String, String>> publicKey(@PathVariable(required = false) String keyId) {
        ServerResponse<Map<String, String>> serverResponse = new ServerResponse<>();
        Map<String, String> keyMap = rsaService.getPublicKey(keyId);
        if (keyMap == null) {
            serverResponse.setHodgepodgeResponse(GET_RSA_PUBLIC_KEY_FAILED);
        } else {
            serverResponse.setData(keyMap);
        }
        return serverResponse;
    }
}
