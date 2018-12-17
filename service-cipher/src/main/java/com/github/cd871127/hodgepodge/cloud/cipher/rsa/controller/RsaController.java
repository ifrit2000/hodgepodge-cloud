package com.github.cd871127.hodgepodge.cloud.cipher.rsa.controller;

import com.github.cd871127.hodgepodge.cloud.cipher.rsa.service.RsaService;
import com.github.cd871127.hodgepodge.cloud.lib.web.server.response.ServerResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
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

    @Resource
    RedisTemplate redisTemplate;

    @RequestMapping(value = "test/{aa}")
    public String test(@PathVariable String aa) {
        redisTemplate.opsForValue().set("test", aa);
        return "ok";
    }

    @RequestMapping(value = "session/{v}")
    public String test(HttpSession session, @SessionAttribute(value = "test", required = false) String test, @PathVariable("v") String v) {
        if (StringUtils.isEmpty(test)) {
            session.setAttribute("test", v);
        }
        return v;
    }
}
