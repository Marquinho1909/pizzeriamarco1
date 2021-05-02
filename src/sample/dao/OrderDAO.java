package sample.dao;

import sample.JDBCClient;
import sample.dto.Admin;
import sample.dto.Order;
import sample.dto.OrderPosition;
import sample.dto.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class OrderDAO implements DAO<Order> {
    UserDAO userDAO = new UserDAO();
    OrderPositionDAO orderpositionDAO = new OrderPositionDAO();

    @Override
    public Optional<Order> get(int id) {
        return Optional.empty();
    }

    @Override
    public List<Order> getAll() {
        List<Order> orders = new ArrayList<>();
        try (Connection connection = new JDBCClient().connection) {
            ResultSet rs = connection.createStatement().executeQuery("SELECT * FROM Dishorder");
            while (rs.next()) {
                orders.add(new Order(
                        rs.getInt("id"),
                        orderpositionDAO.getAllByOrderId(rs.getInt("id")),
                        userDAO.get(rs.getInt("userid")).get()
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orders;
    }

    @Override
    public void save(Order order) {
        try (Connection connection = new JDBCClient().connection) {
            PreparedStatement prep = connection.prepareStatement("INSERT INTO Dishorder(userid, orderdate) VALUES(?, ?)", Statement.RETURN_GENERATED_KEYS);
            prep.setInt(1, UserDAO.loggedInUser.getId());
            prep.setDate(2, new Date(order.getOrderDate().getTime()));

            prep.execute();
            ResultSet gK = prep.getGeneratedKeys();
            int oId;
            if (gK.first())
                oId = gK.getInt(1);
            else
                throw new SQLException("GENERATED KEY NOT FOUND");

            for (OrderPosition op : order.getOrderpositions()) {
                PreparedStatement prepP = connection.prepareStatement("INSERT INTO OrderPosition(orderid, dishid, amount) VALUES (?,?,?);");
                prepP.setInt(1, oId);
                prepP.setInt(2, op.getDish().getId());
                prep.setInt(3, op.getAmount());

                prep.execute();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(int id, Order order) {
        try (Connection connection = new JDBCClient().connection) {
            PreparedStatement prep = connection.prepareStatement("UPDATE Dishorder SET userid=?, orderdate=? WHERE id=" + id + ";");
            prep.setInt(1, order.getUser().getId());
            prep.setDate(2, new Date(order.getOrderDate().getTime()));

            prep.executeUpdate();

            connection.createStatement().executeUpdate("DELETE FROM OrderPosition WHERE orderid=" + id + ";");

            for (OrderPosition op : order.getOrderpositions()) {
                PreparedStatement prepP = connection.prepareStatement("INSERT INTO OrderPosition(orderid, dishid, amount) VALUES (?,?,?);");
                prepP.setInt(1, id);
                prepP.setInt(2, op.getDish().getId());
                prep.setInt(3, op.getAmount());

                prep.execute();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(int id) {
        try (Connection connection = new JDBCClient().connection) {
            connection.createStatement().executeUpdate("DELETE FROM OrderPosition WHERE orderid=" + id + ";");
            connection.createStatement().executeUpdate("DELETE FROM Dishorder WHERE id=" + id + ";");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}
