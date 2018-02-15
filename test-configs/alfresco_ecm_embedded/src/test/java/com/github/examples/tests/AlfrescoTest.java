package com.github.examples.tests;

import com.github.examples.config.TestConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.context.ApplicationContext;

import javax.annotation.Resource;

/**
 *
 */
@Slf4j
public class AlfrescoTest extends TestConfiguration {

    @Resource
    private ApplicationContext context;
    @Test
    public void testAlfrescoEcm() throws InterruptedException {
        log.info("---------TEST ACCEPTED ALL CONTAINERS START CORRECTLY---------");

    }

}
