package sample.data_logic.dao;

import sample.JDBCClient;
import sample.data_logic.dto.Coupon;
import sample.data_logic.idao.iCouponDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

/**
 * DAO for Coupon
 */
public class CouponDAO extends DAO implements iCouponDAO {
    Connection connection;

    public CouponDAO() { connection = new JDBCClient().connection; }

    /**
     * deletes coupon by its id when user uses coupon for discount
     * @param id of coupon
     * @throws SQLException sql exception
     */
    @Override
    public void delete(int id) throws SQLException {
        System.out.println(id);
        connection.createStatement().execute("DELETE FROM Coupon WHERE couponid=" + id);
    }

}
