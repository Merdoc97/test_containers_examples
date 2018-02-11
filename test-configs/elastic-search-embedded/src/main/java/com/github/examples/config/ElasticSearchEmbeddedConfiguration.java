package com.github.examples.config;

import org.testcontainers.containers.FixedHostPortGenericContainer;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.Wait;

/**
 * if you want use elasticsearch embedded configuration just inherit current class and add annotation @Configuration
 */
public class ElasticSearchEmbeddedConfiguration {

    public static GenericContainer elasticEmbedded(Integer portDefault9200) {
        //    test container @see https://www.testcontainers.org/
//    it embedded elasticsearch for saving indexes in elastic for improving full text search

        try (GenericContainer elasticContainer = new FixedHostPortGenericContainer("docker.elastic.co/elasticsearch/elasticsearch:5.5.2")
                .withFixedExposedPort(portDefault9200, 9200)
                .withFixedExposedPort(9300, 9300)
                .waitingFor(Wait.forHttp("/")) // Wait until elastic start
                .withEnv("xpack.security.enabled", "false")
                .withEnv("http.host", "0.0.0.0")
                .withEnv("transport.host", "127.0.0.1")) {
            return elasticContainer;
        }


    }

}
