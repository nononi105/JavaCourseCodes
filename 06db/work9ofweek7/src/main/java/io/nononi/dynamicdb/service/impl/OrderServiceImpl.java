package io.nononi.dynamicdb.service.impl;

import io.nononi.dynamicdb.annotation.ReadOnly;
import io.nononi.dynamicdb.datasource.DynamicDataSource;
import io.nononi.dynamicdb.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class OrderServiceImpl implements OrderService {

    @Autowired
    private DynamicDataSource dataSource;

    @Override
    public void insert(int id, String name) {
        try {
            Connection conn = dataSource.getConnection();
            System.out.println(conn.getMetaData().getURL());
            String sql = "INSERT INTO `order` (id,name) VALUES (?,?);";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1,id);
            stmt.setString(2,name);
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    @ReadOnly
    public void query() {
        String sql = "SELECT * FROM `order`;";
        try {
            Connection conn = dataSource.getConnection();
            System.out.println(conn.getMetaData().getURL());
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet resultSet = stmt.executeQuery();
            while (resultSet.next()) {
                System.out.print(resultSet.getInt("id") + " ");
                System.out.println(resultSet.getString("name") + " ");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
