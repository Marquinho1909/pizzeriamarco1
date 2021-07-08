package sample.functional_logic.controllers;

import sample.data_logic.DAOFactory;
import sample.data_logic.UserSessionSingleton;
import sample.data_logic.dao.UserDAO;
import sample.data_logic.dto.Admin;
import sample.data_logic.dto.Customer;
import sample.data_logic.dto.User;

import java.sql.SQLException;
import java.util.Optional;

public class LoginController {
    UserDAO userDAO;

    public LoginController() {
        userDAO = (UserDAO) DAOFactory.getInstance().getDAO("User");
    }

    /**
     * tries to login user with input, sets UserSession and redirects to fitting scene
     */
    public boolean login(String email, String password) throws SQLException {
        Optional<User> user = userDAO.getUserByEmailAndPassword(email, password);

        if (user.isPresent()) {
            UserSessionSingleton.currentSession().setUser(user.get());
            return true;
        }
        return false;
    }

    public boolean isUserAdmin() {
        return UserSessionSingleton.currentSession().getUser().getClass().equals(Admin.class);
    }

    public boolean isUserCustomer() {
        return UserSessionSingleton.currentSession().getUser().getClass().equals(Customer.class);
    }
}
