package sample.dao;

import sample.JDBCClient;
import sample.dto.Dish;
import sample.dto.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DishDAO implements DAO<Dish> {

    @Override
    public Optional<Dish> get(int id) {
        try (Connection connection = new JDBCClient().connection) {
            ResultSet result = connection.createStatement().executeQuery("SELECT * FROM Dish WHERE id=" + id + ";");
            if (result.first()) {
                return Optional.of(new Dish(
                        result.getInt("id"),
                        result.getString("name"),
                        result.getDouble("price")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public List<Dish> getAll() {
        List<Dish> dishes = new ArrayList<>();

        try (Connection connection = new JDBCClient().connection) {
            ResultSet result = connection.createStatement().executeQuery("SELECT * FROM Dish;");
            while (result.next()) {
                dishes.add(new Dish(
                        result.getInt("id"),
                        result.getString("name"),
                        result.getDouble("price")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dishes;
    }

    @Override
    public boolean save(Dish dish) {
        try (Connection connection = new JDBCClient().connection) {
            PreparedStatement prep = connection.prepareStatement("INSERT INTO Dish (name, price) VALUES (?,?);");

            prep.setString(1, dish.getName());
            prep.setDouble(2, dish.getPrice());
            prep.execute();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public void update(int id, Dish dish) {
        try (Connection connection = new JDBCClient().connection) {
            ResultSet result = connection.createStatement().executeQuery("SELECT * FROM Dish WHERE id=" + id + ";");

            if (result.first()) {
                int dId = result.getInt("id");
                PreparedStatement prep = connection.prepareStatement("UPDATE Dish SET name=?, price=? WHERE id=?");

                prep.setString(1, dish.getName());
                prep.setDouble(2, dish.getPrice());
                prep.setInt(3, dId);
                prep.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(int id) {
        try (Connection connection = new JDBCClient().connection) {
            ResultSet result = connection.createStatement().executeQuery("SELECT * FROM Dish WHERE id=" + id + ";");

            if (result.first()) {
                int dId = result.getInt("id");
                PreparedStatement prep = connection.prepareStatement("DELETE FROM Dish WHERE id=?");

                prep.setInt(1, dId);
                prep.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
