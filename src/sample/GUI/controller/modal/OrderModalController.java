package sample.GUI.controller.modal;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.Stage;
import sample.GUI.AlertService;
import sample.GUI.GUIHandler;
import sample.GUI.controller.Modal;
import sample.data_logic.dto.OrderPosition;
import sample.functional_logic.service.CategoryService;

import java.net.URL;
import java.sql.SQLException;
import java.util.Date;
import java.util.ResourceBundle;

public class OrderModalController extends Modal implements Initializable {
    public Label orderText;
    public CheckBox coupon_check;
    public ListView<String> overview;

    public OrderModalController(GUIHandler guiHandler) {
        super(guiHandler);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        displayDishes();
    }

    /**
     * displays orderpositions of dishesInCart in list, calculates total including possible coupon
     */
    @FXML
    public void displayDishes() {
        setStatus(ModalStatus.INITIALIZED);

        double total = 0;
        overview.getItems().clear();

        coupon_check.setVisible(guiHandler.getCouponOfLoggedInUser() != null);

        for (OrderPosition o : guiHandler.getCart()) {
            total += o.getDish().getPrice() * o.getAmount();
            overview.getItems().add("+" + GUIHandler.transformPrice(o.getDish().getPrice() * o.getAmount()) + "   " + o.getAmount() + "x " + o.getDish().getName());
        }
        if (coupon_check.isSelected()) {
            overview.getItems().add("-" + GUIHandler.transformPrice(total * 0.1) + "   " + "10% Willkommens-Gutschein");
            total *= 0.9;
        }

        overview.getItems().addAll("", "_______________________", "Total: " + GUIHandler.transformPrice(total));

        orderText.setText("Wollen Sie die Bestellung für " + GUIHandler.transformPrice(total) + " aufgeben?");
    }

    /**
     * closes modal when user cancels order process
     *
     * @param actionEvent for getting stage
     */
    public void closeModal(ActionEvent actionEvent) {
        setStatus(ModalStatus.CLOSED);
        ((Stage) ((Node) actionEvent.getSource()).getScene().getWindow()).close();
    }

    /**
     * saves order in database, sets status accordingly and closes modal
     *
     * @param actionEvent ae
     */
    public void order(ActionEvent actionEvent) {
        try {
            guiHandler.order(coupon_check.isSelected(), new Date());
            setStatus(ModalStatus.SUCCESS);

        } catch (SQLException e) {
            e.printStackTrace();
            setStatus(ModalStatus.FAILURE);
            AlertService.showError();
        }
        ((Stage) ((Node) actionEvent.getSource()).getScene().getWindow()).close();
    }
}
