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

/**
 * DAO of OrderPosition
 */
public class OrderPositionDAO extends DAO {
    CategoryDAO categoryDAO = new CategoryDAO();

    Connection connection;

    public OrderPositionDAO() {
        connection = new JDBCClient().connection;
    }

    /**
     * saves orderPosition to order by its id
     *
     * @param orderPosition to be saved
     * @param orderid       of order
     * @throws SQLException sql exception
     */
    public void save(OrderPosition orderPosition, int orderid) throws SQLException {
        PreparedStatement prep = connection.prepareStatement("INSERT INTO OrderPosition(orderid, dishid, amount) VALUES(?, ?, ?)");
        prep.setInt(1, orderid);
        prep.setInt(2, orderPosition.getDish().getId());
        prep.setInt(3, orderPosition.getAmount());
        prep.execute();
    }

    /**
     * returns all orderPositions of order by its id
     *
     * @param orderid of order
     * @return all found orderPositions
     * @throws SQLException sql exception
     */
    public List<OrderPosition> getAllByOrderId(int orderid) throws SQLException {
        List<OrderPosition> orderPositions = new ArrayList<>();
        ResultSet rs = connection.createStatement().executeQuery("SELECT * FROM OrderPosition o JOIN Dish d ON o.dishid=d.id WHERE orderid=" + orderid + ";");
        while (rs.next()) {
            orderPositions.add(new OrderPosition(
                    rs.getInt("id"), new Dish(
                    rs.getInt("dishid"),
                    rs.getString("name"),
                    categoryDAO.getAllByDishId(rs.getInt("dishid")),
                    rs.getDouble("price")
            ),
                    rs.getInt("amount")
            ));
        }
        return orderPositions;
    }
}
