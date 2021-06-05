package sample.data_logic.dao;

import sample.JDBCClient;
import sample.data_logic.dto.Dish;
import sample.data_logic.dto.OrderPosition;
import sample.data_logic.idao.iOrderPositionDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO of OrderPosition
 */
public class OrderPositionDAO extends DAO implements iOrderPositionDAO {
    CategoryDAO categoryDAO;
    Connection connection;

    public OrderPositionDAO(CategoryDAO categoryDAO) {
        this.categoryDAO = categoryDAO;
        connection = new JDBCClient().connection;
    }

    /**
     * saves orderPosition to order by its id
     *
     * @param orderPosition to be saved
     * @param orderid       of order
     * @throws SQLException sql exception
     */
    @Override
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
    @Override
    public List<OrderPosition> getAllByOrderId(int orderid) throws SQLException {
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