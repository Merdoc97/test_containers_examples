package com.github.examples.config.consumer;

import com.github.examples.config.model.Car;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;

import java.util.concurrent.CountDownLatch;

@Slf4j
@Getter
public class CarReceiver {

    /**
     * simple explanation of CountDownLatch
     * https://habrahabr.ru/post/277669/
     */
    private CountDownLatch latch = new CountDownLatch(1);

    @KafkaListener(topics = "${kafka.topic.json}",groupId = "json",containerFactory = "kafkaListenerContainerFactory")
    public void receive(Car payload) {
        log.info("car received='{}'", payload);
        latch.countDown();
    }
}
