package com.wsh.consumer.config;

import com.wsh.consumer.direct.DirectRabbitConfig;
import com.wsh.consumer.direct.DirectReceiver;
import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author jmfen
 * date 2019-11-29
 */

@Configuration
public class MessageListenerConfig {
    @Autowired
    private CachingConnectionFactory cachingConnectionFactory;

    //Direct消息接收处理类
    @Autowired
    private DirectReceiver directReceiver;

    @Autowired
    private DirectRabbitConfig directRabbitConfig;

    @Bean
    public SimpleMessageListenerContainer simpleMessageListenerContainer(){
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer(cachingConnectionFactory);
        container.setConcurrentConsumers(1);
        container.setMaxConcurrentConsumers(1);
        container.setAcknowledgeMode(AcknowledgeMode.MANUAL);
        container.setQueues(directRabbitConfig.getDirectQueue());
        container.setMessageListener(directReceiver);
        return container;
    }
}