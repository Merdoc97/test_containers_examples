package com.github.examples.config.consumer;

import com.github.examples.config.model.Car;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Service;

/**
 */
@Service
@Slf4j
public class ConsumerExample {

    @Autowired
    private KafkaTemplate<String,Object> kafkaTemplate;


    @KafkaListener(topics = "rpc.request",groupId = "rpc",containerFactory = "kafkaListenerContainerFactory")
    @SendTo("rpc.response")
//    send to doesn't work instead can't find correlation id
    public Car listen(Car in) {
        log.info("message received: {}" , in.toString());
        Car car=  in;
        car.setMake("new Maker");

        //
        log.info("modifying message {}",car);

       return car;
    }
}
