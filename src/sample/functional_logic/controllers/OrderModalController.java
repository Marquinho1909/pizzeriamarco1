package sample.functional_logic.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import sample.data_logic.DAOFactory;
import sample.data_logic.dao.CouponDAO;
import sample.data_logic.dao.OrderDAO;
import sample.data_logic.dao.UserDAO;
import sample.data_logic.dto.*;
import sample.functional_logic.AlertService;

import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

public class OrderModalController extends ModalController implements Initializable {
    @FXML private CheckBox coupon_check;
    @FXML private ListView<String> overview;

    private List<OrderPosition> dishesInCart = new ArrayList<>();
    private Coupon coupon;

    private OrderDAO orderDAO;
    private UserDAO userDAO;
    private CouponDAO couponDAO;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        orderDAO = (OrderDAO) DAOFactory.getInstance().getDAO("Order");
        userDAO = (UserDAO) DAOFactory.getInstance().getDAO("User");
        couponDAO = (CouponDAO) DAOFactory.getInstance().getDAO("Coupon");

        synchronizeCoupon();
    }

    /**
     * fetches coupon for customer if available
     */
    public void synchronizeCoupon() {
        try {
            coupon = userDAO.getCustomerCoupon((Customer) UserSessionSingleton.currentSession().getUser()).orElse(null);
        } catch (SQLException e) {
            e.printStackTrace();
            setStatus(ModalStatus.FAILURE);
            AlertService.showAlert(Alert.AlertType.ERROR, "Fehler", "Ein Fehler ist aufgetreten, bitte wenden Sie sich an den Support.", ButtonType.OK);
        }
        coupon_check.setVisible(coupon != null);
    }

    /**
     * displays orderpositions of dishesInCart in list, calculates total including possible coupon
     */
    public void displayDishes() {
        setStatus(ModalStatus.INITIALIZED);

        double total = 0;
        overview.getItems().clear();

        for (OrderPosition o: dishesInCart) {
            total += o.getDish().getPrice()*o.getAmount();
            overview.getItems().add("+" + CustomerPageController.transformPrice(o.getDish().getPrice()*o.getAmount()) + "   " + o.getAmount() + "x " + o.getDish().getName());
        }
        if (coupon_check.isSelected()) {
            overview.getItems().add("-" + CustomerPageController.transformPrice(total * 0.1) + "   " + "10% Willkommens-Gutschein");
            total *= 0.9;
        }

        overview.getItems().addAll("", "_______________________", "Total: " + CustomerPageController.transformPrice(total));
    }

    /**
     * closes modal when user cancels order process
     * @param actionEvent for getting stage
     */
    public void closeModal(ActionEvent actionEvent) {
        setStatus(ModalStatus.CLOSED);
        ((Stage) ((Node)actionEvent.getSource()).getScene().getWindow()).close();
    }

    /**
     * saves order in database, sets status accordingly and closes modal
     *
     * @param actionEvent ae
     */
    public void order(ActionEvent actionEvent) {
        try {
            if (orderDAO.save(
                    new Order(
                            dishesInCart,
                            new Date(),
                            UserSessionSingleton.currentSession().getUser(),
                            coupon_check.isSelected() ? coupon.getValue() : 0
                    )
            ) != 0) {
                if (coupon != null) {
                    couponDAO.delete(coupon.getId());
                    coupon = null;
                }
                setStatus(ModalStatus.SUCCESS);
            } else
                throw new SQLException("ORDER COULD NOT BE CREATED OR NOT KEY WAS RETURNED PROPERLY");
        } catch (SQLException e) {
            e.printStackTrace();
            setStatus(ModalStatus.FAILURE);
            AlertService.showError();
            ((Stage) ((Node)actionEvent.getSource()).getScene().getWindow()).close();
        }

    }

    public void setDishesInCart(List<OrderPosition> dishesInCart) {
        this.dishesInCart = dishesInCart;
    }
}
