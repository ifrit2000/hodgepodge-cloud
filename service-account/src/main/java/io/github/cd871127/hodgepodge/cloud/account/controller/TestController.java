package io.github.cd871127.hodgepodge.cloud.account.controller;

import io.github.cd871127.hodgepodge.cloud.account.mapper.TestMapper;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class TestController {
    @Resource
    TestMapper testMapper;

    @Resource
    StringRedisTemplate stringRedisTemplate;

    @GetMapping("/test")
    public int test() {
        stringRedisTemplate.boundValueOps("test").append("fff");
        stringRedisTemplate.boundValueOps("test").append("aa");

        return testMapper.getOne();
    }
}
