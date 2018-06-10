package com.github.examples.config.consumer;

import com.github.examples.config.model.Car;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Service;

/**
 */
@Service
@Slf4j
public class ConsumerExample {


    @KafkaListener(topics = "rpc.request",groupId = "rpc")
    @SendTo
    public Car listen(Car in) {
        log.info("message received: {}" , in.toString());
        Car car=  in;
        car.setMake("new Maker");

        //
        log.info("modifying message {}",car);

       return car;
    }

}
