package io.github.cd871127.hodgepodge.cloud.cipher.rsa.controller;

import io.github.cd871127.hodgepodge.cloud.cipher.crypto.CryptoString;
import io.github.cd871127.hodgepodge.cloud.cipher.exception.InvalidKeyIdException;
import io.github.cd871127.hodgepodge.cloud.cipher.response.CipherResponse;
import io.github.cd871127.hodgepodge.cloud.cipher.rsa.service.RsaService;
import io.github.cd871127.hodgepodge.cloud.lib.util.ByteArrayConversion;
import io.github.cd871127.hodgepodge.cloud.lib.web.server.response.ServerResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.nio.charset.Charset;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

import static io.github.cd871127.hodgepodge.cloud.lib.web.server.response.GeneralHodgepodgeResponse.SUCCESSFUL;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RequestMapping("/rsa")
@RestController
public class RsaController {

    @Resource
    private RsaService rsaService;

    @Resource
    private Charset charset;

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

    @RequestMapping(value = {"encode", "decode"}, method = POST)
    public ServerResponse<String> encode(@RequestBody CryptoString cryptoString, HttpServletRequest request) throws NoSuchAlgorithmException, InvalidKeyIdException {

        String keyId = cryptoString.getKeyId();
        String data = cryptoString.getData();
        if (StringUtils.isEmpty(keyId)) {
            throw new InvalidKeyIdException("empty keyId");
        }
        byte[] res = null;
        if (request.getRequestURI().contains("/encode/")) {
            res = rsaService.encode(keyId, data.getBytes(charset));
        } else if (request.getRequestURI().contains("/decode/")) {
            res = rsaService.decode(keyId, ByteArrayConversion.hexString2ByteArray(data));
        }

        ServerResponse<String> serverResponse = new ServerResponse<>(SUCCESSFUL);
        serverResponse.setData(ByteArrayConversion.byteArray2HexString(res));
        return serverResponse;
    }

}
