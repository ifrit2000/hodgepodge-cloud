package com.github.cd871127.hodgepodge.cloud.test;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {
    @RequestMapping("log/{id}")
    public String log(@PathVariable("id") String id) {
        return id;
    }
}
