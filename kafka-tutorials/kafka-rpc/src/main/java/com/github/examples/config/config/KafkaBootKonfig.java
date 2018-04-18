package com.github.examples.config.config;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.springframework.kafka.support.SimpleKafkaHeaderMapper;
import org.springframework.kafka.support.converter.DefaultJackson2JavaTypeMapper;
import org.springframework.kafka.support.converter.MessagingMessageConverter;
import org.springframework.kafka.support.converter.StringJsonMessageConverter;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

/**

 */
@Configuration
public class KafkaBootKonfig {

    @Value("${spring.kafka.consumer.bootstrap-servers}")
    private String bootstrapServers;

    @Value("${spring.kafka.consumer.auto-offset-reset}")
    private String offSetReset;


    @Bean(name = "kafkaTemplate")
    @Primary
    public ReplyingKafkaTemplate<String, Object, Object> replyTemplate(
            ProducerFactory<String, Object> producerFactory,
            KafkaMessageListenerContainer<String, Object> replyContainer) {
        ReplyingKafkaTemplate template=new ReplyingKafkaTemplate<>(producerFactory, replyContainer);
        template.setMessageConverter(simpleMapperConverter());

        return template;
    }


    @Bean
    public KafkaMessageListenerContainer<String, Object> replyContainer(
            ConsumerFactory<String, Object> consumerFactory) {
        ContainerProperties containerProperties = new ContainerProperties("rpc.response");

        KafkaMessageListenerContainer kafkaMessageListenerContainer= new KafkaMessageListenerContainer<>(consumerFactory, containerProperties);

        return kafkaMessageListenerContainer;
    }

    @Bean
    public NewTopic kRequests() {
        return new NewTopic("rpc.request", 10, (short) 2);
    }

    @Bean
    public NewTopic kReplies() {
        return new NewTopic("rpc.response", 10, (short) 2);
    }


    @Bean // not required if Jackson is on the classpath
    public MessagingMessageConverter simpleMapperConverter() {
        StringJsonMessageConverter messagingMessageConverter = new StringJsonMessageConverter(new ObjectMapper());
        SimpleKafkaHeaderMapper mapper=new SimpleKafkaHeaderMapper();

        messagingMessageConverter.setHeaderMapper(mapper);
        DefaultJackson2JavaTypeMapper typeMapper=new DefaultJackson2JavaTypeMapper();
        typeMapper.addTrustedPackages("*");
        messagingMessageConverter.setTypeMapper(typeMapper);
        messagingMessageConverter.setGenerateTimestamp(true);
        messagingMessageConverter.setGenerateMessageId(true);
        return messagingMessageConverter;
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
                 .withBatch(true)
                 .withMessageConverter(simpleMapperConverter())
                 .withReplyTemplate(kafkaTemplate)
                 .build();
    }


    @Bean
    public ConsumerFactory<String,Object>consumerFactory(){
        DefaultKafkaConsumerFactory factory=new DefaultKafkaConsumerFactory<>(consumerConfigs());
        factory.setKeyDeserializer(new StringDeserializer());
        JsonDeserializer deserializer=new JsonDeserializer();
        deserializer.addTrustedPackages("*");
        DefaultJackson2JavaTypeMapper typeMapper=new DefaultJackson2JavaTypeMapper();
        typeMapper.addTrustedPackages("*");
        deserializer.setTypeMapper(typeMapper);
        deserializer.addTrustedPackages("*");
        factory.setValueDeserializer(deserializer);

        return factory;
    }

    @Bean
    public ProducerFactory<String, Object> producerFactory() {
        DefaultKafkaProducerFactory factory=new DefaultKafkaProducerFactory<>(producerConfigs());
        factory.setKeySerializer(new StringSerializer());
        JsonSerializer serializer=new JsonSerializer();
        DefaultJackson2JavaTypeMapper typeMapper=new DefaultJackson2JavaTypeMapper();
//        it only for tests add yours mapping if needed
        typeMapper.addTrustedPackages("*");
        serializer.setTypeMapper(typeMapper);
        serializer.setAddTypeInfo(true);
        factory.setValueSerializer(serializer);
        factory.setKeySerializer(new StringSerializer());

        return factory;
    }
}
