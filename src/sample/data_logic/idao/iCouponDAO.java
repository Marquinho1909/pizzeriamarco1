package sample.data_logic.idao;

import sample.data_logic.dto.Coupon;

import java.sql.SQLException;
import java.util.Optional;

public interface iCouponDAO {
    void saveByAddressId(Coupon coupon, int addressid) throws SQLException;
    Optional<Coupon> getCouponByAddressId(int addressid) throws SQLException;
    void delete(int id) throws SQLException;
}
