package io.nononi.dynamicdb.datasource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class DynamicDataSourceConfig {

    @Bean(name = "master")
    public DataSource masterDatasource(){
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("com.mysql.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql://localhost:3316/db");
        dataSource.setUsername("root");
        dataSource.setPassword("");
        return dataSource;
    }

    @Bean(name = "slave")
    public DataSource slaveDatasource(){
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("com.mysql.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql://localhost:3326/db");
        dataSource.setUsername("root");
        dataSource.setPassword("");
        return dataSource;
    }

    @Bean
    @Primary
    public DynamicDataSource dataSource(@Qualifier("master") DataSource masterDatasource, @Qualifier("slave") DataSource slaveDatasource){
        Map<Object, Object> targetDataSources = new HashMap<>();
        targetDataSources.put("master", masterDatasource);
        targetDataSources.put("slave",slaveDatasource);
        return new DynamicDataSource(masterDatasource,targetDataSources);
    }
}
