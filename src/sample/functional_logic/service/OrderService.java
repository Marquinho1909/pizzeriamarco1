package sample.functional_logic.service;

import sample.data_logic.DAOFactory;
import sample.data_logic.dao.DAO;
import sample.data_logic.dao.OrderDAO;
import sample.data_logic.dto.Order;
import sample.data_logic.dto.User;
import sample.functional_logic.ParentService;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OrderService extends ParentService {

    private List<Order> orders;

    OrderDAO orderDAO;

    public OrderService() {
        this.orderDAO = (OrderDAO) DAOFactory.getInstance().getDAO("Order");

        load();
    }

    public List<Order> get() {
        return orders;
    }

    public void deleteAll() throws SQLException {
        orderDAO.deleteAll();
        load();
        notifyObservers();
    }

    public void save(Order order) throws SQLException {
        orderDAO.save(order);
        load();
        notifyObservers();
    }

    public void load() {
        try {
            orders = orderDAO.getAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
