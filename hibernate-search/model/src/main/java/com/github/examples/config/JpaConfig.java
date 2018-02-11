package com.github.examples.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.flywaydb.core.Flyway;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.Search;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.sql.DataSource;
import java.util.Properties;

/**
 *
 */
@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = {"com.github.examples.repository", "com.github.examples.model"})
@Slf4j
public class JpaConfig {
    @Value("${db.url}")
    private String jdbcUrl;
    @Value("${db.user}")
    private String dbuser;
    @Value("${db.password}")
    private String dbpassword;
    @Value("${db.driver.classname}")
    private String driverClassName;
    @Value("${db.hibernate.connection.pool_size}")
    private int poolSize;
    @Value("${db.hibernate.hbm2ddl.auto}")
    private String hbm2ddlAuto;
    @Value("${db.hibernate.connection.charset}")
    private String charsetEncoding;
    @Value("${db.hibernate.batch.size}")
    private int batchSize;
    @Value("${db.hibernate.sql.dialect}")
    private String hiberDialect;
    @Value("${db.show.sql}")
    private boolean showSql;
    @Value("${db.hibernate.use.unicode}")
    private boolean useUnicode;
    @Value("${db.hibernate.order.inserts}")
    private boolean orderInserts;
    @Value("${db.hibernate.order.updates}")
    private boolean orderUpdates;
    @Value("${dm.conn.max.life}")
    private long connTimeOut;
    @Value("${hibernate.search.default.indexmanager}")
    private String indexManager;
    @Value("${hibernate.search.default.elasticsearch.host}")
    private String elasticHost;
    @Value("${hibernate.search.default.elasticsearch.username}")
    private String elasticLogin;
    @Value("${hibernate.search.default.elasticsearch.password}")
    private String elasticPwd;
    @Value("${hibernate.search.default.elasticsearch.index_schema_management_strategy}")
    private String indexSchemaStrategy;
    @Value("${hibernate.search.default.elasticsearch.discovery.refresh_interval}")
    private String discoveryRefreshInterval;
    @Value("${hibernate.search.default.elasticsearch.index_management_wait_timeout}")
    private String indexManageWait;
    @Value("${hibernate.search.default.elasticsearch.required_index_status}")
    private String elasticRequiredIndexStatus;

    @Bean(initMethod = "migrate")
    Flyway flyway() {
        Flyway flyway = new Flyway();
        flyway.setBaselineOnMigrate(true);
        flyway.setLocations("classpath:/db/");
        flyway.setDataSource(dataSource());
        return flyway;
    }

    @Bean
    @Profile("prod")
    public DataSource dataSource() {
        log.debug("init DataSource ");
        //configuration
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setDriverClassName(driverClassName);
        hikariConfig.setJdbcUrl(jdbcUrl);
        hikariConfig.setUsername(dbuser);
        hikariConfig.setPassword(dbpassword);
        hikariConfig.setConnectionTimeout(connTimeOut);
        hikariConfig.setMaximumPoolSize(poolSize);
        hikariConfig.setMinimumIdle(1);
        //datasource
        HikariDataSource dataSource = new HikariDataSource(hikariConfig);
        return dataSource;
    }

    @Bean
    @DependsOn("flyway")
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        vendorAdapter.setDatabase(Database.POSTGRESQL);
        vendorAdapter.setShowSql(showSql);
        LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
        factory.setJpaVendorAdapter(vendorAdapter);
        factory.setPackagesToScan("com.github.examples.repository", "com.github.examples.model");
        factory.setDataSource(dataSource());
        Properties props = new Properties();
        props.put("connection.pool_size", poolSize);
        props.put("hibernate.show_sql", showSql);
        props.put("hibernate.hbm2ddl.auto", hbm2ddlAuto);
        props.put("hibernate.dialect", hiberDialect);
        props.put("hibernate.connection.charSet", charsetEncoding);
        props.put("connection.characterEncoding", charsetEncoding);
        props.put("hibernate.connection.Useunicode", useUnicode);
        props.put("hibernate.jdbc.batch_size", batchSize);
        props.put("hibernate.order_inserts", orderInserts);
        props.put("hibernate.order_updates", orderUpdates);
        props.put("hibernate.search.default.indexmanager", indexManager);
        props.put("hibernate.search.default.elasticsearch.host", elasticHost);
        props.put("hibernate.search.default.elasticsearch.username", elasticLogin);
        props.put("hibernate.search.default.elasticsearch.password", elasticPwd);
        props.put("hibernate.search.default.elasticsearch.index_schema_management_strategy", indexSchemaStrategy);
        props.put("hibernate.search.default.elasticsearch.discovery.refresh_interval", discoveryRefreshInterval);
        props.put("hibernate.search.default.elasticsearch.index_management_wait_timeout", indexManageWait);
        props.put("hibernate.search.default.elasticsearch.required_index_status", elasticRequiredIndexStatus);
        props.put("hibernate.format_sql", true);
        props.put("hibernate.use_sql_comments", true);
        factory.setJpaProperties(props);
        factory.afterPropertiesSet();
        return factory;
    }

    @Bean
    @DependsOn("entityManagerFactory")
    @Transactional
    public FullTextEntityManager fullTextEntityManager() throws InterruptedException {
        EntityManager entityManager = entityManagerFactory().getObject().createEntityManager();
        FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(entityManager);
        fullTextEntityManager.createIndexer().startAndWait();
        return fullTextEntityManager;
    }

    @Bean(name = "transactionManager")
    public JpaTransactionManager transactionManager() {
        JpaTransactionManager txManager = new JpaTransactionManager();
        txManager.setEntityManagerFactory(entityManagerFactory().getObject());
        return txManager;
    }
}
