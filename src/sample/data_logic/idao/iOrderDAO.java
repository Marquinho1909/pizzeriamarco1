package sample.data_logic.idao;

import sample.data_logic.dto.Order;

import java.sql.SQLException;
import java.util.List;

public interface iOrderDAO {
    List<Order> getAll() throws SQLException;
    int save(Order order) throws SQLException;
    List<Order> getAllByUserId(int userid) throws SQLException;
    void deleteAll() throws SQLException;
}
