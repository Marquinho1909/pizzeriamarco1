package sample.functional_logic.service;

import sample.data_logic.DAOFactory;
import sample.data_logic.dao.UserDAO;
import sample.data_logic.dto.User;
import sample.data_logic.idao.iUserDAO;
import sample.functional_logic.ParentService;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserService extends ParentService {
    public iUserDAO userDAO;

    private List<User> users = new ArrayList<>();

    public UserService() {
        this.userDAO = (UserDAO) DAOFactory.getInstance().getDAO("User");
        load();
    }

    public List<User> get() {
        return users;
    }

    public void save(User user) throws SQLException {
        userDAO.save(user);
        load();
        notifyObservers();
    }

    public void update(int id, User user) throws SQLException {
        userDAO.update(id, user);
        load();
        notifyObservers();
    }

    public void load() {
        try {
            users = userDAO.getAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
