package com.github.examples.config;

import org.junit.ClassRule;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.testcontainers.containers.GenericContainer;

/**
 * Created on 09.01.2017.
 * imbeded postgres sql for tests only
 */
@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AlfrescoApp.class)

public class TestConfiguration {

    @ClassRule
    public static GenericContainer postgres = AlfrescoEcmEmveddedConfiguration.postgresAlfrescoEmbedded();

    @ClassRule
    public static GenericContainer libreOffice = AlfrescoEcmEmveddedConfiguration.libreOfficeEmbedded();

    @ClassRule
    public static GenericContainer alfrescoEmbedded = AlfrescoEcmEmveddedConfiguration.alfrescoEmbedded(8085);

    @ClassRule
    public static GenericContainer dms = AlfrescoEcmEmveddedConfiguration.dms(8081);

    @ClassRule
    public static GenericContainer zuul = AlfrescoEcmEmveddedConfiguration.zuul(8900);


}
