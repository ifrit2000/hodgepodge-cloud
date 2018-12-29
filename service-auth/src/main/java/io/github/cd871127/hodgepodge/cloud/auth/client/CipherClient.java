package io.github.cd871127.hodgepodge.cloud.auth.client;

import com.sun.org.apache.xpath.internal.operations.Bool;
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

    @PostMapping(value="/rsa/comparison")
    ServerResponse<Boolean> comparison(@RequestBody Pair<Map<String,String>, Map<String,String>> dataEntityPair);
}
