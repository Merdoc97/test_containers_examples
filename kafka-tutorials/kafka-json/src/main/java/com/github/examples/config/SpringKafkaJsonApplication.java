package com.github.examples.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableAutoConfiguration

public class SpringKafkaJsonApplication {

  public static void main(String[] args) {
    SpringApplication.run(SpringKafkaJsonApplication.class, args);

  }
}
