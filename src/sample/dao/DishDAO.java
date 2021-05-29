package sample.dao;

import sample.JDBCClient;
import sample.dto.Category;
import sample.dto.Dish;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * DAO for Dish
 */
public class DishDAO extends DAO {
    CategoryDAO categoryDAO = new CategoryDAO();
    Connection connection;

    public DishDAO() {
        connection = new JDBCClient().connection;
    }

    /**
     * returns dish by its id
     * @param id of dish
     * @return dish
     * @throws SQLException sql exception
     */
    public Optional<Dish> get(int id) throws SQLException {
            ResultSet result = connection.createStatement().executeQuery("SELECT * FROM Dish WHERE id=" + id + ";");
            if (result.first()) {
                return Optional.of(new Dish(
                        result.getInt("id"),
                        result.getString("name"),
                        categoryDAO.getAllByDishId(result.getInt("id")),
                        result.getDouble("price")
                ));
            }
        return Optional.empty();
    }

    /**
     * returns all dishes in database
     * @return list of found dishes
     * @throws SQLException sql exception
     */
    public List<Dish> getAll() throws SQLException {
        List<Dish> dishes = new ArrayList<>();
            ResultSet result = connection.createStatement().executeQuery("SELECT * FROM Dish;");
            while (result.next()) {
                dishes.add(new Dish(
                        result.getInt("id"),
                        result.getString("name"),
                        categoryDAO.getAllByDishId(result.getInt("id")),
                        result.getDouble("price")
                ));
            }
        return dishes;
    }

    /**
     * saves dish
     * @param dish to be saved
     * @return generated id key
     * @throws SQLException sql exception
     */
    public int save(Dish dish) throws SQLException {
            int dishKey;
            int categoryKey = 0;

            // inserting dish and setting dishKey
            PreparedStatement prep = connection.prepareStatement("INSERT INTO Dish (name, price) VALUES (?,?);", Statement.RETURN_GENERATED_KEYS);
            prep.setString(1, dish.getName());
            prep.setDouble(2, dish.getPrice());
            prep.execute();
            ResultSet rs = prep.getGeneratedKeys();
            if (rs.first()) dishKey = rs.getInt(1);
            else throw new SQLException("GENERATED KEY NOT FOUND");

            List<Category> db_c = new CategoryDAO().getAll();
            if (dish.getCategories() != null)
                for (Category c : dish.getCategories()) {
                    for (Category db : db_c)
                        if (db.getName().equals(c.getName())) {
                            // if category name already exists, use its key
                            categoryKey = db.getId();
                            break;
                        }
                    if (categoryKey == 0)
                        throw new SQLException("CATEGORY " + c.getName() + " NOT FOUND");

                    prep = connection.prepareStatement("INSERT INTO Dish_Category(dishid, categoryid) VALUES(?,?);");
                    prep.setInt(1, dishKey);
                    prep.setInt(2, categoryKey);
                    prep.execute();
                }
            return dishKey;
    }

    /**
     * deletes dish by its id
     * @param id of dish to be deleted
     * @throws SQLException sql exception
     */
    public void delete(int id) throws SQLException {
            connection.createStatement().execute("DELETE FROM Dish_Category WHERE dishid=" + id + ";");
            connection.createStatement().execute("DELETE FROM Dish WHERE id=" + id + ";");
    }
}
