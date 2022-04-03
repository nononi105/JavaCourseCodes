package io.nononi.db.dbdemo.jdbc;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Random;

/**
 * 使用Hikati连接池
 */
public class HIkariDemo {
    private static HikariDataSource dataSource;

    static {

        //初始化HikariConfig配置
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:mysql://localhost:3306/test");
        config.setUsername("root");
        config.setPassword("root");
        config.setDriverClassName("com.mysql.jdbc.Driver");
        config.setMaximumPoolSize(20);
        config.setMinimumIdle(10);
        config.setAutoCommit(false);
        config.setConnectionTimeout(30000);
        //初始化HikariDataSource
        dataSource = new HikariDataSource(config);
    }

    public static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    public static void main(String[] args) throws SQLException {
        Random random = new Random();
        Connection conn = null;
        try {
            conn = getConnection();
            conn.setAutoCommit(false);
            String sql = "INSERT INTO `user` (id, name, pwd) VALUES(?, ?, ?);";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            long begin = System.currentTimeMillis();
            for (int i = 0; i < 100000; i++) {
                preparedStatement.setInt(1, i + 1);
                preparedStatement.setString(2, "name_" + i);
                preparedStatement.setInt(3, random.nextInt(9999));
                preparedStatement.addBatch();
                if ((i + 1) % 200 == 0) {
                    preparedStatement.executeBatch();
                    preparedStatement.clearBatch();
                }
            }
            if (100000 % 200 != 0) {
                preparedStatement.executeBatch();
                preparedStatement.clearBatch();
            }
            conn.commit();
            long end = System.currentTimeMillis();
            System.out.println("Time: " + (end - begin));
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                conn.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } finally {
            conn.close();

        }
    }
}
