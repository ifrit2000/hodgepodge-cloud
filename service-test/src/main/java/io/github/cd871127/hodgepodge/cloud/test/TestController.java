package io.github.cd871127.hodgepodge.cloud.test;

import io.github.cd871127.hodgepodge.redis.RedisLock;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 *
 */
@RestController
@RequestMapping("/test")
public class TestController {

    @Resource
    private RedisLock redisLock;

    @GetMapping("lock/{value}")
    public String lock(@PathVariable String value) {
        return redisLock.lock("TEST", value).toString();
    }

    @GetMapping("unlock/{value}")
    public String unlock(@PathVariable String value) {
        return redisLock.unlock("TEST", value).toString();
    }
}
