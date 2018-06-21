package com.github.examples.config.consumer;

import com.github.examples.config.model.Car;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.TopicPartition;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.listener.ConsumerSeekAware;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Slf4j
@Getter
@Service
public class CarReceiver implements ConsumerSeekAware {

    /**
     * simple explanation of CountDownLatch
     * https://habrahabr.ru/post/277669/
     */


    //    at least once example
//    for consume from many instances producer should send into topic and each consumer should have different group id
    @KafkaListener(topics = "${kafka.topic.json}", groupId = "${spring.kafka.consumer.group-id}")
    public void receive(List<Car> payload) {
        log.info("car received='{}'", payload);
    }

    @Override
    public void registerSeekCallback(ConsumerSeekCallback callback) {

    }

    @Override
    public void onPartitionsAssigned(Map<TopicPartition, Long> assignments, ConsumerSeekCallback callback) {
        log.info("get latest message from topic");
//        get oofset for json topic
        long offset=assignments.get(assignments.keySet().stream().findFirst().get());
//        get latest message if present
        long currentOfset=offset==0?0:offset-1;
//        readl latest message
//        if in class present more than one different topic current void be run as many as topics present in class
        callback.seek(assignments.keySet().stream().findFirst().get().topic(),
                assignments.keySet().stream().findFirst().get().partition(),
                currentOfset);
    }

    @Override
    public void onIdleContainer(Map<TopicPartition, Long> assignments, ConsumerSeekCallback callback) {
        log.info("on idle container");
    }
}
