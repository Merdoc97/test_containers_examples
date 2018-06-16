package com.github.examples.config.consumer;

import com.github.examples.config.model.Car;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Configuration
@EnableKafka
public class ReceiverConfig {

  @Value("${spring.kafka.consumer.bootstrap-servers}")
  private String bootstrapServers;

  @Bean
  public Map<String, Object> consumerConfigs() {
    Map<String, Object> props = new HashMap<>();
    props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
    props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
    props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);

    return props;
  }

  @Bean
  public ConsumerFactory<String, List<Car>> consumerFactory() {

    return new DefaultKafkaConsumerFactory<>(consumerConfigs(), new StringDeserializer(),
            new JsonDeserializer<>());
  }

  @Bean
  public ConcurrentKafkaListenerContainerFactory<String, List<Car>> kafkaListenerContainerFactory() {
    ConcurrentKafkaListenerContainerFactory<String, List<Car>> factory =
        new ConcurrentKafkaListenerContainerFactory<>();
    factory.setConsumerFactory(consumerFactory());

    return factory;
  }


}
