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
     * saves coupon with given address id
     * @param coupon to be saved
     * @param addressid of address
     * @throws SQLException sql exception
     */
    @Override
    public void saveByAddressId(Coupon coupon, int addressid) throws SQLException {
            PreparedStatement prep = connection.prepareStatement("INSERT INTO Coupon (value, addressid) VALUES (?,?);");
            prep.setDouble(1, coupon.getValue());
            prep.setInt(2, addressid);
            prep.execute();
    }

    /**
     * returns coupon by address id
     * @param addressid to find coupon
     * @return found coupon or null if coupon was already used
     * @throws SQLException sql exception
     */
    @Override
    public Optional<Coupon> getCouponByAddressId(int addressid) throws SQLException {
            ResultSet rs = connection.createStatement().executeQuery("SELECT * FROM Coupon WHERE addressid=" + addressid);
            if (rs.first())
                // When coupon exists
                return Optional.of(new Coupon(
                        rs.getInt("id"),
                        rs.getDouble("value")
                ));
            else return Optional.empty();
    }

    /**
     * deletes coupon by its id when user uses coupon for discount
     * @param id of coupon
     * @throws SQLException sql exception
     */
    @Override
    public void delete(int id) throws SQLException {
            connection.createStatement().execute("DELETE FROM Coupon WHERE id=" + id);
    }

}
