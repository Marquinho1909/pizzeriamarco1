package sample.functional_logic.controllers;

import resources.GUIController.AdminPageGUIController;
import sample.data_logic.DAOFactory;
import sample.data_logic.UserSessionSingleton;
import sample.data_logic.dao.DishDAO;
import sample.data_logic.dao.OrderDAO;
import sample.data_logic.dao.UserDAO;
import sample.data_logic.dto.*;
import java.sql.SQLException;
import java.util.*;

public class AdminPageController {

    public UserDAO userDAO;
    public OrderDAO orderDAO;
    public DishDAO dishDAO;

    public AdminPageController() {
        this.userDAO = (UserDAO) DAOFactory.getInstance().getDAO("User");
        this.orderDAO = (OrderDAO) DAOFactory.getInstance().getDAO("Order"); ;
        this.dishDAO = (DishDAO) DAOFactory.getInstance().getDAO("Dish"); ;
    }

    /**
     * logs out user
     */
    public void logout() {
        UserSessionSingleton.currentSession().cleanUserSession();
    }

    /**
     * deletes selected dish from dish table
     */
    public void deleteDish(int id) throws SQLException {
        dishDAO.delete(id);
    }

    /**
     * deletes all orders and their orderpositions
     */
    public void deleteOrderHistory() throws SQLException {
        orderDAO.deleteAll();
    }

    public List<Customer> getCustomers() throws SQLException {
        return userDAO.getAllCustomers();
    }

    public List<Order> getOrders() throws SQLException {
        return orderDAO.getAll();
    }

    public List<Dish> getDishes() throws SQLException {
        return dishDAO.getAll();
    }

    public void makeCustomerAdmin(int id) throws SQLException {
        userDAO.makeCustomerAdmin(id);
    }

    public boolean canDishBeDeleted(int id) throws SQLException {
        List<Order> orders = orderDAO.getAll();
        return orders.stream().noneMatch(o -> o.getOrderpositions().stream().anyMatch(p -> p.getDish().getId() == id));
    }

    public void setActivation(boolean activate, int id) throws SQLException {
        dishDAO.setActivation(activate, id);
    }
}
