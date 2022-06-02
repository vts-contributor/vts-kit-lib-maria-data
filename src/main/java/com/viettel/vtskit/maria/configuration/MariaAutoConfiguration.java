package com.viettel.vtskit.maria.configuration;

import com.viettel.vtskit.maria.repository.CommonMariaRepository;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseProperties;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateProperties;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.core.Ordered;

@Configuration
@AutoConfigureOrder(Ordered.HIGHEST_PRECEDENCE)
@Import(CommonMariaRepository.class)
public class MariaAutoConfiguration {

    @Bean
    @Primary
    @ConfigurationProperties("vtskit.mariadb.datasource")
    public DataSourceProperties dataSourceProperties() {
        DataSourceProperties dataSourceProperties = new DataSourceProperties();
        dataSourceProperties.setDriverClassName("org.mariadb.jdbc.Driver");
        return dataSourceProperties;
    }

    @Bean
    @Primary
    @ConfigurationProperties("vtskit.mariadb.jpa.hibernate")
    public HibernateProperties hibernateProperties() {
        HibernateProperties hibernateProperties = new HibernateProperties();
        hibernateProperties.setDdlAuto("none");
        return hibernateProperties;
    }

    @Bean
    @Primary
    @ConfigurationProperties("vtskit.mariadb.jpa")
    public JpaProperties jpaProperties() {
        JpaProperties jpaProperties = new JpaProperties();
        jpaProperties.setShowSql(false);
        jpaProperties.setOpenInView(false);
        jpaProperties.setGenerateDdl(false);
        return jpaProperties;
    }

    @Bean
    @Primary
    @ConfigurationProperties("vtskit.mariadb.liquibase")
    public LiquibaseProperties liquibaseProperties() {
        LiquibaseProperties liquibaseProperties = new LiquibaseProperties();
        liquibaseProperties.setChangeLog("liquibase/master.xml");
        return liquibaseProperties;
    }

    @Bean
    @ConfigurationProperties("vtskit.mariadb.datasource.hikari")
    public HikariDataSource dataSource(DataSourceProperties properties) {
        HikariDataSource dataSource = properties.initializeDataSourceBuilder().type(HikariDataSource.class).build();
        dataSource.setAutoCommit(false);
        dataSource.setConnectionTimeout(60000);
        dataSource.setConnectionTestQuery("select 1");
        dataSource.setValidationTimeout(60000);
        return dataSource;
    }

}
