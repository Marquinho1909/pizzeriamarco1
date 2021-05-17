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

public class OrderPositionDAO {
    DishDAO dishDAO = new DishDAO();

    Connection connection;

    public OrderPositionDAO() {
        connection = new JDBCClient().connection;
    }

    public Optional<OrderPosition> get(int id) {
        try {
            ResultSet rs = connection.createStatement().executeQuery("SELECT * FROM OrderPosition WHERE id=" + ";");
            if (rs.first()) {
                OrderPosition op = new OrderPosition(null, rs.getInt("amount"));
                dishDAO.get(rs.getInt("dishid")).ifPresent(op::setDish);
                return Optional.of(op);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public List<OrderPosition> getAll() {
        return null;
    }

    public int save(OrderPosition orderPosition) {
        return 0;
    }

    public void save(OrderPosition orderPosition, int orderid) {
        try {
            PreparedStatement prep = connection.prepareStatement("INSERT INTO OrderPosition(orderid, dishid, amount) VALUES(?, ?, ?)");
            prep.setInt(1, orderid);
            prep.setInt(2, orderPosition.getDish().getId());
            prep.setInt(3, orderPosition.getAmount());
            prep.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean update(int id, OrderPosition orderPosition) {
        return false;
        //TODO
    }

    public boolean delete(int id) {
        return false;
        //TODO
    }

    public List<OrderPosition> getAllByOrderId(int orderid) {
        List<OrderPosition> orderPositions = new ArrayList<>();
        try {
            ResultSet rs = connection.createStatement().executeQuery("SELECT * FROM OrderPosition o JOIN Dish d ON o.dishid=d.id WHERE orderid=" + orderid + ";");
            while (rs.next()) {
                orderPositions.add(
                        new OrderPosition(
                                rs.getInt("id"),
                                new Dish(
                                        rs.getInt("dishid"),
                                        rs.getString("name"),
                                        new CategoryDAO().getAllByDishId(rs.getInt("dishid")),
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
