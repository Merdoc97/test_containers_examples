package com.github.examples.config.consumer;

import com.github.examples.config.model.People;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.concurrent.CountDownLatch;

/**.
 */
@Slf4j
@Getter
@Service
public class PeopleReceiver {

    /**
     * simple explanation of CountDownLatch
     * https://habrahabr.ru/post/277669/
     */
    private CountDownLatch latch = new CountDownLatch(1);

    @KafkaListener(topics = "${kafka.topic.people}",groupId = "people",containerFactory = "peopleListenerFactory")
    public void receive(People payload) {
        log.info("people received='{}'", payload);
        latch.countDown();
    }
}
