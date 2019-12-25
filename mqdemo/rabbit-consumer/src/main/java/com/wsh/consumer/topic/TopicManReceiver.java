package com.wsh.consumer.topic;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author jmfen
 * date 2019-11-28
 */

@Component
@RabbitListener(queues = "topic.man")
public class TopicManReceiver {

    @RabbitHandler
    public void process(Map msg){
        System.out.println("TopicManReceiver消费者收到消息  : " + msg.toString());
    }
}