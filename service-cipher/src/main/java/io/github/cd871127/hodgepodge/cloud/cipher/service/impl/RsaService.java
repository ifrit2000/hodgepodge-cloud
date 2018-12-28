package io.github.cd871127.hodgepodge.cloud.cipher.service.impl;

import io.github.cd871127.hodgepodge.cloud.cipher.algorithm.AsymmetricCipher;
import io.github.cd871127.hodgepodge.cloud.cipher.algorithm.keypair.RsaKeyPair;
import io.github.cd871127.hodgepodge.cloud.cipher.exception.InvalidKeyIdException;
import io.github.cd871127.hodgepodge.cloud.cipher.mapper.RsaMapper;
import io.github.cd871127.hodgepodge.cloud.cipher.service.CipherService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class RsaService implements CipherService {

    @Resource
    private AsymmetricCipher rsaCipher;

    @Resource
    private RedisTemplate<String, RsaKeyPair> redisTemplate;

    @Resource
    private RsaMapper rsaMapper;

    private RsaKeyPair getRsaKeyPair(String keyId, Long expire) throws NoSuchAlgorithmException, InvalidKeyIdException {
        RsaKeyPair rsaKeyPair;
        if (StringUtils.isEmpty(keyId)) {
            rsaKeyPair = rsaCipher.getBase64KeyPair();
            keyId = UUID.randomUUID().toString().replaceAll("-", "");
            rsaKeyPair.setKeyId(keyId);
            if (0 == expire) {
                rsaMapper.insertRsaKeyPair(rsaKeyPair);
            } else {
                redisTemplate.opsForValue().set(keyId, rsaKeyPair, expire, TimeUnit.SECONDS);
            }
        } else {
            rsaKeyPair = redisTemplate.opsForValue().get(keyId);
//            if (rsaKeyPair == null) {
//                throw new InvalidKeyIdException("Invalid RSA keyId");
//            }
        }

        return rsaKeyPair;
    }

    private RsaKeyPair getRsaKeyPair(String keyId) throws InvalidKeyIdException, NoSuchAlgorithmException {
        return getRsaKeyPair(keyId, 0L);
    }

    public Map<String, String> getPublicKey(String keyId, Long expire) throws NoSuchAlgorithmException, InvalidKeyIdException {
        RsaKeyPair rsaKeyPair = getRsaKeyPair(keyId, expire);
        Map<String, String> res = new HashMap<>();
        res.put("keyId", rsaKeyPair.getKeyId());
        res.put("publicKey", rsaKeyPair.getPublicKey());
        return res;
    }

    public byte[] encode(String keyId, byte[] data) throws InvalidKeyIdException, NoSuchAlgorithmException {
        RsaKeyPair rsaKeyPair = getRsaKeyPair(keyId);
        return rsaCipher.encode(data, rsaCipher.base64StringPublicKey(rsaKeyPair.getPublicKey()));

    }

    public byte[] decode(String keyId, byte[] data) throws InvalidKeyIdException, NoSuchAlgorithmException {
        RsaKeyPair rsaKeyPair = getRsaKeyPair(keyId);
        PrivateKey privateKey = rsaCipher.base64StringToPrivateKey("MIIEvAIBADANBgkqhkiG9w0BAQEFAASCBKYwggSiAgEAAoIBAQCWJsE+fmI9kMDHTFSGVIAaKrnw1nfKKsQfHuZzaRqv8vP2lHHuqBrBbbs+oKl+aF3kz9Yr971jqLHStwXTDpPzW+xINMkmwlkenovFULoVvUGWdrU+4M0TGuhVdzEFrT/KnSAwxXNgj96AMazJtJMf7EKz4SH1PzFn8f2lZK9LmVimNocePU0+lXj+3wOw4oLa8Jitlt5a3AkBCBu41FzFpfo7QUezpfCbjvSqauALyjPDylEec8xM7x94naJA6w/axvrqzI6V9mN3HIOu4hJ8FCq8+CD4dCRYFm1QJ9qeC/+/K6CW9rKc7Fx39UbFsHpmtrqKIQ1NFgH6M71gOGmfAgMBAAECggEAMJNlHSSh/6ze0RselT6tGsoL0aBwrJTdUfwbLeco3RdKWdF4cm8sCLvJQd+UNfLpvWaHsT26pY0jyjmvxrIGp0przIhXMxTY5BECwtj0+qd5moXY6PitH6sq0st2rpF3+8KNcXnPc8PXhb2MWszyc/dpNOx2ofLJtwkQt/s0ws3fjl2ZtlDbBTIM/E27ZCvw8ENvGkKeuHcERLkZB9rMPs6pnM8ICf1eDyCBquEVtBgDYb65MlFWg5TYHfwSbIxNfkwh8yEaz9W7kBjDvPgiwKXZqsoMLmhsB2fUOsoaWJhxdWWtICmI2mrCxYk9KQ2zwM3YoLAkXl9peD70+7zdUQKBgQDd3H+vNYZAv6Prmt5vMQFHZ4b2hg+KQAFtKl6GjzW4xxaLTVCJMIzGp+kOMgphevCTlxLSV0wunWzdSJ0OHKgbq+qAT0ZjD8sgt2xVyU+Z6pFQj/kz/tfbNIP+oHGJ8HLh/18uwLNsgtIChb7JG6sKHHJwniClXGwmr9HW66jUNQKBgQCtQXpHEPz3m04pBkXLOLjFElSbipb9OqMCeTb54ApwZLjqaHBV40P0aH8hz5fDfvvHOdo1qtiXa4OQ81B7TzBoO/p+JGLRLC86d8T/V2LmxgDIothAf/E2FnYDiAH7vSPsDZ218CqU5k8Hhe4b4faaA0x1Nsfcea+Ij7VFTYnZAwKBgDx8/aL7aNsGZN67nqGaLssVAsr7ygjbYogs4RC2wuLaBN99+NMulXMkHHpuUj45kpXqvorymiarbR73yTorvfmtaYYKFxqzF6KX38WT2UwRlATu+/adKKTvMH2fqNT+5ZOQWJcamtCe6jsd0+Jo1L0w/FKQCj6LcEEr9n4uSh1xAoGAeopSQwt38GVPLeL64Fa5EOH7J5wpWOftPaWgRbG5kG8c/uZpdcXtXWO/b5mVfLdGu01m0giJcuefQZlmdiC3WzH45Nk1bz6yFMd7dSJImHK5QS80hsI3SAsw4ySCSpwnWSD0SCea5n/Sq76FgAEdWyc0H79kMsN6bLs/+cly0yMCgYBqcjjnNWE8dXRnt83F5SykBas5YilG8zqvcXaKJAKfQH/PHflrxvUva98zMYhWXlk2H5bBLSfUoMYNSM3RM78GKFtvkMxyu0a/c0lo9UA8ZQAKv3VLommYdpcynHKbBWPiTq1Rapu7HvD0N26wsrmcKDVQZCfK+pzQRNN8EcqXoA==");
        return rsaCipher.decode(data, privateKey);
//        return rsaEncipher.decode(data, rsaEncipher.base64StringToPrivateKey(rsaKeyPair.getPrivateKey()));
    }

}
