package com.github.examples.config.reciever;

import com.github.examples.config.model.Car;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.kafka.requestreply.RequestReplyFuture;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

/**
 */
@Service
@Slf4j
public class ReceiverBootExample {


    @Autowired
    private ReplyingKafkaTemplate<String, Object, Object> template;



    public Car sendAndReceiveExample(Car car) throws ExecutionException, InterruptedException, TimeoutException {
        ProducerRecord<String, Object> record = new ProducerRecord<>("rpc.request", car);
        record.headers().add(KafkaHeaders.REPLY_TOPIC,"rpc.response".getBytes());
        record.headers().add(KafkaHeaders.CORRELATION_ID,car.getId().toString().getBytes());
        RequestReplyFuture<String, Object, Object> replyFuture =template.sendAndReceive(record);
        template.setReplyTimeout(100000L);

        ConsumerRecord response=replyFuture.get();
//        No correlationId found in reply always timeout exception
        log.info("response: {}",response);
        return (Car) response.value();
    }
}
