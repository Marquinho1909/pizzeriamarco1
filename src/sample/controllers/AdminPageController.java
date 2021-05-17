package sample.controllers;

import javafx.beans.property.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Menu;
import javafx.scene.control.TableView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import sample.dao.DishDAO;
import sample.dao.OrderDAO;
import sample.dao.UserDAO;
import sample.dto.*;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

public class AdminPageController implements Initializable {
    @FXML
    private TableView<TableDish> table_dish;
    @FXML
    private TableView<TableCustomer> table_user;
    @FXML
    private TableView<TableOrder> table_order;
    @FXML
    private Menu menu_btn;

    private UserDAO userDAO;
    private OrderDAO orderDAO;
    private DishDAO dishDAO;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        userDAO = new UserDAO();
        orderDAO = new OrderDAO();
        dishDAO = new DishDAO();

        menu_btn.setText(UserSession.currentSession().getUser().getLastname() + ", " + UserSession.currentSession().getUser().getFirstName());
        updateCustomerTable();
        updateOrderTable();
        updateDishTable();
    }

    /**
     * gets all customers and displays them in table
     */
    public void updateCustomerTable() {
        table_user.getItems().clear();

        List<Customer> customers = userDAO.getAllCustomers();
        for (Customer c : customers)
            table_user.getItems().add(new TableCustomer(
                    c.getId(),
                    c.getFirstName(),
                    c.getLastname(),
                    "" + c.getGender(),
                    c.getAddress().getStreet() + " " + c.getAddress().getHouseNumber() + ", " + c.getAddress().getZipCode(),
                    c.getEmail()
            ));
    }

    /**
     * gets all orders and displays them in table
     */
    public void updateOrderTable() {
        table_order.getItems().clear();

        List<Order> orders = orderDAO.getAll();
        for (Order o : orders)
            table_order.getItems().add(new TableOrder(
                    o.getId(),
                    o.getOrderDate().toString(),
                    o.getOrderpositions(),
                    o.getDiscount(),
                    o.getUser().getId()
            ));
    }

    /**
     * gets all dishes and displays them in table
     */
    public void updateDishTable() {
        table_dish.getItems().clear();

        List<Dish> dishes = dishDAO.getAll();
        for (Dish d : dishes)
            table_dish.getItems().add(new TableDish(
                    d.getId(),
                    d.getName(),
                    d.getCategories(),
                    d.getPrice()
            ));
    }

    /**
     * converts an customer to admin
     */
    public void makeUserAdmin() {
        TableCustomer tc = table_user.getSelectionModel().getSelectedItem();
        if (tc == null) return;

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("User zum Admin machen");
        alert.setContentText("Wenn der User " + tc.getFirstname() + " " + tc.getLastname() +
                " zum Admin ernannt wird, wird er sämtliche Rechte haben und kann nicht mehr als Kunde agieren. " +
                "Wollen Sie trotzdem fortfahren?");
        alert.getButtonTypes().setAll(ButtonType.YES, ButtonType.NO);
        alert.showAndWait();

        if (alert.getResult().equals(ButtonType.YES) && userDAO.makeCustomerAdmin(tc.getId()))
            updateCustomerTable();
    }

    public void openDishCreationModal() {
        try {
            Stage modal = new Stage();
            modal.setScene(new Scene(FXMLLoader.load(Objects.requireNonNull(getClass().getResource("../../resources/views/admin/dish_creation_modal.fxml")))));
            modal.initOwner(table_dish.getScene().getWindow());
            modal.initModality(Modality.APPLICATION_MODAL);
            modal.showAndWait();
            updateDishTable();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void logout() {
        try {
            Stage stage = (Stage) table_dish.getScene().getWindow();
            stage.setScene(new Scene(FXMLLoader.load(Objects.requireNonNull(getClass().getResource("../../resources/views/login.fxml")))));
            UserSession.currentSession().cleanUserSession();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void openAccountEditModal() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../../resources/views/admin/admin_profile_edit_modal.fxml"));
            Parent root = loader.load();

            ProfileEditModalController controller = loader.getController();

            Stage main = (Stage) table_dish.getScene().getWindow();
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

    public void deleteDish() {
        TableDish td = table_dish.getSelectionModel().getSelectedItem();

        if (td == null) return;

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Gericht löschen");
        alert.setContentText("Soll das Gericht " + td.getName() + " wirklich gelöscht werden?");
        alert.getButtonTypes().setAll(ButtonType.YES, ButtonType.NO);
        alert.showAndWait();

        if (alert.getResult().equals(ButtonType.YES) && new DishDAO().delete(td.getId()))
            updateDishTable();

    }

    /**
     * Class for displaying Customer in table_customer
     */
    public static class TableCustomer {
        private final IntegerProperty id;
        private final StringProperty firstname;
        private final StringProperty lastname;
        private final StringProperty gender;
        private final StringProperty fullAddress;
        private final StringProperty email;

        TableCustomer(int id, String firstname, String lastname, String gender, String fullAddress, String email) {
            this.id = new SimpleIntegerProperty(id);
            this.firstname = new SimpleStringProperty(firstname);
            this.lastname = new SimpleStringProperty(lastname);
            this.gender = new SimpleStringProperty(gender);
            this.fullAddress = new SimpleStringProperty(fullAddress);
            this.email = new SimpleStringProperty(email);
        }

        public int getId() {
            return id.get();
        }

        public void setId(int id) {
            this.id.set(id);
        }

        public String getFirstname() {
            return firstname.get();
        }

        public String getLastname() {
            return lastname.get();
        }

        public String getGender() {
            return gender.get();
        }

        public String getFullAddress() {
            return fullAddress.get();
        }

        public String getEmail() {
            return email.get();
        }
    }

    /**
     * Class for displaying Order in table_order
     */
    public static class TableOrder {
        private final IntegerProperty id;
        private final StringProperty date;
        private final StringProperty orderlist;
        private final StringProperty total;
        private final IntegerProperty userid;

        TableOrder(int id, String date, List<OrderPosition> orderlist, double discount, int userid) {
            this.id = new SimpleIntegerProperty(id);
            this.date = new SimpleStringProperty(date);
            this.userid = new SimpleIntegerProperty(userid);

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
            this.orderlist = new SimpleStringProperty(s.substring(0, s.length() - 3));
        }

        public int getId() {
            return id.get();
        }

        public void setId(int id) {
            this.id.set(id);
        }

        public String getOrderlist() {
            return orderlist.get();
        }

        public String getTotal() {
            return total.get();
        }

        public int getUserid() {
            return userid.get();
        }

        public String getDate() {
            return date.get();
        }
    }

    /**
     * Class for displaying Dish in table_dish
     */
    public static class TableDish {
        private final IntegerProperty id;
        private final StringProperty name;
        private final StringProperty categories;
        private final StringProperty price;

        TableDish(int id, String name, List<Category> categories, double price) {
            this.id = new SimpleIntegerProperty(id);
            this.name = new SimpleStringProperty(name);

            StringBuilder s = new StringBuilder();
            if (categories.size() > 0) {

                for (Category c : categories)
                    s.append(c.getName()).append(", ");

                this.categories = new SimpleStringProperty(s.substring(0, s.length() - 2));

            } else this.categories = new SimpleStringProperty("");
            this.price = new SimpleStringProperty(CustomerPageController.transformPrice(price));
        }

        public int getId() {
            return id.get();
        }

        public String getName() {
            return name.get();
        }

        public String getCategories() {
            return categories.get();
        }

        public String getPrice() {
            return price.get();
        }
    }
}
