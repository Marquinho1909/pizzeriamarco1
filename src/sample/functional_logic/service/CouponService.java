package sample.functional_logic.service;

import sample.data_logic.DAOFactory;
import sample.data_logic.dao.CouponDAO;
import sample.data_logic.idao.iCouponDAO;
import sample.functional_logic.ParentService;

import java.sql.SQLException;

public class CouponService extends ParentService {
    public iCouponDAO couponDAO;

    public CouponService() {
        this.couponDAO = (CouponDAO) DAOFactory.getInstance().getDAO("Coupon");
    }

    public void delete(int id) throws SQLException {
        couponDAO.delete(id);
        notifyObservers();
    }

}
