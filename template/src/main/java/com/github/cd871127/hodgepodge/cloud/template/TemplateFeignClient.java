package com.github.cd871127.hodgepodge.cloud.template;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient("template")
public interface TemplateFeignClient {
    @RequestMapping(value = "/template/health", method = RequestMethod.GET)
    String sayHiFromClientOne();
}
