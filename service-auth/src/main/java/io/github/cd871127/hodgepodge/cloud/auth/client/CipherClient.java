package io.github.cd871127.hodgepodge.cloud.auth.client;

import io.github.cd871127.hodgepodge.cloud.lib.cipher.CipherDataEntity;
import io.github.cd871127.hodgepodge.cloud.lib.util.Pair;
import io.github.cd871127.hodgepodge.cloud.lib.web.server.response.ServerResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@FeignClient(value = "service-cipher")
public interface CipherClient {

    @RequestMapping(value = "/rsa/publicKey/{keyId}", method = RequestMethod.GET)
    ServerResponse<Map<String, String>> publicKey(@PathVariable(required = false) String keyId);

    @GetMapping(value = "/rsa/publicKey")
    ServerResponse<Map<String, String>> publicKey(@RequestParam(value = "expire", required = false, defaultValue = "300") Long expire);

    @PostMapping(value = "/rsa/comparison")
    ServerResponse<Boolean> comparison(@RequestBody Pair<CipherDataEntity, CipherDataEntity> dataEntityPair);

    @PatchMapping(value = "/cipher/cipherKey/{keyId}")
    ServerResponse<String> persistentKey(@PathVariable String keyId);
}
