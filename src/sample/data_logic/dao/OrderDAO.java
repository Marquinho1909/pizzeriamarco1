package sample.data_logic.dao;

import sample.JDBCClient;
import sample.data_logic.dto.Dish;
import sample.data_logic.dto.Order;
import sample.data_logic.dto.OrderPosition;
import sample.data_logic.idao.iOrderDAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO for Order
 */
public class OrderDAO extends DAO implements iOrderDAO {
    UserDAO userDAO;
    CategoryDAO categoryDAO;

    Connection connection;

    public OrderDAO(UserDAO userDAO, CategoryDAO categoryDAO) {
        this.userDAO = userDAO;
        this.categoryDAO = categoryDAO;
        connection = new JDBCClient().connection;
    }

    /**
     * returns list of all orders in database
     * @return list of found orders
     * @throws SQLException sql exception
     */
    @Override
    public List<Order> getAll() throws SQLException {
        List<Order> orders = new ArrayList<>();
        ResultSet rs = connection.createStatement().executeQuery("SELECT * FROM Dishorder");
        while (rs.next()) {
            orders.add(new Order(
                    rs.getInt("id"),
                    getOrderPositionsForOrder(rs.getInt("id")),
                    rs.getDate("orderdate"),
                    userDAO.get(rs.getInt("userid")).orElseThrow(),
                    rs.getDouble("discount")
            ));
        }
        return orders;
    }

    /**
     * saves order
     * @param order to be saved
     * @throws SQLException sql exception
     */
    @Override
    public void save(Order order) throws SQLException {
        PreparedStatement prep = connection.prepareStatement("INSERT INTO Dishorder(userid, orderdate, discount) VALUES(?,?,?)", Statement.RETURN_GENERATED_KEYS);
        prep.setInt(1, order.getUser().getId());
        prep.setDate(2, new Date(order.getOrderDate().getTime()));
        prep.setDouble(3, order.getDiscount());

        prep.execute();
        ResultSet gK = prep.getGeneratedKeys();

        int oId;
        if (gK.first()) oId = gK.getInt(1);
        else throw new SQLException("GENERATED KEY NOT FOUND");

        for (OrderPosition op : order.getOrderpositions()) {
            prep = connection.prepareStatement("INSERT INTO OrderPosition(orderid, dishid, amount) VALUES(?, ?, ?)");
            prep.setInt(1, oId);
            prep.setInt(2, op.getDish().getId());
            prep.setInt(3, op.getAmount());
            prep.execute();
        }
    }

    /**
     * deletes all orders and orderpositions from database
     * @throws SQLException sql exception
     */
    @Override
    public void deleteAll() throws SQLException {
        connection.createStatement().executeUpdate("DELETE FROM OrderPosition");
        connection.createStatement().executeUpdate("DELETE FROM Dishorder");
    }

    /**
     * returns all orderPositions of order by its id
     *
     * @param orderid of order
     * @return all found orderPositions
     * @throws SQLException sql exception
     */
    public List<OrderPosition> getOrderPositionsForOrder(int orderid) throws SQLException {
        List<OrderPosition> orderPositions = new ArrayList<>();
        ResultSet rs = connection.createStatement().executeQuery("SELECT * FROM OrderPosition o JOIN Dish d ON o.dishid=d.id WHERE orderid=" + orderid + ";");
        while (rs.next()) {
            orderPositions.add(new OrderPosition(
                    rs.getInt("id"), new Dish(
                    rs.getInt("dishid"),
                    rs.getString("name"),
                    categoryDAO.getAllByDishId(rs.getInt("dishid")),
                    rs.getDouble("price"),
                    rs.getBoolean("active")
            ),
                    rs.getInt("amount")
            ));
        }
        return orderPositions;
    }
}
