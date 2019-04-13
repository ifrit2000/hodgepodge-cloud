package io.github.cd871127.hodgepodge.log.autoconfigure;

import io.github.cd871127.hodgepodge.log.ControllerLogger;
import io.github.cd871127.hodgepodge.log.LogService;
import io.github.cd871127.hodgepodge.log.SimpleControllerLogService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author anthony
 */
@Configuration
@ConditionalOnClass({ProceedingJoinPoint.class})
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
