package sample.functional_logic.controllers;

import sample.data_logic.DAOFactory;
import sample.data_logic.UserSessionSingleton;
import sample.data_logic.dao.OrderDAO;
import sample.data_logic.dto.Order;

import java.sql.SQLException;
import java.util.List;

public class OrderHistoryModalController extends ModalController {
    OrderDAO orderDAO;

    public OrderHistoryModalController() {
        orderDAO = (OrderDAO) DAOFactory.getInstance().getDAO("Order");
    }

    public List<Order> getOrdersFromLoggedInUser() throws SQLException {
        return orderDAO.getAllByUserId(UserSessionSingleton.currentSession().getUser().getId());
    }

}
