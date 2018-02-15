package com.github.examples.config;

import org.testcontainers.containers.FixedHostPortGenericContainer;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.Wait;

import java.time.Duration;

/**
 * if you want use alfresco embedded configuration just inherit current class and add annotation @Configuration
 */
public class AlfrescoEcmEmveddedConfiguration {

    public static GenericContainer alfrescoEmbedded(Integer portDefault) {
        //    test container @see https://www.testcontainers.org/
        try (GenericContainer alfrescoEmbedded = new FixedHostPortGenericContainer("mikelasla/alfresco-standalone:latest")
                .withFixedExposedPort(portDefault, 8080)
                .withExposedPorts(8080)
                .waitingFor(Wait.forHttp("/api-explorer/").forStatusCode(200)) // Wait until elastic start
                .withNetworkAliases("alfresco")) {
            return alfrescoEmbedded;
        }
    }

    public static GenericContainer postgresAlfrescoEmbedded() {
        //    test container @see https://www.testcontainers.org/
        try (GenericContainer postgresEmbedded = new FixedHostPortGenericContainer("postgres:9.4")
                .withFixedExposedPort(5433, 5432)
                .withEnv("POSTGRES_DB", "alfresco")
                .withEnv("POSTGRES_USER", "alfresco")
                .withEnv("POSTGRES_PASSWORD", "alfresco")
                .waitingFor(Wait.forListeningPort()) // Wait until elastic start
                .withNetworkAliases("postgres")) {
            return postgresEmbedded;
        }
    }

    public static GenericContainer libreOfficeEmbedded() {

        //    test container @see https://www.testcontainers.org/
        try (GenericContainer libreOfficeEmbedded = new FixedHostPortGenericContainer("xcgd/libreoffice:latest")
                .waitingFor(Wait.defaultWaitStrategy().withStartupTimeout(Duration.ofSeconds(5))) // Wait until elastic start
                .withNetworkAliases("libreoffice")) {
            return libreOfficeEmbedded;
        }
    }
}
