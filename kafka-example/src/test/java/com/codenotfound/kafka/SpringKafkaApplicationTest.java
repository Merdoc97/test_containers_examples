package com.codenotfound.kafka;


import com.github.examples.config.KafkaTestContainers;
import com.github.examples.config.SpringKafkaApplication;
import com.github.examples.config.consumer.Receiver;
import com.github.examples.config.producer.Sender;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.testcontainers.containers.DockerComposeContainer;

import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = SpringKafkaApplication.class)
public class SpringKafkaApplicationTest {

  protected final static String HELLOWORLD_TOPIC = "helloworld.t";

  @Autowired
  private Receiver receiver;

  @Autowired
  private Sender sender;

  @ClassRule
  public static DockerComposeContainer kafkaInContainer = KafkaTestContainers.kafkaEmbedded("src/main/resources/docker-compose.yml");

  @Test
  public void testReceive() throws Exception {
    sender.send(HELLOWORLD_TOPIC, "Test Spring Kafka!");
    receiver.getLatch().await(10000, TimeUnit.MILLISECONDS);
    assertThat(receiver.getLatch().getCount()).isEqualTo(0);
  }
}
