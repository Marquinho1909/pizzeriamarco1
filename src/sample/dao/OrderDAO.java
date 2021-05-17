package sample.dao;

import sample.JDBCClient;
import sample.dto.Dish;
import sample.dto.Order;
import sample.dto.OrderPosition;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class OrderDAO {
    UserDAO userDAO = new UserDAO();
    OrderPositionDAO orderpositionDAO = new OrderPositionDAO();
    Connection connection;

    public OrderDAO() {
        connection = new JDBCClient().connection;
    }

    public Optional<Order> get(int id) {
        return Optional.empty();
    }

    public List<Order> getAll() {
        List<Order> orders = new ArrayList<>();
        try {
            ResultSet rs = connection.createStatement().executeQuery("SELECT * FROM Dishorder");
            while (rs.next()) {
                orders.add(new Order(
                        rs.getInt("id"),
                        orderpositionDAO.getAllByOrderId(rs.getInt("id")),
                        rs.getDate("orderdate"),
                        userDAO.get(rs.getInt("userid")).get(),
                        rs.getDouble("discount")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orders;
    }

    public int save(Order order) {
        try {
            PreparedStatement prep = connection.prepareStatement("INSERT INTO Dishorder(userid, orderdate, discount) VALUES(?,?,?)", Statement.RETURN_GENERATED_KEYS);
            prep.setInt(1, order.getUser().getId());
            prep.setDate(2, new Date(order.getOrderDate().getTime()));
            prep.setDouble(3, order.getDiscount());

            prep.execute();
            ResultSet gK = prep.getGeneratedKeys();
            int oId;
            if (gK.first())
                oId = gK.getInt(1);
            else
                throw new SQLException("GENERATED KEY NOT FOUND");

            for (OrderPosition op : order.getOrderpositions())
                orderpositionDAO.save(op, oId);

            return oId;
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public boolean update(int id, Order order) {
        try {
            PreparedStatement prep = connection.prepareStatement("UPDATE Dishorder SET userid=?, orderdate=?, discount=? WHERE id=" + id + ";");
            prep.setInt(1, order.getUser().getId());
            prep.setDate(2, new Date(order.getOrderDate().getTime()));
            prep.setDouble(3, order.getDiscount());
            prep.executeUpdate();
            connection.createStatement().executeUpdate("DELETE FROM OrderPosition WHERE orderid=" + id + ";");

            for (OrderPosition op : order.getOrderpositions())
                orderpositionDAO.save(op, id);

            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean delete(int id) {
        try {
            connection.createStatement().executeUpdate("DELETE FROM OrderPosition WHERE orderid=" + id + ";");
            connection.createStatement().executeUpdate("DELETE FROM Dishorder WHERE id=" + id + ";");
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Order> getAllByUserId(int userid) {
        List<Order> orders = new ArrayList<>();
        try {
            ResultSet rs = connection.createStatement().executeQuery("SELECT * FROM Dishorder WHERE userid=" + userid);
            while (rs.next()) {
                orders.add(new Order(
                        rs.getInt("id"),
                        orderpositionDAO.getAllByOrderId(rs.getInt("id")),
                        rs.getDate("orderdate"),
                        userDAO.get(rs.getInt("userid")).get(),
                        rs.getDouble("discount")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orders;
    }
}
