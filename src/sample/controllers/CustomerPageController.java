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
import javafx.stage.Modality;
import javafx.stage.Stage;
import sample.dao.DishDAO;
import sample.dto.Dish;
import sample.dto.OrderPosition;
import sample.dto.UserSession;

import java.io.IOException;
import java.net.URL;
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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        menu_btn.setText(UserSession.currentSession().getUser().getLastname() + ", " + UserSession.currentSession().getUser().getFirstName());
        synchronizeDishlist();
    }

    /**
     * fetches and displays all dishes from database in GUI
     */
    public void synchronizeDishlist() {
        DishDAO dishDAO = new DishDAO();
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
     */
    public void openOrderModal(ActionEvent actionEvent) {
        if (dishesInCart.isEmpty()) {
            error_msg.setVisible(true);
            return;
        }
        error_msg.setVisible(false);
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../../resources/views/customer/ordermodal.fxml"));
            Parent root = loader.load();

            OrderModalController controller = loader.getController();
            controller.setDishesInCart(dishesInCart);
            controller.displayDishes();

            Stage main = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            Stage modal = new Stage();
            modal.setScene(new Scene(root));
            modal.initOwner(main);
            modal.initModality(Modality.APPLICATION_MODAL);
            modal.showAndWait();

            if (controller.getStatus() == ModalStatus.SUCCESS) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Erfolgreich");
                alert.setContentText("Ihre Bestellung wurde erfolgreich aufgegeben");
                alert.getButtonTypes().setAll(ButtonType.OK);
                alert.show();
                dishesInCart.clear();
                synchronizeCart();
            } else if (controller.getStatus() == ModalStatus.FAILURE) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Fehler");
                alert.setContentText("Ein Fehler ist aufgetreten, Ihre Bestellung konnte nicht aufgegeben werden.");
                alert.getButtonTypes().setAll(ButtonType.OK);
                alert.show();
            }
        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Fehler");
            alert.setContentText("Ein Fehler ist aufgetreten, bitte wenden Sie sich an den nicht vorhandenen Support0.");
            alert.getButtonTypes().setAll(ButtonType.OK);
            alert.show();
        }

    }

    public void showOrderHistory() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../../resources/views/customer/order_history.fxml"));
            Parent root = loader.load();

            Stage main = (Stage) dishlist.getScene().getWindow();
            Stage modal = new Stage();
            modal.setScene(new Scene(root));
            modal.initOwner(main);
            modal.initModality(Modality.APPLICATION_MODAL);
            modal.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void openAccountEditModal() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../../resources/views/customer/customer_profile_edit_modal.fxml"));
            Parent root = loader.load();

            ProfileEditModalController controller = loader.getController();

            Stage main = (Stage) dishlist.getScene().getWindow();
            Stage modal = new Stage();
            modal.setScene(new Scene(root));
            modal.initOwner(main);
            modal.initModality(Modality.APPLICATION_MODAL);
            modal.showAndWait();

            if (controller.getStatus() == ModalStatus.SUCCESS) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Erfolgreich");
                alert.setContentText("Änderungen wurden gespeichert");
                alert.getButtonTypes().setAll(ButtonType.OK);
                alert.show();
                menu_btn.setText(UserSession.currentSession().getUser().getLastname() + ", " + UserSession.currentSession().getUser().getFirstName());
            } else if (controller.getStatus() == ModalStatus.FAILURE) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Fehler");
                alert.setContentText("Ein Fehler ist aufgetreten, Änderungen konnten nicht gespeichert werden.");
                alert.getButtonTypes().setAll(ButtonType.OK);
                alert.show();
            }
        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Fehler");
            alert.setContentText("Ein Fehler ist aufgetreten, bitte wenden Sie sich an den nicht vorhandenen Support.");
            alert.getButtonTypes().setAll(ButtonType.OK);
            alert.show();
        }
    }

    public void logout() throws IOException {
        Stage stage = (Stage) dishlist.getScene().getWindow();
        stage.setScene(new Scene(FXMLLoader.load(Objects.requireNonNull(getClass().getResource("../../resources/views/login.fxml")))));
        UserSession.currentSession().cleanUserSession();
    }
}
