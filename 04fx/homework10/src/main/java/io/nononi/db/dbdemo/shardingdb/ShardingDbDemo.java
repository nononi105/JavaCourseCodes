package io.nononi.db.dbdemo.shardingdb;

import java.sql.*;
import java.util.Random;

/**
 * sharding_db demo
 */
public class ShardingDbDemo {
    public static void main(String[] args) throws Exception {
        Connection conn = null;
        Random random = new Random();
        try {
            conn = getConn();
            conn.setAutoCommit(false);
            System.out.println("开始查询：");
            select(conn);
            insert(conn, random.nextInt(100000), "OK");
            System.out.println("插入后查询：");
            select(conn);
            update(conn,  724643077406830593L, "FAIL");
            System.out.println("更新后查询：");
            select(conn);
            delete(conn, 724643077406830593L);
            System.out.println("删除后查询：");
            select(conn);
            conn.commit();

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
        String url = "jdbc:mysql://localhost:3307/sharding_db";
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

    private static void select(Connection connection) throws SQLException {
        String sql = "SELECT * FROM `t_order`;";
        PreparedStatement stmt = connection.prepareStatement(sql);
        ResultSet resultSet = stmt.executeQuery();
        while (resultSet.next()) {
            System.out.print(resultSet.getLong("order_id") + " ");
            System.out.print(resultSet.getInt("user_id") + " ");
            System.out.println(resultSet.getString("status") + " ");
        }
        System.out.println("");
    }

    private static int insert(Connection connection, int user_id, String status) throws SQLException {
        String sql = "INSERT INTO `t_order` (user_id, status) VALUES(?, ?);";
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setInt(1,user_id);
        stmt.setString(2,status);
        return stmt.executeUpdate();

    }

    private static int update(Connection connection, long order_id, String status) throws SQLException {
        String sql = "UPDATE `t_order` SET status = ? WHERE order_id = ?;";
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setString(1,status);
        stmt.setLong(2,order_id);
        return stmt.executeUpdate();
    }

    private static int delete(Connection connection, long order_id) throws SQLException {


        String sql = "DELETE FROM `t_order` WHERE order_id=? ;";
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setLong(1,order_id);
        return stmt.executeUpdate();
    }

}
