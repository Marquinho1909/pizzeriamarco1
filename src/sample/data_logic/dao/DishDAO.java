package sample.data_logic.dao;

import sample.JDBCClient;
import sample.data_logic.dto.Category;
import sample.data_logic.dto.Dish;
import sample.data_logic.idao.iDishDAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO for Dish
 */
public class DishDAO extends DAO implements iDishDAO {
    CategoryDAO categoryDAO;
    Connection connection;

    public DishDAO(CategoryDAO categoryDAO) {
        this.categoryDAO = categoryDAO;
        connection = new JDBCClient().connection;
    }

    /**
     * returns all dishes in database
     * @return list of found dishes
     * @throws SQLException sql exception
     */
    @Override
    public List<Dish> getAll() throws SQLException {
        List<Dish> dishes = new ArrayList<>();
            ResultSet result = connection.createStatement().executeQuery("SELECT * FROM Dish;");
            while (result.next()) {
                dishes.add(new Dish(
                        result.getInt("id"),
                        result.getString("name"),
                        categoryDAO.getAllByDishId(result.getInt("id")),
                        result.getDouble("price"),
                        result.getBoolean("active")
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
    @Override
    public int save(Dish dish) throws SQLException {
            int dishKey;
            int categoryKey = 0;

            // inserting dish and setting dishKey
            PreparedStatement prep = connection.prepareStatement("INSERT INTO Dish (name, price, active) VALUES (?,?,?);", Statement.RETURN_GENERATED_KEYS);
            prep.setString(1, dish.getName());
            prep.setDouble(2, dish.getPrice());
            prep.setBoolean(3, dish.isActive());
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
    @Override
    public void delete(int id) throws SQLException {
            connection.createStatement().execute("DELETE FROM Dish_Category WHERE dishid=" + id + ";");
            connection.createStatement().execute("DELETE FROM Dish WHERE id=" + id + ";");
    }

    public void update(int id, Dish dish) throws SQLException {
        PreparedStatement prep = connection.prepareStatement("UPDATE Dish SET name=?, price=?, active=? WHERE id=?;");
        prep.setString(1, dish.getName());
        prep.setDouble(2, dish.getPrice());
        prep.setBoolean(3, dish.isActive());
        prep.setInt(4, id);
        prep.executeUpdate();
    }
}
