package io.github.cd871127.hodgepodge.cloud.t66y.service;

import io.github.cd871127.hodgepodge.cloud.t66y.mapper.T66yMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class T66yService {
    @Resource
    private T66yMapper t66yMapper;
}
