package com.wsh.product.topic;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author jmfen
 * date 2019-11-28
 */

@Configuration
public class TopicRabbitConfig {
    //绑定键
    public final static String man = "topic.man";
    public final static String woman = "topic.woman";

    @Bean
    public Queue firstTopicQueue(){
        return new Queue(TopicRabbitConfig.man);
    }

    @Bean
    public Queue secondTopicQueue(){
        return new Queue(TopicRabbitConfig.woman);
    }

    @Bean
    public TopicExchange topicExchange(){
        return new TopicExchange("topicExchange");
    }

    @Bean
    public Binding firstTopicBinding(){
        return BindingBuilder.bind(firstTopicQueue()).to(topicExchange()).with(man);
    }

    @Bean
    public Binding secondTopicBinding(){
        return BindingBuilder.bind(secondTopicQueue()).to(topicExchange()).with("topic.#");
    }
}