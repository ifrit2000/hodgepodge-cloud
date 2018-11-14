package com.github.cd871127.hodgepodge.cloud.cipher.controller;

import com.github.cd871127.hodgepodge.cloud.lib.web.server.response.ServerResponse;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/rsa")
@RestController
public class RsaController {

    @RequestMapping(value = "publicKey", method = RequestMethod.GET)
    public ServerResponse<String> publicKey() {
        ServerResponse<String> serverResponse = new ServerResponse<>();



        return serverResponse;
    }
}
