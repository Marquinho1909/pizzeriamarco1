package sample.dao;

import sample.JDBCClient;
import sample.dto.Dish;
import sample.dto.OrderPosition;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class OrderPositionDAO implements DAO<OrderPosition> {
    DishDAO dishDAO = new DishDAO();

    @Override
    public Optional<OrderPosition> get(int id) {
        try (Connection connection = new JDBCClient().connection) {
            ResultSet rs = connection.createStatement().executeQuery("SELECT * FROM OrderPosition WHERE id=" + ";");

            if (rs.first()) {
                OrderPosition op = new OrderPosition(
                        null,
                        rs.getInt("amount")
                );

                dishDAO.get(rs.getInt("dishid")).ifPresent(op::setDish);
                return Optional.of(op);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public List<OrderPosition> getAll() {
        return null;
    }

    @Override
    public void save(OrderPosition orderPosition) {

    }

    public void save(OrderPosition orderPosition, int orderid) {
        try (Connection connection = new JDBCClient().connection) {
            PreparedStatement prep = connection.prepareStatement("INSERT INTO OrderPosition(orderid, dishid, amount) VALUES(?, ?, ?)");
            prep.setInt(1, orderid);
            prep.setInt(2, orderPosition.getDish().getId());
            prep.setInt(3, orderPosition.getAmount());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(int id, OrderPosition orderPosition) {
        //TODO
    }

    @Override
    public void delete(int id) {
        //TODO
    }

    public List<OrderPosition> getAllByOrderId(int orderid) {
        List<OrderPosition> orderPositions = new ArrayList<>();
        try (Connection connection = new JDBCClient().connection) {
            ResultSet rs = connection.createStatement().executeQuery("SELECT * FROM OrderPosition o JOIN Dish d ON o.dishid=d.id WHERE orderid=" + orderid + ";");
            while (rs.next()) {
                orderPositions.add(
                        new OrderPosition(
                                rs.getInt("id"),
                                new Dish(
                                        rs.getInt("dishid"),
                                        rs.getString("name"),
                                        rs.getDouble("price")
                                ),
                                rs.getInt("amount")
                        ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orderPositions;
    }
}
