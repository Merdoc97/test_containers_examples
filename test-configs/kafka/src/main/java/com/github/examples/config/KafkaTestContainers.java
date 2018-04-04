package com.github.examples.config;

import org.testcontainers.containers.DockerComposeContainer;

import java.io.File;

/**
 * if you want use kafka test containers configuration just inherit current class and add annotation @Configuration
 */
public class KafkaTestContainers {

    public static DockerComposeContainer kafkaEmbedded(String composeFile) {
        //    test container @see https://www.testcontainers.org/
//    it embedded kafka for testing with auto create topics
        return new DockerComposeContainer(new File(composeFile));
    }


}
