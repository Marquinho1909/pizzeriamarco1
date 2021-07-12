package sample.data_logic.idao;

import sample.data_logic.dto.Dish;

import java.sql.SQLException;
import java.util.List;

public interface iDishDAO {
    List<Dish> getAll() throws SQLException;
    int save(Dish dish) throws SQLException;
    void delete(int id) throws SQLException;
    void update(int id, Dish dish) throws SQLException;
}
