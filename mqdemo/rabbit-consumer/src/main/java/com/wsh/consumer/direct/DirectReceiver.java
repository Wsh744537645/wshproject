package com.wsh.consumer.direct;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.ChannelAwareMessageListener;
import org.springframework.stereotype.Component;

import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * @author jmfen
 * date 2019-11-28
 */

@Slf4j
@Component
@RabbitListener(queues = "TestDirectQueue")
public class DirectReceiver implements ChannelAwareMessageListener {

    private Gson gson = new GsonBuilder().create();

//    @RabbitHandler
//    public void process(Map msg){
//        log.info("queue: TestDirectQueue, get message={}", msg);
//    }

    @Override
    public void onMessage(Message message, Channel channel) throws Exception {
        long deliveryTag = message.getMessageProperties().getDeliveryTag();
        try {
            String data = new String(message.getBody(), StandardCharsets.UTF_8);
            Type type = new TypeToken<String> () {
            }.getType();
            String str = gson.fromJson(data, type);
            System.out.println("data= "+ str);
            channel.basicAck(deliveryTag, true);
        }catch (Exception e){
            channel.basicReject(deliveryTag, false);
            e.printStackTrace();
        }
    }
}