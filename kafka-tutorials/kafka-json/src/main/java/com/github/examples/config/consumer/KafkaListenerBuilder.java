package com.github.examples.config.consumer;

import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.converter.BatchMessagingMessageConverter;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.Map;

/**
 */
public class KafkaListenerBuilder {
    private KafkaListenerBuilder(){}

    public static <V> KafkaListenerContainerFactory buildFactory(Class<V> tClass,BatchMessagingMessageConverter converter,
                                                                 Map<String, Object> consumerConfigs){
        ConcurrentKafkaListenerContainerFactory<String, V> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(new DefaultKafkaConsumerFactory<>(consumerConfigs,new StringDeserializer(),new JsonDeserializer(tClass)));
        factory.setBatchListener(true);
        factory.setMessageConverter(converter);
        return factory;
    }
}
