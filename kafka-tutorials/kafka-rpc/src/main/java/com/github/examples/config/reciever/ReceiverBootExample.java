package com.github.examples.config.reciever;

import com.github.examples.config.model.Car;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.kafka.requestreply.RequestReplyFuture;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

/**
 */
@Service
@Slf4j
public class ReceiverBootExample {


    @Autowired
    private ReplyingKafkaTemplate<String, Car, Car> template;

    public Car sendAndReceiveExample(Car car) throws ExecutionException, InterruptedException, TimeoutException {
        ProducerRecord<String, Car> record = new ProducerRecord<>("rpc.request", car);
        record.headers().add(KafkaHeaders.REPLY_TOPIC, "rpc.response".getBytes());
//        post in kafka topic
        RequestReplyFuture<String, Car, Car> replyFuture = template.sendAndReceive(record);
// confirm if producer produced successfully
        SendResult<String,Car> confirmResult=replyFuture.getSendFuture().get();
//print all headers it just for test and not needed in production
        confirmResult.getProducerRecord().headers().forEach(header -> log.info("headers: {}, {}",header.key(),header.value()));
        // get consumer record
        ConsumerRecord<String,Car> response = replyFuture.get();
        log.info("response: {}", response);
        return response.value();
    }

}
