package sample.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import sample.dao.CouponDAO;
import sample.dao.OrderDAO;
import sample.dao.UserDAO;
import sample.dto.*;

import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

public class OrderModalController implements Initializable {
    @FXML private CheckBox coupon_check;
    @FXML private ListView<String> overview;

    private ModalStatus status = ModalStatus.INITIALIZED;
    private List<OrderPosition> dishesInCart = new ArrayList<>();
    private Coupon coupon;
    private OrderDAO orderDAO;
    private UserDAO userDAO;
    private CouponDAO couponDAO;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        orderDAO = new OrderDAO();
        userDAO = new UserDAO();
        couponDAO = new CouponDAO();

        coupon = userDAO.getCustomerCoupon((Customer) UserSession.currentSession().getUser());
        coupon_check.setVisible(coupon != null);
    }

    /**
     * displays orderpositions of dishesInCart in list, calculates total including possible coupon
     */
    public void displayDishes() {
        status = ModalStatus.INITIALIZED;

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
        status = ModalStatus.CLOSED;
        ((Stage) ((Node)actionEvent.getSource()).getScene().getWindow()).close();
    }

    /**
     * saves order in database, sets status accordingly and closes modal
     *
     * @param actionEvent ae
     */
    public void order(ActionEvent actionEvent) {
        if (orderDAO.save(
                new Order(
                        dishesInCart,
                        new Date(),
                        UserSession.currentSession().getUser(),
                        coupon_check.isSelected() ? coupon.getValue() : 0
                )
        ) != 0) {
            if (coupon != null && couponDAO.delete(coupon.getId()))
            coupon = null;
            status = ModalStatus.SUCCESS;
        }
        else
            status = ModalStatus.FAILURE;
        ((Stage) ((Node)actionEvent.getSource()).getScene().getWindow()).close();
    }

    public void setDishesInCart(List<OrderPosition> dishesInCart) {
        this.dishesInCart = dishesInCart;
    }

    public ModalStatus getStatus() {
        return status;
    }
}
