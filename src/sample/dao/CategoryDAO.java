package sample.dao;

import sample.JDBCClient;
import sample.dto.Category;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CategoryDAO {
    Connection connection;

    public CategoryDAO() {
        connection = new JDBCClient().connection;
    }

    public List<Category> getAll() {
        try {
            ResultSet rs = connection.createStatement().executeQuery("SELECT * FROM Category");
            List<Category> categories = new ArrayList<>();
            while (rs.next())
                categories.add(new Category(
                        rs.getInt("id"),
                        rs.getString("name")
                ));
            return categories;
        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public int save(Category category) {
        try {
            PreparedStatement prep = connection.prepareStatement("INSERT INTO Category(name) VALUES(?);", Statement.RETURN_GENERATED_KEYS);
            prep.setString(1, category.getName());
            prep.execute();
            ResultSet rs = prep.getGeneratedKeys();
            if (rs.next())
                return rs.getInt(1);
            else throw new SQLException();
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public boolean delete(int id) {
        return false;
    }

    public List<Category> getAllByDishId(int dishid) {
        try {
            ResultSet rs = connection.createStatement().executeQuery("SELECT c.id, c.name FROM Category c JOIN Dish_Category dc ON c.id=dc.categoryid WHERE dc.dishid=" + dishid);
            List<Category> categories = new ArrayList<>();
            while (rs.next())
                categories.add(
                        new Category(
                                rs.getInt("c.id"),
                                rs.getString("c.name")
                        ));
            return categories;
        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}
