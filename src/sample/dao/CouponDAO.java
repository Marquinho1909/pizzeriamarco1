package sample.dao;

import sample.JDBCClient;
import sample.dto.Coupon;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class CouponDAO {
    Connection connection;

    public CouponDAO() {
        connection = new JDBCClient().connection;
    }

    public Optional<Coupon> get(int id) {
        return Optional.empty();
    }

    public List<Coupon> getAll() {
        return null;
    }

    public int save(Coupon coupon) {
        return 0;
    }

    public boolean saveByAddressId(Coupon coupon, int addressid) {
        try {
            PreparedStatement prep = connection.prepareStatement("INSERT INTO Coupon (value, addressid) VALUES (?,?);");
            prep.setDouble(1, coupon.getValue());
            prep.setInt(2, addressid);
            prep.execute();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Coupon getCouponByAddressId(int addressid) {
        try {
            ResultSet rs = connection.createStatement().executeQuery("SELECT * FROM Coupon WHERE addressid=" + addressid);
            if (rs.first())
                // When coupon exists
                return new Coupon(
                        rs.getInt("id"),
                        rs.getDouble("value")
                );
            else return null;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean delete(int id) {
        try {
            connection.createStatement().execute("DELETE FROM Coupon WHERE id=" + id);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

}
