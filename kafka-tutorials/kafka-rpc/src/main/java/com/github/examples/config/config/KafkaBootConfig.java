package com.github.examples.config.config;

import com.github.examples.config.model.Car;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.*;
import org.springframework.kafka.listener.KafkaMessageListenerContainer;
import org.springframework.kafka.listener.config.ContainerProperties;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

/**

 */
@Configuration
public class KafkaBootConfig {

    @Value("${spring.kafka.consumer.bootstrap-servers}")
    private String bootstrapServers;

    @Value("${spring.kafka.consumer.auto-offset-reset}")
    private String offSetReset;


    @Bean(name = "kafkaTemplate")
    @Primary
    public ReplyingKafkaTemplate<String, Car, Car> replyTemplate(
            ProducerFactory<String, Car> producerFactory,
//            it's important to configure response queue if you want to use request reply pattern
            KafkaMessageListenerContainer<String, Car> replyContainer) {
        ReplyingKafkaTemplate template=new ReplyingKafkaTemplate<>(producerFactory, replyContainer);
        return template;
    }


    @Bean
    public KafkaMessageListenerContainer<String, Car> replyContainer(
            ConsumerFactory<String, Car> consumerFactory) {
        ContainerProperties containerProperties = new ContainerProperties("rpc.response");
        return new KafkaMessageListenerContainer<>(consumerFactory, containerProperties);
    }

    @Bean
    public NewTopic kRequests() {
        return new NewTopic("rpc.request", 10, (short) 2);
    }

    @Bean
    public NewTopic kReplies() {
        return new NewTopic("rpc.response", 10, (short) 2);
    }

    @Bean
    public KafkaTemplate<String, Car> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }

    @Bean
    public Map<String, Object> producerConfigs() {
        Map<String, Object> props = new HashMap<>();
        // list of host:port pairs used for establishing the initial connections
        // to the Kakfa cluster
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        // value to block, after which it will throw a TimeoutException
        props.put(ProducerConfig.MAX_BLOCK_MS_CONFIG, 5000);


        return props;
    }

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
        props.put(ConsumerConfig.GROUP_ID_CONFIG,"rpc");
        return props;
    }

    @Bean
    public KafkaListenerContainerFactory kafkaListenerContainerFactory(KafkaTemplate kafkaTemplate) {
         return KafkaListenerBuilder.createFactory(Car.class,consumerConfigs())
//if you want use request reply pattern don't use with batch parameter
                 .withConsumerFactory(consumerFactory())
                 .withReplyTemplate(kafkaTemplate)
                 .build();
    }



    @Bean
    public ConsumerFactory<String, Car> consumerFactory() {
        return new DefaultKafkaConsumerFactory<>(consumerConfigs(),new StringDeserializer(),new JsonDeserializer<>(Car.class));
    }


    @Bean
    public ProducerFactory<String,Car> producerFactory() {
        return new DefaultKafkaProducerFactory<>(producerConfigs());
    }
}
