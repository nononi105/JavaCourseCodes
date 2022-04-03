package io.nononi.db.dbdemo.jdbc;

import java.sql.*;

/**
 * JDBC增删改查，开启事务
 */
public class JdbcTransactionDemo {
    public static void main(String[] args) throws Exception {
        Connection conn = null;
        try{
            conn = getConn();
            conn.setAutoCommit(false);
            System.out.println("开始查询：");
            select(conn);
            insert(conn,20,"Amy",1234);
            System.out.println("插入后查询：");
            select(conn);
            update(conn,20,"Bob",1111);
            System.out.println("更新后查询：");
            select(conn);
            delete(conn,20);
            System.out.println("删除后查询：");
            select(conn);
            conn.commit();

        }catch (SQLException e){
            e.printStackTrace();
            try {
               conn.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }finally {
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
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM user");
        while (resultSet.next()){
            System.out.print(resultSet.getInt("id")+ " ");
            System.out.print(resultSet.getString("name")+ " ");
            System.out.println(resultSet.getInt("pwd")+ " ");
        }
        System.out.println("");
    }

    private static boolean insert(Connection connection,int id, String name, int pwd) throws SQLException {
        Statement statement = connection.createStatement();
        String sql = "INSERT INTO `user` (id, name, pwd) VALUES("+ id+ ",'"+ name +"'," +pwd +");";
        return statement.execute(sql);

    }

    private static boolean update(Connection connection,int id, String name, int pwd) throws SQLException {
        Statement statement = connection.createStatement();
        String sql = "UPDATE `user` SET name ='"+name+"'," +"pwd = "+ pwd+" WHERE id =" +pwd + ";";
        return statement.execute(sql);
    }

    private static int delete(Connection connection, int pwd) throws SQLException {
        Statement statement = connection.createStatement();
        String sql = "DELETE FROM `user` WHERE id=" +pwd + ";";
        return statement.executeUpdate(sql);
    }

}
