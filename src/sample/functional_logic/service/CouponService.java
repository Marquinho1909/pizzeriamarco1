package sample.functional_logic.service;

import sample.data_logic.DAOFactory;
import sample.data_logic.dao.CouponDAO;
import sample.data_logic.dao.DAO;
import sample.functional_logic.ParentService;

import java.sql.SQLException;

public class CouponService extends ParentService {
    CouponDAO couponDAO;

    public CouponService() {
        this.couponDAO = (CouponDAO) DAOFactory.getInstance().getDAO("Coupon");
    }

    public void delete(int id) throws SQLException {
        couponDAO.delete(id);
        notifyObservers();
    }

}
