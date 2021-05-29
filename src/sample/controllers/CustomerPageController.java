package sample.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import sample.AlertService;
import sample.DAOFactory;
import sample.ModalService;
import sample.dao.DishDAO;
import sample.dto.Dish;
import sample.dto.OrderPosition;
import sample.dto.UserSessionSingleton;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

public class CustomerPageController implements Initializable {
    @FXML
    private Label error_msg;
    @FXML
    private VBox dishlist;
    @FXML
    private VBox cart;
    @FXML
    private Label lTotal;
    @FXML
    private Menu menu_btn;

    private final List<OrderPosition> dishesInCart = new ArrayList<>();

    DishDAO dishDAO;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        dishDAO = (DishDAO) DAOFactory.getInstance().getDAO("Dish");

        menu_btn.setText(UserSessionSingleton.currentSession().getUser().getLastname() + ", " + UserSessionSingleton.currentSession().getUser().getFirstName());
        try {
            synchronizeDishlist();
        } catch (SQLException e) {
            AlertService.showAlert(Alert.AlertType.ERROR, "Fehler", "Ein Fehler ist aufgetreten, bitte wenden Sie sich an den Support.", ButtonType.OK);
        }
    }

    /**
     * fetches and displays all dishes from database in GUI
     */
    public void synchronizeDishlist() throws SQLException {
        List<Dish> dishes = dishDAO.getAll();
        boolean bcolor = false;

        for (Dish dish : dishes) {
            Label name = new Label(dish.getId() + ". " + dish.getName());
            name.setPrefWidth(500);

            Label price = new Label(transformPrice(dish.getPrice()));
            price.setPrefWidth(70);
            price.setContentDisplay(ContentDisplay.CENTER);
            price.setAlignment(Pos.CENTER);

            Spinner<Integer> sAmount = new Spinner<>();
            sAmount.setPrefWidth(80);
            sAmount.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 20));
            sAmount.getValueFactory().setValue(1);

            Button btn = new Button("Hinzufügen");
            btn.setPrefWidth(100);
            btn.setOnAction((e) -> addToCart(dish, sAmount.getValue()));

            HBox hBox = new HBox(name, price, sAmount, btn);
            hBox.setAlignment(Pos.CENTER);
            hBox.setPrefHeight(40);
            hBox.setSpacing(10);

            if (bcolor = !bcolor) hBox.setStyle("-fx-background-color: #e3e3e3;");
            else hBox.setStyle("-fx-background-color: #eeeeee;");

            dishlist.getChildren().add(hBox);
        }
    }

    /**
     * adds dish as orderposition to list dishesInCart and calls synchronizeCart method
     *
     * @param dish   added to cart
     * @param amount of dish
     */
    private void addToCart(Dish dish, Integer amount) {
        for (OrderPosition o : dishesInCart) {
            if (o.getDish().getId() == dish.getId()) {
                o.setAmount(Math.min(o.getAmount() + amount, 20));
                synchronizeCart();
                return;
            }
        }
        dishesInCart.add(new OrderPosition(dish, amount));
        synchronizeCart();
    }

    /**
     * displays all orderpositions of list dishesInCart in GUI
     */
    public void synchronizeCart() {
        double total = 0;
        boolean bcolor = false;

        cart.getChildren().clear();

        for (OrderPosition o : dishesInCart) {
            total += o.getDish().getPrice() * o.getAmount();

            //name
            Label name = new Label(o.getAmount() + " x " + o.getDish().getId() + ". " + o.getDish().getName());
            name.setPrefWidth(185);
            HBox.setHgrow(name, Priority.ALWAYS);

            //price
            Label price = new Label(transformPrice(o.getDish().getPrice() * o.getAmount()));
            price.setAlignment(Pos.CENTER);
            price.setContentDisplay(ContentDisplay.CENTER);
            price.setPrefWidth(40);

            //delete button
            Button btn = new Button("Entfernen");
            btn.setPrefWidth(80);
            btn.setOnAction((e) -> {
                dishesInCart.remove(o);
                synchronizeCart();
            });

            HBox hbox = new HBox(name, price, btn);
            hbox.setAlignment(Pos.CENTER);
            hbox.setPrefHeight(40);
            hbox.setSpacing(10);

            //for switching background color every second row
            if (bcolor = !bcolor)
                hbox.setStyle("-fx-background-color: #e3e3e3;");
            else
                hbox.setStyle("-fx-background-color: #eeeeee;");

            VBox.setVgrow(hbox, Priority.ALWAYS);
            cart.getChildren().add(hbox);
        }

        lTotal.setText("Total: " + transformPrice(total));
    }

    /**
     * turns double into String, replaces dot with comma and makes amount of decimal places 2
     *
     * @param price double
     * @return transformed price as string
     */
    public static String transformPrice(double price) {
        String tr = "" + price;
        tr = tr.replace('.', ',');
        if (tr.substring(tr.indexOf(',') + 1).length() == 1)
            tr += "0";
        if (tr.substring(tr.indexOf(',') + 1).length() > 2)
            tr = tr.substring(0, tr.indexOf(',') + 2);
        return tr + "€";
    }

    /**
     * opens ordermodal and handles responds after modal closure through its status
     *
     * @param actionEvent ae
     * @throws IOException when scene not found
     */
    public void openOrderModal(ActionEvent actionEvent) throws IOException {
        if (dishesInCart.isEmpty()) {
            error_msg.setVisible(true);
            return;
        }
        error_msg.setVisible(false);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../../resources/views/customer/order_modal.fxml"));
        Parent root = loader.load();

        OrderModalController controller = loader.getController();
        controller.setDishesInCart(dishesInCart);
        controller.displayDishes();

        ModalController.ModalStatus result = ModalService.openModal((Stage) ((Node) actionEvent.getSource()).getScene().getWindow(), loader, root);

        if (result == ModalController.ModalStatus.SUCCESS) {
            AlertService.showAlert(Alert.AlertType.CONFIRMATION, "Erfolgreich", "Ihre Bestellung wurde erfolgreich aufgegeben", ButtonType.OK);
            dishesInCart.clear();
            synchronizeCart();
        } else if (result == ModalController.ModalStatus.FAILURE)
            AlertService.showAlert(Alert.AlertType.ERROR, "Fehler", "Ein Fehler ist aufgetreten, Ihre Bestellung konnte nicht aufgegeben werden.", ButtonType.OK);
    }

    /**
     * opens OrderHistoryModal
     * @throws IOException when scene not found
     */
    public void openOrderHistoryModal() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../../resources/views/customer/order_history_modal.fxml"));
        ModalService.openModal((Stage) dishlist.getScene().getWindow(), loader, loader.load());
    }

    /**
     * opens AccountEditModal
     * @throws IOException when scene not found
     */
    public void openAccountEditModal() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../../resources/views/customer/customer_profile_edit_modal.fxml"));
        ModalController.ModalStatus result = ModalService.openModal((Stage) dishlist.getScene().getWindow(), loader, loader.load());

        if (result == ModalController.ModalStatus.SUCCESS) {
            AlertService.showAlert(Alert.AlertType.CONFIRMATION, "Erfolgreich", "Änderungen wurden gespeichert", ButtonType.OK);
            menu_btn.setText(UserSessionSingleton.currentSession().getUser().getLastname() + ", " + UserSessionSingleton.currentSession().getUser().getFirstName());
        } else if (result == ModalController.ModalStatus.FAILURE)
            AlertService.showAlert(Alert.AlertType.ERROR, "Fehler", "Ein Fehler ist aufgetreten, Änderungen konnten nicht gespeichert werden.", ButtonType.OK);
    }

    /**
     * logs user out, sets UserSession to null and switches scene to Login-Scene
     * @throws IOException when login scene not found
     */
    public void logout() throws IOException {
        Stage stage = (Stage) dishlist.getScene().getWindow();
        stage.setScene(new Scene(FXMLLoader.load(Objects.requireNonNull(getClass().getResource("../../resources/views/login.fxml")))));
        UserSessionSingleton.currentSession().cleanUserSession();
    }
}
