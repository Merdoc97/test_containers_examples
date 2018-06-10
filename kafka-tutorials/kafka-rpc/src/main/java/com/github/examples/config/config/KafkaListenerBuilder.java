package com.github.examples.config.config;

import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.converter.MessageConverter;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.retry.support.RetryTemplate;

import java.util.Map;

/**
 */
public class KafkaListenerBuilder {

    private KafkaListenerBuilder(){}
    private static KafkaListenerBuilder builder=new KafkaListenerBuilder();

    public static <V> Builder createFactory(Class<V>tClass,Map<String, Object> consumerConfigs){
        return  builder.new Builder<>(consumerConfigs,tClass);
    }

    public class Builder<V>{
        private ConcurrentKafkaListenerContainerFactory<String,V>containerFactory;
        private ConsumerFactory defaultKafkaConsumerFactory;
        private boolean isBatch;
        private MessageConverter messageConverter;
        private KafkaTemplate replyTemplate;
        private Integer concurrency;
        private RetryTemplate retryTemplate;
        private boolean stateFullRetry;

        public Builder(Map<String, Object> consumerConfigs,Class<V>tclass){
            this.containerFactory=new ConcurrentKafkaListenerContainerFactory<String, V>();
            this.defaultKafkaConsumerFactory=new DefaultKafkaConsumerFactory<>(consumerConfigs,new StringDeserializer(),new JsonDeserializer(tclass));
        }
        public Builder withConsumerFactory(ConsumerFactory consumerFactory){
            this.defaultKafkaConsumerFactory=consumerFactory;
            return this;
        }
        public Builder withBatch(boolean isBatch){
            this.isBatch=isBatch;
            return this;
        }

        public Builder withMessageConverter(MessageConverter converter){
            this.messageConverter=converter;
            return this;
        }
        public Builder withReplyTemplate(KafkaTemplate kafkaTemplate){
            this.replyTemplate=kafkaTemplate;
            return this;
        }

        public Builder withConcurrency(Integer concurrency){
            this.concurrency=concurrency;
            return this;
        }
        public Builder withRetry(RetryTemplate retry){
            this.retryTemplate=retry;
            return this;
        }

        public Builder withStateFullRetry(boolean stateFullRetry){
            this.stateFullRetry=stateFullRetry;
            return this;
        }


        public  KafkaListenerContainerFactory build(){
            containerFactory.setBatchListener(this.isBatch);
            containerFactory.setReplyTemplate(this.replyTemplate);
            containerFactory.setMessageConverter(this.messageConverter);
            containerFactory.setConsumerFactory(this.defaultKafkaConsumerFactory);
            containerFactory.setConcurrency(this.concurrency);
            containerFactory.setRetryTemplate(this.retryTemplate);
            containerFactory.setStatefulRetry(this.stateFullRetry);
            return containerFactory;
        }
    }
}
