package com.github.examples.config;

import com.github.examples.impl.NewsSearchServiceImpl;
import com.github.examples.interfaces.NewsSearchService;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 */
@Configuration
@Import(JpaConfig.class)
public class BeanConfiguration {
    @Bean
    public NewsSearchService newsSearchService(@Qualifier("fullTextEntityManager") FullTextEntityManager fullTextEntityManager) {
        return new NewsSearchServiceImpl(fullTextEntityManager);
    }
}
