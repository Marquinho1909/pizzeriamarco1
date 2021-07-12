package sample.data_logic.idao;

import sample.data_logic.dto.User;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface iUserDAO {
    Optional<User> get(int id) throws SQLException;

    List<User> getAll() throws SQLException;

    int save(User user) throws SQLException;

    void update(int id, User user) throws SQLException;
}
