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
import sample.dao.UserDAO;
import sample.dto.Dish;
import sample.dto.OrderPosition;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class MenuController implements Initializable {
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

    private List<OrderPosition> dishesInCart = new ArrayList<>();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        menu_btn.setText(UserDAO.loggedInUser.getLastname() + ", " + UserDAO.loggedInUser.getFirstName());
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
            Label lName = new Label(dish.getId() + ". " + dish.getName());
            lName.setPrefWidth(500);

            Label lPrice = new Label(transformPrice(dish.getPrice()));
            lPrice.setPrefWidth(70);
            lPrice.setContentDisplay(ContentDisplay.CENTER);
            lPrice.setAlignment(Pos.CENTER);

            Spinner<Integer> sAmount = new Spinner<>();
            sAmount.setPrefWidth(80);
            sAmount.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 20));
            sAmount.getValueFactory().setValue(1);

            Button btn = new Button("Hinzufügen");
            btn.setPrefWidth(100);
            btn.setOnAction((e) -> addToCart(dish, sAmount.getValue()));

            HBox hBox = new HBox(lName, lPrice, sAmount, btn);
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
        cart.getChildren().clear();
        double total = 0;
        boolean bcolor = false;
        for (OrderPosition o : dishesInCart) {
            total += o.getDish().getPrice() * o.getAmount();
            Label name = new Label(o.getAmount() + " x " + o.getDish().getId() + ". " + o.getDish().getName());
            name.setPrefWidth(185);
            HBox.setHgrow(name, Priority.ALWAYS);

            Label price = new Label(transformPrice(o.getDish().getPrice() * o.getAmount()));
            price.setAlignment(Pos.CENTER);
            price.setContentDisplay(ContentDisplay.CENTER);
            price.setPrefWidth(40);

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
     * @param actionEvent
     * @throws IOException
     */
    public void openOrderModal(ActionEvent actionEvent) {
        if (dishesInCart.isEmpty()) {
            error_msg.setVisible(true);
            return;
        }
        error_msg.setVisible(false);
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../../resources/views/ordermodal.fxml"));
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

            if (controller.getStatus() == OrderModalController.OrderStatus.SUCCESS) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Erfolgreich");
                alert.setContentText("Ihre Bestellung wurde erfolgreich aufgegeben");
                alert.getButtonTypes().setAll(ButtonType.OK);
                alert.show();
                dishesInCart.clear();
                synchronizeCart();
            } else if (controller.getStatus() == OrderModalController.OrderStatus.FAILURE) {
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

    public void showOrderHistory(ActionEvent actionEvent) {
        //TODO
    }

    public void openAccountEditModal(ActionEvent actionEvent) {
        //TODO
    }

    public void logout() throws IOException {
        Stage stage = (Stage) dishlist.getScene().getWindow();
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("../../resources/views/login.fxml"))));
        UserDAO.loggedInUser = null;
    }
}
