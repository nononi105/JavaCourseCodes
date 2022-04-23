### 作业2 
---
#### 设计对前面的订单表数据进行水平分库分表，拆分 2 个库，每个库 16 张表。并在新结构在演示常见的增删改查操作。

实现代码：https://github.com/nononi105/JavaCourseCodes/blob/main/04fx/homework10/src/main/java/io/nononi/db/dbdemo/shardingdb/ShardingDbDemo.java

sql：CREATE TABLE t_order (order_id BIGINT NOT NULL AUTO_INCREMENT, user_id INT NOT NULL, status VARCHAR(50), PRIMARY KEY(order_id));

配置：
server.yaml
```
authentication:
 users:
   root:
     password: root

props:
 max-connections-size-per-query: 1
 acceptor-size: 16  # The default value is available processors count * 2.
 executor-size: 16  # Infinite by default.
 proxy-frontend-flush-threshold: 128  # The default value is 128.
 proxy-transaction-type: LOCAL
 proxy-opentracing-enabled: false
 proxy-hint-enabled: false
 query-with-cipher-column: true
 sql-show: true
 check-table-metadata-enabled: false
```

config-sharding.yaml

```
schemaName: sharding_db

dataSourceCommon:
 username: root
 password: 
 connectionTimeoutMilliseconds: 30000
 idleTimeoutMilliseconds: 60000
 maxLifetimeMilliseconds: 1800000
 maxPoolSize: 5
 minPoolSize: 1
 maintenanceIntervalMilliseconds: 30000

dataSources:
 ds_0:
   url: jdbc:mysql://127.0.0.1:3316/demo_ds_0?serverTimezone=UTC&useSSL=false
 ds_1:
   url: jdbc:mysql://127.0.0.1:3326/demo_ds_1?serverTimezone=UTC&useSSL=false

rules:
- !SHARDING
 tables:
   t_order:
     actualDataNodes: ds_${0..1}.t_order_${0..15}
     tableStrategy:
       standard:
         shardingColumn: order_id
         shardingAlgorithmName: t_order_inline
     keyGenerateStrategy:
       column: order_id
       keyGeneratorName: snowflake
 defaultDatabaseStrategy:
   standard:
     shardingColumn: user_id
     shardingAlgorithmName: database_inline
 defaultTableStrategy:
   none:
 
 shardingAlgorithms:
   database_inline:
     type: INLINE
     props:
       algorithm-expression: ds_${user_id % 2}
   t_order_inline:
     type: INLINE
     props:
       algorithm-expression: t_order_${order_id % 16}
 keyGenerators:
   snowflake:
     type: SNOWFLAKE
     props:
       worker-id: 123
```


### 作业6 基于ShardingSphere 的 Atomikos XA 实现一个简单的分布式事务应用 demo
---
实现代码：https://github.com/nononi105/JavaCourseCodes/tree/main/06db/work6ofweek8/src/main

