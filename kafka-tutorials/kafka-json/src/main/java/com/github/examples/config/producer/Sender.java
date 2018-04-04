package com.github.examples.config.producer;

import com.github.examples.config.model.Car;
import com.github.examples.config.model.People;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;

@Slf4j
@AllArgsConstructor
public class Sender {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    private final String carTopic;
    private final String peopleTopic;

    public void send( Car car) {
        log.info("sending car='{}' to topic='{}'", car, carTopic);
        kafkaTemplate.send(carTopic, car);
    }

    public void send(People people){
        log.info("sending people='{}' to topic='{}'", people, peopleTopic);
        kafkaTemplate.send(peopleTopic,people);
    }


}
