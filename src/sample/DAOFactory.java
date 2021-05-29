package sample;

import sample.dao.*;

public class DAOFactory {
    CategoryDAO categoryDAO = new CategoryDAO();
    CouponDAO couponDAO = new CouponDAO();
    DishDAO dishDAO = new DishDAO();
    OrderDAO orderDAO = new OrderDAO();
    OrderPositionDAO orderPositionDAO = new OrderPositionDAO();
    UserDAO userDAO = new UserDAO();

    private static final DAOFactory instance = new DAOFactory();

    public static DAOFactory getInstance() {
        return instance;
    }

    public DAO getDAO(String type) {
        return switch (type) {
            case "Category" -> categoryDAO;
            case "Coupon" -> couponDAO;
            case "Dish" -> dishDAO;
            case "Order" -> orderDAO;
            case "OrderPosition" -> orderPositionDAO;
            case "User" -> userDAO;
            default -> null;
        };
    }
}
