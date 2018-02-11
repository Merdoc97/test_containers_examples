package com.github.examples.config;

import org.junit.ClassRule;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.testcontainers.containers.GenericContainer;

/**
 * Created on 09.01.2017.
 * imbeded postgres sql for tests only
 */
@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = {TestDataSourceConfig.class, JpaConfig.class, BeanConfiguration.class})
public class TestConfiguration {
    

    @ClassRule
    public static GenericContainer container = ElasticSearchEmbeddedConfiguration.elasticEmbedded(9201);


}
