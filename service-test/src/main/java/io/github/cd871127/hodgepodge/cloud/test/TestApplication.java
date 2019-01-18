package io.github.cd871127.hodgepodge.cloud.test;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@SpringBootApplication
@RestController
@RequestMapping("/test")
public class TestApplication {

    @Bean
    public Queue queue() {
        return new Queue("hello");
    }

    @Bean
    public DirectExchange directExchange() {
        return new DirectExchange("ex");
    }

    @Bean
    public Binding binding() {
        //链式写法: 用指定的路由键将队列绑定到交换机
        return BindingBuilder.bind(queue()).to(directExchange()).with("key");
    }


    @Resource
    private Sender sender;

    @GetMapping("push/{message}")
    public String push(@PathVariable("message") String message) {
        sender.send(message);
        return null;
    }

    @GetMapping("pull/{message}")
    public String pull(@PathVariable("message") String message) {
        return null;
    }

}
