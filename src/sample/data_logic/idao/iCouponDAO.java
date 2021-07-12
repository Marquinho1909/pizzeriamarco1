package sample.data_logic.idao;

import sample.data_logic.dto.Coupon;

import java.sql.SQLException;
import java.util.Optional;

public interface iCouponDAO {
    void delete(int id) throws SQLException;
}
