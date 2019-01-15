package io.github.cd871127.hodgepodge.cloud.test;


import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Sender {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void send(String message) {
        System.out.println("Sender : " + message);
        this.rabbitTemplate.convertAndSend("ex","key", message);
    }
}
