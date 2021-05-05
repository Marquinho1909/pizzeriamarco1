package sample.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import sample.dao.OrderDAO;
import sample.dao.UserDAO;
import sample.dto.Order;
import sample.dto.OrderPosition;
import java.util.ArrayList;
import java.util.List;

public class OrderModalController {
    @FXML private CheckBox code_check;
    @FXML private ListView<String> overview;

    private OrderStatus status = OrderStatus.INITIALIZED;
    private List<OrderPosition> dishesInCart = new ArrayList<>();

    /**
     * displays orderpositions of dishesInCart in list, calculates total including possible coupon
     */
    public void displayDishes() {
        status = OrderStatus.INITIALIZED;

        double total = 0;
        overview.getItems().clear();

        for (OrderPosition o: dishesInCart) {
            total += o.getDish().getPrice()*o.getAmount();
            overview.getItems().add(
                    "+" + MenuController.transformPrice(o.getDish().getPrice()*o.getAmount()) + "   " + o.getAmount() + "x " + o.getDish().getName()
            );
        }
        if (code_check.isSelected()) {
            overview.getItems().add("-" + MenuController.transformPrice(total * 0.1) + "   " + "10% Willkommens-Gutschein");
            total *= 0.9;
        }
        overview.getItems().add("");
        overview.getItems().add("_______________________");
        overview.getItems().add("Total: " + MenuController.transformPrice(total));
    }

    /**
     * closes modal when user cancels order process
     * @param actionEvent for getting stage
     */
    public void closeModal(ActionEvent actionEvent) {
        status = OrderStatus.CLOSED;
        ((Stage) ((Node)actionEvent.getSource()).getScene().getWindow()).close();
    }

    /**
     * saves order in database, sets status accordingly and closes modal
     * TODO calculate coupon
     * @param actionEvent
     */
    public void order(ActionEvent actionEvent) {
        OrderDAO orderDAO = new OrderDAO();
        if (orderDAO.save(
                new Order(
                        dishesInCart,
                        UserDAO.loggedInUser
                )
        ))
            status = OrderStatus.SUCCESS;
        else
            status = OrderStatus.FAILURE;
        ((Stage) ((Node)actionEvent.getSource()).getScene().getWindow()).close();
    }

    public void setDishesInCart(List<OrderPosition> dishesInCart) {
        this.dishesInCart = dishesInCart;
    }

    public OrderStatus getStatus() {
        return status;
    }

    enum OrderStatus {
        INITIALIZED, SUCCESS, FAILURE, CLOSED,
    }
}
