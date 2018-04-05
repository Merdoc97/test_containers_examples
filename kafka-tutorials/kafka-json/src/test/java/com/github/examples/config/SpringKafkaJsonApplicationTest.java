package com.github.examples.config;


import com.github.examples.config.consumer.CarReceiver;
import com.github.examples.config.consumer.PeopleReceiver;
import com.github.examples.config.model.Car;
import com.github.examples.config.model.People;
import com.github.examples.config.producer.Sender;
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

import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = SpringKafkaJsonApplication.class)
public class SpringKafkaJsonApplicationTest {

    private final static String JSON_T = "json.t";
    private final static String PEOPLE_T = "people.t";

    @Autowired
    private KafkaListenerEndpointRegistry kafkaListenerEndpointRegistry;

    @Autowired
    private CarReceiver carReceiver;

    @Autowired
    private PeopleReceiver peopleReceiver;

    @Autowired
    private Sender sender;

    @ClassRule
    public static KafkaEmbedded kafkaEmbedded = new KafkaEmbedded(1, true, JSON_T,PEOPLE_T);

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
    public void testReceivePeople() throws Exception {
        sender.send(new People("test","testovich"));
        peopleReceiver.getLatch().await(10000, TimeUnit.MILLISECONDS);
        assertThat(peopleReceiver.getLatch().getCount()).isEqualTo(0);
    }

    @Test
    public void testReceiveCar() throws Exception {
        sender.send(new Car("make", "UA", "unique"));
        carReceiver.getLatch().await(10000, TimeUnit.MILLISECONDS);
        assertThat(carReceiver.getLatch().getCount()).isEqualTo(0);
    }
}
