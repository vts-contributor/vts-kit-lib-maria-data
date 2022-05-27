package com.viettel.vtskit.maria.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@AutoConfigureOrder(Ordered.HIGHEST_PRECEDENCE)
@EnableConfigurationProperties(MariaProperties.class)
public class MariaAutoConfiguration {

    private MariaProperties mariaProperties;

    @ConfigurationProperties(prefix = "spring.datasource")
    @Bean
    public DataSource getDataSource() {
        return DataSourceBuilder
                .create()
                .url(mariaProperties.getDatasourceUrl())
                .username(mariaProperties.getDatasourceUser())
                .password(mariaProperties.getDatasourcePass())
                .driverClassName("org.mariadb.jdbc.Driver")
                .build();
    }

    @Bean
    Properties additionalProperties() {
        Properties hibernateProperties = new Properties();

//        hibernateProperties.setProperty("hibernate.hbm2ddl.auto",
//                env.getProperty("mysql-hibernate.hbm2ddl.auto"));
//        hibernateProperties.setProperty("hibernate.dialect",
//                env.getProperty("mysql-hibernate.dialect"));
        hibernateProperties.setProperty("hibernate.show_sql", "true");
        hibernateProperties.setProperty("hibernate.hbm2ddl.auto", "true");
        return hibernateProperties;
    }

    @Autowired
    public void setMariaProperties(MariaProperties mariaProperties) {
        this.mariaProperties = mariaProperties;
    }
}
