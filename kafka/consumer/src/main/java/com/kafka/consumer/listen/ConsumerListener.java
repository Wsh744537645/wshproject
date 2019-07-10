package com.kafka.consumer.listen;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/**
 * @Author: stephen
 * @Date: 2019/7/10 11:42
 */

@Component
public class ConsumerListener {

    @KafkaListener(topics = "test_topic")
    public void listen(ConsumerRecord<?, ?> record){

        System.out.printf("topic = %s, offset = %d, value = %s\n", record.topic(), record.offset(), record.value());
    }
}
