package resources.GUIController;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.Initializable;
import javafx.scene.control.TableView;
import sample.data_logic.dto.Order;
import sample.data_logic.dto.OrderPosition;
import sample.functional_logic.AlertService;
import sample.functional_logic.controllers.CustomerPageController;
import sample.functional_logic.controllers.ModalController;
import sample.functional_logic.controllers.OrderHistoryModalController;

import java.net.URL;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.ResourceBundle;

public class OrderHistoryModalGUIController extends ModalController implements Initializable {
    public TableView<TableOrder> table_order;

    OrderHistoryModalController controller;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        controller = new OrderHistoryModalController();
        displayOrders();
    }

    /**
     * gets all dishes and displays them in table
     */
    public void displayOrders() {
        table_order.getItems().clear();
        List<Order> orders;
        try {
            orders = controller.getOrdersFromLoggedInUser();
            for (Order o : orders)
                table_order.getItems().add(new TableOrder(
                        new SimpleDateFormat("dd-MM-yyyy").format(o.getOrderDate()),
                        o.getOrderpositions(),
                        o.getDiscount()
                ));
        } catch (SQLException e) {
            e.printStackTrace();
            AlertService.showError();
        }
    }


    /**
     * used for displaying orders in table
     */
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
