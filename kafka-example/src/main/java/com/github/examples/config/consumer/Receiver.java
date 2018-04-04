package com.github.examples.config.consumer;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;

import java.util.concurrent.CountDownLatch;

@Slf4j
@Getter
public class Receiver {

    private CountDownLatch latch = new CountDownLatch(1);

    @KafkaListener(topics = "${kafka.topic.helloworld}")
    public void receive(String payload) {
        log.info("received payload='{}'", payload);
        latch.countDown();
        log.info("received payload='{}'", payload);
    }
}
