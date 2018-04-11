package com.github.examples.config.producer;

import com.github.examples.config.model.Car;
import com.github.examples.config.model.People;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.ListenableFuture;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

@Slf4j
@AllArgsConstructor
public class Sender {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    private final String carTopic;
    private final String peopleTopic;
    private final String rpcTopic;

    //async for different topics
    public void send(Car car) {
        log.info("sending car='{}' to topic='{}'", car, carTopic);
        kafkaTemplate.send(carTopic, car);
    }

    public void send(People people) {
        log.info("sending people='{}' to topic='{}'", people, peopleTopic);
        kafkaTemplate.send(peopleTopic, people);
    }

    //    sync messaging example
// by normally in consumer void must return value as a contract
    public Car sendAndReceiveCar(Car car) throws ExecutionException, InterruptedException, TimeoutException {
         ListenableFuture<SendResult<String,Object>> future=kafkaTemplate.send(rpcTopic, car);
        boolean res=future.completable().complete(future.get());
        return car;

    }

}
