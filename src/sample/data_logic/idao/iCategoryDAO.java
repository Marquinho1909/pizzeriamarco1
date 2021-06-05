package sample.data_logic.idao;

import sample.data_logic.dto.Category;

import java.sql.SQLException;
import java.util.List;

public interface iCategoryDAO {
    List<Category> getAll() throws SQLException;
    int save(Category category) throws SQLException;
    List<Category> getAllByDishId(int dishid) throws SQLException;
    void deleteAll(List<Category> categories) throws SQLException;
}
