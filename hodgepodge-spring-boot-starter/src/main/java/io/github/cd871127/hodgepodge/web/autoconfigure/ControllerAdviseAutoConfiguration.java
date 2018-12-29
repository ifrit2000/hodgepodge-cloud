package io.github.cd871127.hodgepodge.web.autoconfigure;

import io.github.cd871127.hodgepodge.web.WebControllerAdvise;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Configuration
public class ControllerAdviseAutoConfiguration {

    @Bean
    @ConditionalOnClass({RestController.class, Controller.class, RequestMapping.class})
    public WebControllerAdvise webControllerAdvise() {
        return new WebControllerAdvise();
    }


}
