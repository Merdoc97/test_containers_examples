package com.github.examples.config.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.listener.KafkaMessageListenerContainer;
import org.springframework.kafka.listener.config.ContainerProperties;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;

@Configuration
public class Config {


    @Value("${kafka.topic.rpc-response}")
    private String responseTopic;

    @Bean(name = "kafkaTemplate")
    @Primary
    public ReplyingKafkaTemplate<?, ?, ?> replyTemplate(ProducerFactory<?, ?> producerFactory,
//            it's important to configure response queue if you want to use request reply pattern
                                                        KafkaMessageListenerContainer<?, ?> replyContainer) {
        return new ReplyingKafkaTemplate(producerFactory, replyContainer);
    }



    @Bean
    public KafkaMessageListenerContainer<?, ?> replyContainer(ConsumerFactory<?, ?> consumerFactory) {
        ContainerProperties containerProperties = new ContainerProperties(responseTopic);
        return new KafkaMessageListenerContainer<>(consumerFactory, containerProperties);
    }
}
