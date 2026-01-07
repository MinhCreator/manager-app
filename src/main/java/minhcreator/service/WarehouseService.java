package minhcreator.service;

import minhcreator.functional.database.DB;
import minhcreator.functional.location.TimeManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 *
 * @author MinhCreatorVN
 * @version 1.0 PreTest
 */
public class WarehouseService {
    private Connection conn;
    private PreparedStatement ps;

    public WarehouseService() {
    }

    public void updateQuantityService(String tableName, String id, int AmountValues) {
        String sql = "UPDATE " + tableName + " SET amount=? WHERE id=?";
        try {
            conn = DB.getConnection();
            if (conn == null) return;
            ps = conn.prepareStatement(sql);
            ps.setInt(1, AmountValues);
            ps.setString(2, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void insertSaleTable(String saleTable, String id, String category, String ProductName, Double price, Double salePrice, int Amounts) {
//  "INSERT INTO " + get_user().getYour_inventory() + " (id, name, price, sellprice, amount) VALUES (?, ?, ?, ?, ?)";
        TimeManager time = new TimeManager();
        String sql = "INSERT INTO " + saleTable + " (id , name, category, date, price, sellprice, amount) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try {
            conn = DB.getConnection();
            if (conn == null) return;
            ps = conn.prepareStatement(sql);
            ps.setString(1, id);
            ps.setString(2, ProductName);
            ps.setString(3, category);
            ps.setString(4, time.TimeNowFormat("yyyy-MM-dd"));
            ps.setDouble(5, price);
            ps.setDouble(6, salePrice);
            ps.setInt(7, Amounts);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}