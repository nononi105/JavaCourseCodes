package io.nononi.db.dbdemo.jdbc;

import java.sql.*;
import java.util.Random;

/**
 * 批处理模式
 */
public class JdbcBatchDemo {
    public static void main(String[] args) throws Exception {
        Random random = new Random();
        Connection conn = null;
        try {
            conn = getConn();
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

    private static Connection getConn() {
        String driver = "com.mysql.jdbc.Driver";
        String url = "jdbc:mysql://localhost:3306/test";
        String username = "root";
        String password = "root";
        Connection conn = null;
        try {
            Class.forName(driver); //classLoader,加载对应驱动
            conn = (Connection) DriverManager.getConnection(url, username, password);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }

}
