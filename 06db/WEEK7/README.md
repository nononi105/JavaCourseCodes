### 作业2 
---
#### 按自己设计的表结构，插入 100 万订单模拟数据，测试不同方式的插入效率

+ 一个事务下一次性插入1000000条数据，耗时269.519秒

+ 一个事务下利用preparedstatement的Add Batch，200条提交一次，耗时30.657秒（坑：要添加参数rewriteBatchedStatements=true，让mysql开启批处理的支持） 
+ 不开事务下利用preparedstatement的Add Batch，200条执行一次executeBatch - 218.40秒
+ 利用load data 命令导入csv文件，耗时24.67秒


### 作业9
---
读写分离 - 动态切换数据源版本 1.0
实现代码：https://github.com/nononi105/JavaCourseCodes/tree/ca0240d737d54121d7e50a5edd45055c69f18a7b/06db/work9ofweek7/src/main/java/io/nononi/dynamicdb

### 作业10
---
读写分离 - 数据库框架版本 2.0
实现代码：https://github.com/nononi105/JavaCourseCodes/blob/main/06db/work10ofweek7/src/main/java/io/nononi/shardingspheredemo/datasource/DataSourceConfig.java

