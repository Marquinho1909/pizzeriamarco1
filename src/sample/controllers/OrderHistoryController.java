package sample.controllers;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.Initializable;
import javafx.scene.control.TableView;
import sample.dao.OrderDAO;
import sample.dto.Order;
import sample.dto.OrderPosition;
import sample.dto.UserSession;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.ResourceBundle;

public class OrderHistoryController implements Initializable {
    public TableView<TableOrder> table_order;

    OrderDAO orderDAO;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        orderDAO = new OrderDAO();

        updateDishTable();
    }

    /**
     * gets all dishes and displays them in table
     */
    public void updateDishTable() {
        table_order.getItems().clear();

        List<Order> orders = orderDAO.getAllByUserId(UserSession.currentSession().getUser().getId());
        for (Order o : orders)
            table_order.getItems().add(new TableOrder(
                    new SimpleDateFormat("dd-MM-yyyy").format(o.getOrderDate()),
                    o.getOrderpositions(),
                    o.getDiscount()
            ));
    }

    public static class TableOrder {
        private final StringProperty date;
        private final StringProperty orderlist;
        private final StringProperty total;

        TableOrder(String date, List<OrderPosition> orderlist, double discount) {
            this.date = new SimpleStringProperty(date);

            StringBuilder s = new StringBuilder();
            double sum = 0;
            for (OrderPosition op : orderlist) {
                sum += op.getAmount() * op.getDish().getPrice();
                s.append(op.getAmount()).append("x ").append(op.getDish().getName()).append(", ");
            }
            String st_sum = CustomerPageController.transformPrice(sum);

            if (discount != 0.0)
                st_sum += " (-" + discount * 100 + "%)";

            this.total = new SimpleStringProperty(st_sum);
            this.orderlist = new SimpleStringProperty(s.substring(0, s.length() - 2));
        }

        public String getDate() {
            return date.get();
        }

        public StringProperty dateProperty() {
            return date;
        }

        public String getOrderlist() {
            return orderlist.get();
        }

        public StringProperty orderlistProperty() {
            return orderlist;
        }

        public String getTotal() {
            return total.get();
        }

        public StringProperty totalProperty() {
            return total;
        }
    }
}
