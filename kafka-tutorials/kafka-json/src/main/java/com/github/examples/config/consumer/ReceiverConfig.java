package com.github.examples.config.consumer;

/**
 */

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.examples.config.model.Car;
import com.github.examples.config.model.People;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.support.converter.BatchMessagingMessageConverter;
import org.springframework.kafka.support.converter.StringJsonMessageConverter;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableKafka
public class ReceiverConfig {

    @Value("${spring.kafka.consumer.bootstrap-servers}")
    private String bootstrapServers;

    @Value("${spring.kafka.consumer.auto-offset-reset}")
    private String offSetReset;


    @Bean
    public Map<String, Object> consumerConfigs() {
        Map<String, Object> props = new HashMap<>();
        // list of host:port pairs used for establishing the initial connections
        // to the Kakfa cluster
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        // consumer groups allow a pool of processes to divide the work of
        // consuming and processing records
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, offSetReset);

        return props;
    }

    @Bean
    public KafkaListenerContainerFactory kafkaListenerContainerFactory() {
        return KafkaListenerBuilder.buildFactory(Car.class,batchMessagingMessageConverter(),consumerConfigs());
    }


    @Bean
    public KafkaListenerContainerFactory peopleListenerFactory() {
        return KafkaListenerBuilder.buildFactory(People.class,batchMessagingMessageConverter(),consumerConfigs());
    }


    @Bean
    public StringJsonMessageConverter converter() {
        return new StringJsonMessageConverter(new ObjectMapper());
    }

    @Bean
    public BatchMessagingMessageConverter batchMessagingMessageConverter() {
        return new BatchMessagingMessageConverter(converter());
    }

    @Bean
    public CarReceiver receiver() {
        return new CarReceiver();
    }
}

