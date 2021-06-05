package sample.data_logic.idao;

import sample.data_logic.dto.Dish;

import java.sql.SQLException;
import java.util.List;

public interface iDishDAO {
    List<Dish> getAll() throws SQLException;
    List<Dish> getAllActive() throws SQLException;
    int save(Dish dish) throws SQLException;
    void delete(int id) throws SQLException;
    void setActivation(boolean active, int id) throws SQLException;
}
