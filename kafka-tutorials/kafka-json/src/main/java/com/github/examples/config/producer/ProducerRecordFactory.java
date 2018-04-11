package com.github.examples.config.producer;

import org.apache.kafka.clients.producer.ProducerRecord;

/**
 */
public class ProducerRecordFactory {
    private static final ProducerRecordFactory PRODUCER_RECORD_FACTORY = new ProducerRecordFactory();

    private ProducerRecordFactory() {
    }

    public static ProducerRecordFactory getProducerRecord() {
        return PRODUCER_RECORD_FACTORY;
    }

    public <K, V> ProducerRecord<K, V> createRecord(String topic, V value) {
        return new ProducerRecord<>(topic, value);
    }
}
