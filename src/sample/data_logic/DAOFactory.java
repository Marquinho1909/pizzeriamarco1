package sample.data_logic;

import sample.data_logic.dao.*;

public class DAOFactory {
    CategoryDAO categoryDAO = new CategoryDAO();
    OrderPositionDAO orderPositionDAO = new OrderPositionDAO(categoryDAO);
    CouponDAO couponDAO = new CouponDAO();
    UserDAO userDAO = new UserDAO(couponDAO);
    DishDAO dishDAO = new DishDAO(categoryDAO);
    OrderDAO orderDAO = new OrderDAO(userDAO, orderPositionDAO);

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
