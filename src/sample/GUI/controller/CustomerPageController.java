package sample.GUI.controller;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;
import sample.GUI.AlertService;
import sample.GUI.GUIHandler;
import sample.GUI.ParentController;
import sample.data_logic.UserSessionSingleton;
import sample.data_logic.dto.Category;
import sample.data_logic.dto.Dish;
import sample.data_logic.dto.OrderPosition;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

/**
 * Observer that observes ProfileEditController, when notified changes menu button name to newly edited
 */
public class CustomerPageController extends ParentController {
    @FXML private ComboBox<Category> categories_cb;
    @FXML private Label error_msg;
    @FXML private VBox dishlist;
    @FXML private VBox cart;
    @FXML private Label lTotal;
    @FXML private Menu menu_btn;

    public CustomerPageController(GUIHandler guiHandler) {
        super(guiHandler);
    }

    @Override
    public void update() {
        guiHandler.clearCart();
        if (UserSessionSingleton.currentSession().getUser() != null)
            menu_btn.setText(UserSessionSingleton.currentSession().getUser().getLastname() + ", " + UserSessionSingleton.currentSession().getUser().getFirstName());

        displayCategories();
        displayDishes();
        displayCart();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        update();
    }

    /**
     * fetches and displays all categories in filtering option of combobox
     */
    public void displayCategories() {
        categories_cb.getSelectionModel().clearSelection();
        categories_cb.getItems().clear();
        categories_cb.getItems().add(new Category(0, "Alle anzeigen"));
        categories_cb.setValue(categories_cb.getItems().get(0));

        categories_cb.setConverter(new StringConverter<>() {
            @Override
            public String toString(Category category) {
                if (category == null)
                    return "  ";
                return category.getName();
            }

            @Override
            public Category fromString(String s) {
                return new Category(s);
            }
        });

        categories_cb.getItems().addAll(guiHandler.getCategories());
    }

    /**
     * fetches and displays all dishes from database in GUI
     */
    public void displayDishes() {
        dishlist.getChildren().clear();

        List<Dish> filteredDishes = guiHandler.getDishes().stream().filter(d -> d.isActive()).collect(Collectors.toList());
        List<Category> shownCategories;

        if (categories_cb.getValue() != null && categories_cb.getValue().getId() != 0) {
            filteredDishes = guiHandler.getDishes().stream().filter(d -> d.getCategories().contains(categories_cb.getValue())).collect(Collectors.toList());
            shownCategories = List.of(categories_cb.getValue());
        } else {
            shownCategories = guiHandler.getCategories();
        }

        boolean colorDarker;

        for (Category c : shownCategories) {
            colorDarker = false;
            createCategoryEntry(c);
            List<Dish> caDishes = filteredDishes.stream().filter(d -> d.getCategories().contains(c)).collect(Collectors.toList());
            for (Dish dish : caDishes) {
                createDishEntry(dish, colorDarker);
                colorDarker = !colorDarker;
            }
        }
        if (categories_cb.getValue() != null && categories_cb.getValue().getId() == 0) {
            List<Dish> uncat = guiHandler.getDishes().stream().filter(d -> d.getCategories().isEmpty()).collect(Collectors.toList());
            if (!uncat.isEmpty()) {
                createCategoryEntry(new Category("Unkategorisiert"));
                colorDarker = false;
                for (Dish dish : uncat) {
                    createDishEntry(dish, colorDarker);
                    colorDarker = !colorDarker;
                }
            }
        }

    }

    /**
     * creates an entry of category in menu
     *
     * @param category to create entry of
     */
    private void createCategoryEntry(Category category) {
        Label caName = new Label(category.getName());
        caName.setStyle("-fx-font-weight: bold;");
        caName.setStyle("-fx-font-size: 25;");

        HBox cahBox = new HBox(caName);
        cahBox.setAlignment(Pos.CENTER);
        cahBox.setPrefHeight(40);

        dishlist.getChildren().add(cahBox);
    }

    /**
     * creates an entry of dish in menu
     *
     * @param dish        to create entry of
     * @param colorDarker background color to enable switching colors, between light and darker color
     */
    private void createDishEntry(Dish dish, boolean colorDarker) {
        Label name = new Label(dish.getId() + ". " + dish.getName());
        name.setPrefWidth(500);

        Label price = new Label(GUIHandler.transformPrice(dish.getPrice()));
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

        if (colorDarker) hBox.setStyle("-fx-background-color: #e3e3e3;");
        else hBox.setStyle("-fx-background-color: #eeeeee;");

        dishlist.getChildren().add(hBox);
    }

    /**
     * adds dish as orderposition to list dishesInCart and calls synchronizeCart method
     *
     * @param dish   added to cart
     * @param amount of dish
     */
    private void addToCart(Dish dish, Integer amount) {
        if (!guiHandler.addedToExistingOrder(dish, amount)) {
            guiHandler.addOrderToCart(new OrderPosition(dish, amount));
        }
        displayCart();
    }

    /**
     * displays all orderpositions of list dishesInCart in GUI
     */
    public void displayCart() {
        double total = 0;
        boolean bcolor = false;

        cart.getChildren().clear();

        for (OrderPosition o : guiHandler.getCart()) {
            total += o.getDish().getPrice() * o.getAmount();

            //name
            Label name = new Label(o.getAmount() + " x " + o.getDish().getId() + ". " + o.getDish().getName());
            name.setPrefWidth(185);
            HBox.setHgrow(name, Priority.ALWAYS);

            //price
            Label price = new Label(GUIHandler.transformPrice(o.getDish().getPrice() * o.getAmount()));
            price.setAlignment(Pos.CENTER);
            price.setContentDisplay(ContentDisplay.CENTER);
            price.setPrefWidth(40);

            //delete button
            Button btn = new Button("Entfernen");
            btn.setPrefWidth(80);
            btn.setOnAction((e) -> {
                guiHandler.removeOrderFromCart(o.getDish().getId());
                displayCart();
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

        lTotal.setText("Total: " + GUIHandler.transformPrice(total));
    }

    /**
     * opens ordermodal and handles responds after modal closure through its status
     */
    public void openOrderModal() {
        if (guiHandler.getCart().isEmpty()) {
            error_msg.setVisible(true);
            return;
        }
        error_msg.setVisible(false);

        try {
            Modal.ModalStatus result = guiHandler.openOrderModal();

            if (result == Modal.ModalStatus.SUCCESS) {
                AlertService.showAlert(Alert.AlertType.CONFIRMATION, "Erfolgreich", "Ihre Bestellung wurde erfolgreich aufgegeben", ButtonType.OK);
                guiHandler.getCart().clear();

            } else if (result == Modal.ModalStatus.FAILURE)
                AlertService.showAlert(Alert.AlertType.ERROR, "Fehler", "Ein Fehler ist aufgetreten, Ihre Bestellung konnte nicht aufgegeben werden.", ButtonType.OK);
        } catch (IOException e) {
            e.printStackTrace();
            AlertService.showError();
        }
    }

    /**
     * opens OrderHistoryModal
     */
    public void openOrderHistoryModal() {
        try {
            guiHandler.openOrderHistoryModal();
        } catch (IOException e) {
            e.printStackTrace();
            AlertService.showError();
        }
    }

    /**
     * opens AccountEditModal
     */
    public void openAccountEditModal() {
        try {
            Modal.ModalStatus result = guiHandler.openCustomerProfileEditModal();
            if (result == Modal.ModalStatus.SUCCESS) {
                AlertService.showAlert(Alert.AlertType.CONFIRMATION, "Erfolgreich", "Änderungen wurden gespeichert", ButtonType.OK);
            } else if (result == Modal.ModalStatus.FAILURE)
                AlertService.showAlert(Alert.AlertType.ERROR, "Fehler", "Ein Fehler ist aufgetreten, Änderungen konnten nicht gespeichert werden.", ButtonType.OK);
        } catch (IOException e) {
            e.printStackTrace();
            AlertService.showError();
        }
    }

    /**
     * logs user out, sets UserSession to null and switches scene to Login-Scene
     */
    public void logout() {
        try {
            guiHandler.logout();
            guiHandler.navigateToLoginPage();
        } catch (IOException e) {
            e.printStackTrace();
            AlertService.showError();
        }
    }

}
