package com.github.examples.config.producer;

import com.github.examples.config.model.Car;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class Sender {

    @Autowired
    private KafkaTemplate<String, List<Car>> kafkaTemplate;

    @Value("${kafka.topic.json}")
    private String carTopic;
    @Value("${kafka.topic.rpc}")
    private String peopleTopic;


    //async for different topics
    public void send(List<Car> car) {
        log.info("sending car='{}' to topic='{}'", car, carTopic);
        kafkaTemplate.send(carTopic, car);
    }

}
