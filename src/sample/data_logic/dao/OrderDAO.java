package sample.data_logic.dao;

import sample.JDBCClient;
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
    OrderPositionDAO orderpositionDAO;

    Connection connection;

    public OrderDAO(UserDAO userDAO, OrderPositionDAO orderPositionDAO) {
        this.userDAO = userDAO;
        this.orderpositionDAO = orderPositionDAO;
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
                    orderpositionDAO.getAllByOrderId(rs.getInt("id")),
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
     * @return generated id key
     * @throws SQLException sql exception
     */
    @Override
    public int save(Order order) throws SQLException {
        PreparedStatement prep = connection.prepareStatement("INSERT INTO Dishorder(userid, orderdate, discount) VALUES(?,?,?)", Statement.RETURN_GENERATED_KEYS);
        prep.setInt(1, order.getUser().getId());
        prep.setDate(2, new Date(order.getOrderDate().getTime()));
        prep.setDouble(3, order.getDiscount());

        prep.execute();
        ResultSet gK = prep.getGeneratedKeys();

        int oId;
        if (gK.first()) oId = gK.getInt(1);
        else throw new SQLException("GENERATED KEY NOT FOUND");

        for (OrderPosition op : order.getOrderpositions())
            orderpositionDAO.save(op, oId);

        return oId;
    }

    /**
     * returns all orders orderd by one user of given id
     * @param userid of user
     * @return all found orders of user
     * @throws SQLException sql exception
     */
    @Override
    public List<Order> getAllByUserId(int userid) throws SQLException {
        List<Order> orders = new ArrayList<>();
        ResultSet rs = connection.createStatement().executeQuery("SELECT * FROM Dishorder WHERE userid=" + userid);
        while (rs.next()) {
            orders.add(new Order(
                    rs.getInt("id"),
                    orderpositionDAO.getAllByOrderId(rs.getInt("id")),
                    rs.getDate("orderdate"),
                    userDAO.get(rs.getInt("userid")).orElseThrow(),
                    rs.getDouble("discount")
            ));
        }
        return orders;
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
}
