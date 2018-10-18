package com.github.cd871127.hodgepodge.cloud.cas.controller;

import com.github.cd871127.hodgepodge.cloud.cas.mapper.CasMapper;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RequestMapping("/cas")
@RestController
public class CasController {
    @Resource
    private CasMapper casMapper;

    @RequestMapping("test")
    public int test() {
        return casMapper.test();
    }
}
