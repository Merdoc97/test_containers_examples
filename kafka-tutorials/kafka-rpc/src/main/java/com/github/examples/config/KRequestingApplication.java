package com.github.examples.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.listener.KafkaMessageListenerContainer;
import org.springframework.kafka.listener.config.ContainerProperties;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.kafka.requestreply.RequestReplyFuture;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.kafka.support.SendResult;
import org.springframework.kafka.support.converter.KafkaMessageHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.GenericMessage;

import java.util.HashMap;
import java.util.Map;

/**
 */
@SpringBootApplication
@EnableKafka
public class KRequestingApplication {

    public static void main(String[] args) {
        SpringApplication.run(KRequestingApplication.class, args).close();
    }

    @Bean
    public ApplicationRunner runner(ReplyingKafkaTemplate<String, String, String> template) {
        return args -> {
            ProducerRecord<String, String> record = new ProducerRecord<>("k.Requests", "foo");
            record.headers().add(new RecordHeader(KafkaHeaders.TOPIC, "k.Replies".getBytes()));

            RequestReplyFuture<String, String, String> replyFuture = template.sendAndReceive(record);
            SendResult<String, String> sendResult = replyFuture.getSendFuture().get();
            System.out.println("Sent ok: " + sendResult.getRecordMetadata().toString());
            ConsumerRecord<String, String> consumerRecord = replyFuture.get();
            System.out.println("Return value: " + consumerRecord.value());
        };
    }

    @Bean(name = "kafkaTemplate")
    @Primary
    public ReplyingKafkaTemplate<String, String, String> replyingKafkaTemplate(
            ProducerFactory<String, String> pf,
            KafkaMessageListenerContainer<String, String> replyContainer) {
        return new ReplyingKafkaTemplate<>(pf, replyContainer);
    }

    @Bean
    public KafkaMessageListenerContainer<String, String> replyContainer(
            ConsumerFactory<String, String> cf) {
        ContainerProperties containerProperties = new ContainerProperties("k.Replies");
        KafkaMessageListenerContainer kafkaMessageListenerContainer= new KafkaMessageListenerContainer<>(cf, containerProperties);
        return kafkaMessageListenerContainer;
    }

    @Bean
    public NewTopic kRequests() {
        return new NewTopic("k.Requests", 10, (short) 2);
    }

    @Bean
    public NewTopic kReplies() {
        return new NewTopic("k.Replies", 10, (short) 2);
    }


    @Value("${spring.kafka.consumer.bootstrap-servers}")
    private String bootstrapServers;

    @Autowired
    private KafkaTemplate<String,String>kafkaTemplate;


    @KafkaListener(topics = "k.Requests")
//    @SendTo
//    it same as SendTo
    // use default replyTo expression
    public void listen(Message<String> in) {
        System.out.println("Server received: " + in.toString());
        GenericMessage<String>message=new GenericMessage<>(in.getPayload().toString().toUpperCase(),in.getHeaders());


        kafkaTemplate.send(message);
    }


}
