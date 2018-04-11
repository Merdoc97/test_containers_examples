package com.github.examples.config.consumer;

import com.github.examples.config.model.Car;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;

import java.util.concurrent.CountDownLatch;

@Slf4j
@Getter
public class RpcReceiver {

    /**
     * simple explanation of CountDownLatch
     * https://habrahabr.ru/post/277669/
     */
    private CountDownLatch latch = new CountDownLatch(1);

//    rpc second part
    @KafkaListener(topics = "${kafka.topic.rpc}",groupId = "rpc",containerFactory = "kafkaListenerContainerFactory")
    public Car consumeAndReturn(Car payload) {
        log.info("car received='{}'", payload);
        payload.setMake("new maker");
        latch.countDown();
        return payload;
    }
}
