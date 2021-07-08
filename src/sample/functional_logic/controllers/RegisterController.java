package sample.functional_logic.controllers;

import sample.data_logic.DAOFactory;
import sample.data_logic.UserSessionSingleton;
import sample.data_logic.dao.UserDAO;
import sample.data_logic.dto.Customer;

import java.sql.SQLException;

public class RegisterController {
    private UserDAO userDAO;

    public RegisterController() {
        userDAO = (UserDAO) DAOFactory.getInstance().getDAO("User");
    }

    /**
     * creates customer if form is valid and redirects to customer page
     *
     * @param actionEvent ae
     */
    public void register(Customer customer) throws SQLException {
        userDAO.save(customer);
        UserSessionSingleton.currentSession().setUser(userDAO.getUserByEmailAndPassword(customer.getEmail(), customer.getPassword()).orElseThrow());
    }

}
