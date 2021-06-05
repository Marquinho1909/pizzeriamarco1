package sample.data_logic.dao;

import sample.JDBCClient;
import sample.data_logic.dto.Category;
import sample.data_logic.idao.iCategoryDAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO for Category
 */
public class CategoryDAO extends DAO implements iCategoryDAO {
    Connection connection;

    public CategoryDAO() { connection = new JDBCClient().connection; }

    /**
     * gets all categories
     * @return list of found categories
     * @throws SQLException sql exception
     */
    @Override
    public List<Category> getAll() throws SQLException {
            ResultSet rs = connection.createStatement().executeQuery("SELECT * FROM Category");
            List<Category> categories = new ArrayList<>();
            while (rs.next())
                categories.add(new Category(
                        rs.getInt("id"),
                        rs.getString("name")
                ));
            return categories;
    }

    /**
     * saves category in database
     * @param category to be saved
     * @return generated id key
     * @throws SQLException sql exception
     */
    @Override
    public int save(Category category) throws SQLException {
            PreparedStatement prep = connection.prepareStatement("INSERT INTO Category(name) VALUES(?);", Statement.RETURN_GENERATED_KEYS);
            prep.setString(1, category.getName());
            prep.execute();
            ResultSet rs = prep.getGeneratedKeys();
            if (rs.next()) return rs.getInt(1);
            else throw new SQLException();
    }

    /**
     * returns all categories of one dish by its id
     * @param dishid of dish
     * @return list of found categories from dish
     * @throws SQLException sql exception
     */
    @Override
    public List<Category> getAllByDishId(int dishid) throws SQLException {
            ResultSet rs = connection.createStatement().executeQuery("SELECT c.id, c.name FROM Category c JOIN Dish_Category dc ON c.id=dc.categoryid WHERE dc.dishid=" + dishid);
            List<Category> categories = new ArrayList<>();
            while (rs.next())
                categories.add(
                        new Category(
                                rs.getInt("c.id"),
                                rs.getString("c.name")
                        ));
            return categories;
    }

    /**
     * deletes categories
     * @param categories to be deleted
     * @throws SQLException sql exception
     */
    @Override
    public void deleteAll(List<Category> categories) throws SQLException {
        for (Category c: categories)
            connection.createStatement().executeUpdate("DELETE FROM category WHERE id=" + c.getId());
    }
}
