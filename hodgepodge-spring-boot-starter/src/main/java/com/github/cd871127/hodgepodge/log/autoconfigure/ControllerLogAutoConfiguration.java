package com.github.cd871127.hodgepodge.log.autoconfigure;

import com.github.cd871127.hodgepodge.log.ControllerLogger;
import com.github.cd871127.hodgepodge.log.LogService;
import com.github.cd871127.hodgepodge.log.SimpleControllerLogService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Configuration
public class ControllerLogAutoConfiguration {

    @Bean
    @ConditionalOnClass({RestController.class, Controller.class, RequestMapping.class})
    public ControllerLogger controllerLogger(LogService logService) {
        return new ControllerLogger(logService);
    }

    @Bean
    LogService logService() {
        return new SimpleControllerLogService();
    }
}
