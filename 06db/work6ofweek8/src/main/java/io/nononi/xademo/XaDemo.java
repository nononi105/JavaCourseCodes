package io.nononi.xademo;

import org.apache.shardingsphere.driver.api.yaml.YamlShardingSphereDataSourceFactory;
import org.apache.shardingsphere.transaction.core.TransactionType;
import org.apache.shardingsphere.transaction.core.TransactionTypeHolder;

import javax.sql.DataSource;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class XaDemo {
    public static void main(String[] args) throws IOException, SQLException {
        DataSource dataSource = getShardingDatasource();
        cleanupData(dataSource);

        TransactionTypeHolder.set(TransactionType.XA);

        Connection conn = dataSource.getConnection();
        String sql = "insert into t_order (user_id, order_id) VALUES (?, ?);";

        System.out.println("First XA Start insert data");
        try (PreparedStatement statement = conn.prepareStatement(sql)) {
            conn.setAutoCommit(false);
            //会插入到ds1的t_order_1;
            statement.setLong(1, 1);
            statement.setLong(2, 1);
            statement.executeUpdate();
            //会插入到ds0的t_order_0;
            statement.setLong(1, 2);
            statement.setLong(2, 2);
            statement.executeUpdate();

            conn.commit();
        }

        System.out.println("First XA insert successful");

        System.out.println("Second XA Start insert data");
        try (PreparedStatement statement = conn.prepareStatement(sql)) {
            conn.setAutoCommit(false);
            //会插入到ds1的t_order_1;
            //回滚后应该两个数据库中都没有插入
            statement.setLong(1, 3);
            statement.setLong(2, 3);
            statement.executeUpdate();
            //会插入到ds0的t_order_0;
            statement.setLong(1, 4);
            statement.setLong(2, 4);
            statement.executeUpdate();
            conn.rollback();

        } finally {
            conn.close();
        }
        System.out.println("Second XA insert rollback");
    }

    private static void cleanupData(DataSource dataSource) {
        System.out.println("Delete all Data");
        try (Connection conn = dataSource.getConnection(); Statement statement = conn.createStatement()) {
            statement.execute("delete from t_order;");
            conn.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("Delete all Data successful");
    }

    static private DataSource getShardingDatasource() throws IOException, SQLException {
        String fileName = "C:\\Users\\NoNo_Ni\\Desktop\\Java进阶训练营\\javacourse\\06db\\work6ofweek8\\src\\main\\resources\\sharding-databases-tables.yaml";
        File yamlFile = new File(fileName);
        return YamlShardingSphereDataSourceFactory.createDataSource(yamlFile);
    }
}
