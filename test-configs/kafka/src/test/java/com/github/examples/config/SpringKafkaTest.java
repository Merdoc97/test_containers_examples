package com.github.examples.config;


import com.github.examples.config.KafkaTestContainers;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;
import org.testcontainers.containers.DockerComposeContainer;

@RunWith(SpringRunner.class)

public class SpringKafkaTest {

  @ClassRule
  public static DockerComposeContainer kafkaInContainer = KafkaTestContainers.kafkaEmbedded("src/main/resources/docker-compose.yml");
  @Test
  public void testReceive() throws Exception {

  }
}
