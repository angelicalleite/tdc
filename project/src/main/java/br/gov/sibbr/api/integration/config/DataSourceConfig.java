package br.gov.sibbr.api.integration.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

@Configuration
public class DataSourceConfig {

    @Primary
    @Bean(name = "primaryDatasource")
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource primaryDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Autowired
    @Bean(name = "jdbcPrimary")
    public JdbcTemplate primaryJdbcTemplate(@Qualifier("primaryDatasource") DataSource dsPrimary) {
        return new JdbcTemplate(dsPrimary);
    }

    @Bean(name = "secundaryDatasource")
    @ConfigurationProperties(prefix = "spring.datasource.secundary")
    public DataSource secundaryDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Autowired
    @Bean(name = "jdbcSecundary")
    public JdbcTemplate secundaryJdbcTemplate(@Qualifier("secundaryDatasource") DataSource dsSecundary) {
        return new JdbcTemplate(dsSecundary);
    }

}
