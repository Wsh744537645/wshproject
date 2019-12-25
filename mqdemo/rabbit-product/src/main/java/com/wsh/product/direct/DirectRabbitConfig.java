package com.wsh.product.direct;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author jmfen
 * date 2019-11-27
 */

@Configuration
public class DirectRabbitConfig {

    //队列
    @Bean
    public Queue getDirectQueue(){
        return new Queue("TestDirectQueue", true); //true 是否持久化
    }

    //交换机
    @Bean
    public DirectExchange getDirectExchange(){
        return new DirectExchange("TestDirectExchange");
    }

    //绑定  将队列和交换机绑定, 并设置用于匹配键：wsh-direct-routing
    @Bean
    public Binding bindingDirect(){
        return BindingBuilder.bind(getDirectQueue()).to(getDirectExchange()).with("wsh-direct-routing");
    }
}