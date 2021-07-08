package resources.GUIController;

import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.Stage;
import sample.data_logic.dto.OrderPosition;
import sample.functional_logic.AlertService;
import sample.functional_logic.controllers.CustomerPageController;
import sample.functional_logic.controllers.ModalController;
import sample.functional_logic.controllers.OrderModalController;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class OrderModalGUIController extends ModalController implements Initializable {
    public Label orderText;
    public CheckBox coupon_check;
    public ListView<String> overview;

    OrderModalController controller;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        controller = new OrderModalController();
        synchronizeCoupon();
    }

    /**
     * fetches coupon for customer if available
     */
    public void synchronizeCoupon() {
        try {
            controller.fetchCoupon();
        } catch (SQLException e) {
            e.printStackTrace();
            setStatus(ModalStatus.FAILURE);
            AlertService.showAlert(Alert.AlertType.ERROR, "Fehler", "Ein Fehler ist aufgetreten, bitte wenden Sie sich an den Support.", ButtonType.OK);
        }
        coupon_check.setVisible(controller.coupon != null);
    }

    /**
     * displays orderpositions of dishesInCart in list, calculates total including possible coupon
     */
    public void displayDishes() {
        setStatus(ModalStatus.INITIALIZED);

        double total = 0;
        overview.getItems().clear();

        for (OrderPosition o : controller.dishesInCart) {
            total += o.getDish().getPrice() * o.getAmount();
            overview.getItems().add("+" + CustomerPageController.transformPrice(o.getDish().getPrice() * o.getAmount()) + "   " + o.getAmount() + "x " + o.getDish().getName());
        }
        if (coupon_check.isSelected()) {
            overview.getItems().add("-" + CustomerPageController.transformPrice(total * 0.1) + "   " + "10% Willkommens-Gutschein");
            total *= 0.9;
        }

        overview.getItems().addAll("", "_______________________", "Total: " + CustomerPageController.transformPrice(total));

        orderText.setText("Wollen Sie die Bestellung für " + CustomerPageController.transformPrice(total) + " aufgeben?");
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
            controller.order(coupon_check.isSelected());
            setStatus(ModalStatus.SUCCESS);

        } catch (SQLException e) {
            e.printStackTrace();
            setStatus(ModalStatus.FAILURE);
            AlertService.showError();
        }
        ((Stage) ((Node) actionEvent.getSource()).getScene().getWindow()).close();
    }
}
