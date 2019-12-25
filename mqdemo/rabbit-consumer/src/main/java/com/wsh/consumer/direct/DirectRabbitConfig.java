package com.wsh.consumer.direct;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author jmfen
 * date 2019-11-28
 */

@Configuration
public class DirectRabbitConfig {

    @Bean
    public Queue getDirectQueue(){
        return new Queue("TestDirectQueue", true);
    }

    @Bean
    public DirectExchange getDirectExchange(){
        return new DirectExchange("TestDirectExchange");
    }

    @Bean
    Binding bindingDirect(){
        return BindingBuilder.bind(getDirectQueue()).to(getDirectExchange()).with("wsh-direct-routing");
    }
}