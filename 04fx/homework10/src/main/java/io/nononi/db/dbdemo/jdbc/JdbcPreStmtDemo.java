package io.nononi.db.dbdemo.jdbc;

import java.sql.*;

/**
 * PrepareStatement 方式改造
 */
public class JdbcPreStmtDemo {
    public static void main(String[] args) throws Exception {
        Connection conn = null;
        try {
            conn = getConn();
            conn.setAutoCommit(false);
            System.out.println("开始查询：");
            select(conn);
            insert(conn, 20, "Amy", 1234);
            System.out.println("插入后查询：");
            select(conn);
            update(conn, 20, "Bob", 1111);
            System.out.println("更新后查询：");
            select(conn);
            delete(conn, 20);
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

    private static void select(Connection connection) throws SQLException {
        String sql = "SELECT * FROM user;";
        PreparedStatement stmt = connection.prepareStatement(sql);
        ResultSet resultSet = stmt.executeQuery();
        while (resultSet.next()) {
            System.out.print(resultSet.getInt("id") + " ");
            System.out.print(resultSet.getString("name") + " ");
            System.out.println(resultSet.getInt("pwd") + " ");
        }
        System.out.println("");
    }

    private static int insert(Connection connection, int id, String name, int pwd) throws SQLException {
        String sql = "INSERT INTO `user` (id, name, pwd) VALUES(?, ?, ?);";
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setInt(1,id);
        stmt.setString(2,name);
        stmt.setInt(3,pwd);
        return stmt.executeUpdate();

    }

    private static int update(Connection connection, int id, String name, int pwd) throws SQLException {
        String sql = "UPDATE `user` SET name = ?, pwd = ? WHERE id = ?;";
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setString(1,name);
        stmt.setInt(2,pwd);
        stmt.setInt(3,id);
        return stmt.executeUpdate();
    }

    private static int delete(Connection connection, int id) throws SQLException {


        String sql = "DELETE FROM `user` WHERE id=?;";
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setInt(1,id);
        return stmt.executeUpdate();
    }

}
