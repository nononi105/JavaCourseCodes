package io.nononi.shardingspheredemo.datasource;

import org.apache.commons.dbcp2.BasicDataSource;

import org.apache.shardingsphere.api.config.masterslave.MasterSlaveRuleConfiguration;
import org.apache.shardingsphere.shardingjdbc.api.MasterSlaveDataSourceFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@Configuration
public class DataSourceConfig {

    @Bean
    public DataSource dataSource() throws SQLException {
        // Configure actual data sources
        Map<String, DataSource> dataSourceMap = new HashMap<>();

        // Configure master data source
        BasicDataSource masterDataSource = new BasicDataSource();
        masterDataSource.setDriverClassName("com.mysql.jdbc.Driver");
        masterDataSource.setUrl("jdbc:mysql://localhost:3316/db");
        masterDataSource.setUsername("root");
        masterDataSource.setPassword("");
        dataSourceMap.put("ds_master", masterDataSource);

        // Configure the first slave data source
        BasicDataSource slaveDataSource1 = new BasicDataSource();
        slaveDataSource1.setDriverClassName("com.mysql.jdbc.Driver");
        slaveDataSource1.setUrl("jdbc:mysql://localhost:3326/db");
        slaveDataSource1.setUsername("root");
        slaveDataSource1.setPassword("");
        dataSourceMap.put("ds_slave", slaveDataSource1);


        // Configure read-write split rule
        MasterSlaveRuleConfiguration masterSlaveRuleConfig = new MasterSlaveRuleConfiguration("ds_master_slave", "ds_master", Arrays.asList("ds_slave0"));

        // Get data source
        DataSource dataSource = MasterSlaveDataSourceFactory.createDataSource(dataSourceMap, masterSlaveRuleConfig, new Properties());
        return dataSource;
    }
}
