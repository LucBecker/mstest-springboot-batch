package com.lucbecker.msbatch;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.cloud.task.configuration.DefaultTaskConfigurer;
import org.springframework.cloud.task.configuration.TaskConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

@Configuration
public class DatasourceConfig {

    @Bean
    @Primary
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource springDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "appDatasource")
    @ConfigurationProperties(prefix = "app.datasource")
    public DataSource sourceDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean
    public TaskConfigurer taskConfigurer() {
        return new DefaultTaskConfigurer(springDataSource());
    }
}
