package com.github.examples.config.consumer;

import com.github.examples.config.model.Car;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Getter
@Service
public class CarReceiver {

    /**
     * simple explanation of CountDownLatch
     * https://habrahabr.ru/post/277669/
     */
//    private CountDownLatch latch = new CountDownLatch(1);

//    at least once example
//    for consume from many instances producer should send into topic and each consumer should have different group id
    @KafkaListener(topics = "${kafka.topic.json}",groupId = "${spring.kafka.consumer.group-id}")
    public void receive(List<Car> payload) {
        log.info("car received='{}'", payload);
    }
}
