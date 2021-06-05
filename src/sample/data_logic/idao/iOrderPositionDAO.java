package sample.data_logic.idao;

import sample.data_logic.dto.OrderPosition;

import java.sql.SQLException;
import java.util.List;

public interface iOrderPositionDAO {
    void save(OrderPosition orderPosition, int orderid) throws SQLException;
    List<OrderPosition> getAllByOrderId(int orderid) throws SQLException;
}
