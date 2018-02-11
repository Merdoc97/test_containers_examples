package com.github.examples.config;

import com.opentable.db.postgres.embedded.EmbeddedPostgres;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;
import java.io.IOException;

/**
 * if you want use pg embedded configuration just inherit current class and add annotation @Configuration
 */
public class PgEmbeddedConfiguration {

    @Bean
    @Primary
    public DataSource dataSource() throws IOException {
        EmbeddedPostgres pg = EmbeddedPostgres.builder()
                .start();
        return pg.getPostgresDatabase();
    }

}
