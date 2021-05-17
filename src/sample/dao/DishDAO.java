package sample.dao;

import sample.JDBCClient;
import sample.dto.Category;
import sample.dto.Dish;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DishDAO {
    CategoryDAO categoryDAO = new CategoryDAO();
    Connection connection;

    public DishDAO() {
        connection = new JDBCClient().connection;
    }

    public Optional<Dish> get(int id) {
        try {
            ResultSet result = connection.createStatement().executeQuery("SELECT * FROM Dish WHERE id=" + id + ";");
            if (result.first()) {
                return Optional.of(new Dish(
                        result.getInt("id"),
                        result.getString("name"),
                        categoryDAO.getAllByDishId(result.getInt("id")),
                        result.getDouble("price")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public List<Dish> getAll() {
        List<Dish> dishes = new ArrayList<>();

        try {
            ResultSet result = connection.createStatement().executeQuery("SELECT * FROM Dish;");
            while (result.next()) {
                dishes.add(new Dish(
                        result.getInt("id"),
                        result.getString("name"),
                        categoryDAO.getAllByDishId(result.getInt("id")),
                        result.getDouble("price")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dishes;
    }

    public int save(Dish dish) {
        try {
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
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public boolean update(int id, Dish dish) {
        try {
            ResultSet result = connection.createStatement().executeQuery("SELECT * FROM Dish WHERE id=" + id + ";");
            if (result.first()) {
                int dId = result.getInt("id");
                PreparedStatement prep = connection.prepareStatement("UPDATE Dish SET name=?, price=? WHERE id=?");

                prep.setString(1, dish.getName());
                prep.setDouble(2, dish.getPrice());
                prep.setInt(3, dId);
                prep.executeUpdate();
            }
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean delete(int id) {
        try {
            connection.createStatement().execute("DELETE FROM Dish_Category WHERE dishid=" + id + ";");
            connection.createStatement().execute("DELETE FROM Dish WHERE id=" + id + ";");
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
