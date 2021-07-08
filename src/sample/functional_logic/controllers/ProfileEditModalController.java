package sample.functional_logic.controllers;

import sample.data_logic.DAOFactory;
import sample.data_logic.UserSessionSingleton;
import sample.data_logic.dao.UserDAO;
import sample.data_logic.dto.User;

import java.sql.SQLException;

public class ProfileEditModalController {
    UserDAO userDAO;

    public ProfileEditModalController() {
        userDAO = (UserDAO) DAOFactory.getInstance().getDAO("User");
    }

    public void saveChanges(int id, User user) throws SQLException {
        userDAO.update(id, user);
        UserSessionSingleton.currentSession().setUser(userDAO.get(id).orElseThrow());
    }
}
