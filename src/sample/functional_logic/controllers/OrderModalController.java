package sample.functional_logic.controllers;

import sample.data_logic.DAOFactory;
import sample.data_logic.UserSessionSingleton;
import sample.data_logic.dao.CouponDAO;
import sample.data_logic.dao.OrderDAO;
import sample.data_logic.dao.UserDAO;
import sample.data_logic.dto.Coupon;
import sample.data_logic.dto.Customer;
import sample.data_logic.dto.Order;
import sample.data_logic.dto.OrderPosition;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class OrderModalController {
    public List<OrderPosition> dishesInCart = new ArrayList<>();
    public Coupon coupon;

    public OrderDAO orderDAO;
    public UserDAO userDAO;
    public CouponDAO couponDAO;

    public OrderModalController() {
        orderDAO = (OrderDAO) DAOFactory.getInstance().getDAO("Order");
        userDAO = (UserDAO) DAOFactory.getInstance().getDAO("User");
        couponDAO = (CouponDAO) DAOFactory.getInstance().getDAO("Coupon");
    }

    /**
     * saves order in database, sets status accordingly and closes modal
     */
    public void order(boolean coupon) throws SQLException {
        if (orderDAO.save(
                new Order(
                        dishesInCart,
                        new Date(),
                        UserSessionSingleton.currentSession().getUser(),
                        coupon ? this.coupon.getValue() : 0
                )
        ) != 0) {
            if (coupon) {
                couponDAO.delete(this.coupon.getId());
                this.coupon = null;
            }
        } else
            throw new SQLException("ORDER COULD NOT BE CREATED OR NOT KEY WAS RETURNED PROPERLY");
    }

    public void fetchCoupon() throws SQLException {
        coupon = userDAO.getCustomerCoupon((Customer) UserSessionSingleton.currentSession().getUser()).orElse(null);
    }
}
