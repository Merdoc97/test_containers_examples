package com.github.examples.config;


import com.github.examples.config.model.Car;
import com.github.examples.config.reciever.ReceiverBootExample;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.kafka.listener.MessageListenerContainer;
import org.springframework.kafka.test.rule.KafkaEmbedded;
import org.springframework.kafka.test.utils.ContainerTestUtils;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = KRequestingApplication.class)
public class SpringKafkaJsonApplicationTest {

    @Autowired
    private KafkaListenerEndpointRegistry kafkaListenerEndpointRegistry;

    @Autowired
    private ReceiverBootExample receiver;

    @ClassRule
    public static KafkaEmbedded kafkaEmbedded = new KafkaEmbedded(1, true, "rpc.request","rpc.response");

    @Before
    public void runBeforeTestMethod() throws Exception {
        // wait until all the partitions are assigned
        for (MessageListenerContainer messageListenerContainer : kafkaListenerEndpointRegistry
                .getListenerContainers()) {
            ContainerTestUtils.waitForAssignment(messageListenerContainer,
                    kafkaEmbedded.getPartitionsPerTopic());
        }
    }

    @Test
    public void testRpc() throws Exception {
        Car response=receiver.sendAndReceiveExample(new Car("make", "UA", UUID.randomUUID()));
        assertThat(response.getMake()).isEqualTo("new Maker");
    }


}
