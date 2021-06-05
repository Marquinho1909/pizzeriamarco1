package sample.functional_logic.controllers;

import javafx.beans.property.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Menu;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import sample.data_logic.DAOFactory;
import sample.data_logic.dao.DishDAO;
import sample.data_logic.dao.OrderDAO;
import sample.data_logic.dao.UserDAO;
import sample.data_logic.dto.*;
import sample.functional_logic.AlertService;
import sample.functional_logic.ModalService;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

public class AdminPageController implements Initializable {
    @FXML private TableView<TableDish> table_dish;
    @FXML private TableView<TableCustomer> table_user;
    @FXML private TableView<TableOrder> table_order;
    @FXML private Menu menu_btn;

    private UserDAO userDAO;
    private OrderDAO orderDAO;
    private DishDAO dishDAO;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        userDAO = (UserDAO) DAOFactory.getInstance().getDAO("User");
        orderDAO = (OrderDAO) DAOFactory.getInstance().getDAO("Order");
        dishDAO = (DishDAO) DAOFactory.getInstance().getDAO("Dish");

        setMenuBtnNameToCurrent();
            updateCustomerTable();
            updateOrderTable();
            updateDishTable();
    }

    /**
     * gets all customers and displays them in table
     */
    public void updateCustomerTable() {
        table_user.getItems().clear();

        List<Customer> customers;
        try {
            customers = userDAO.getAllCustomers();
            for (Customer c : customers)
                table_user.getItems().add(new TableCustomer(
                        c.getId(),
                        c.getFirstName(),
                        c.getLastname(),
                        "" + c.getGender(),
                        c.getAddress().getStreet() + " " + c.getAddress().getHouseNumber() + ", " + c.getAddress().getZipCode(),
                        c.getEmail()
                ));
        } catch (SQLException e) {
            e.printStackTrace();
            AlertService.showError();
        }

    }

    /**
     * gets all orders and displays them in table
     */
    public void updateOrderTable()  {
        table_order.getItems().clear();

        List<Order> orders;
        try {
            orders = orderDAO.getAll();
            for (Order o : orders)
                table_order.getItems().add(new TableOrder(
                        o.getId(),
                        o.getOrderDate().toString(),
                        o.getOrderpositions(),
                        o.getDiscount(),
                        o.getUser().getId()
                ));
        } catch (SQLException e) {
            e.printStackTrace();
            AlertService.showError();
        }

    }

    /**
     * gets all dishes and displays them in table
     */
    public void updateDishTable() {
        table_dish.getItems().clear();

        List<Dish> dishes = null;
        try {
            dishes = dishDAO.getAll();
        } catch (SQLException e) {
            e.printStackTrace();
            AlertService.showError();
        }
        for (Dish d : dishes)
            table_dish.getItems().add(new TableDish(
                    d.getId(),
                    d.getName(),
                    d.getCategories(),
                    d.getPrice(),
                    d.isActive()
                    ));
    }

    /**
     * converts an customer to admin
     */
    public void makeUserAdmin() {
        TableCustomer tc = table_user.getSelectionModel().getSelectedItem();
        if (tc == null) return;

        ButtonType result = AlertService.showAlert(Alert.AlertType.CONFIRMATION, "User zum Admin machen", "Wenn der User " + tc.getFirstname() + " " + tc.getLastname() +
                " zum Admin ernannt wird, wird er sämtliche Rechte haben und kann nicht mehr als Kunde agieren. " +
                "Wollen Sie trotzdem fortfahren?", ButtonType.YES, ButtonType.NO);

        if (result.equals(ButtonType.YES)) {
            try {
                userDAO.makeCustomerAdmin(tc.getId());
                updateCustomerTable();
            } catch (SQLException e) {
                e.printStackTrace();
                AlertService.showError();
            }
        }
    }

    /**
     * Opens DishCreationModal and updates dish table after closing
     */
    public void openDishCreationModal() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../../../resources/views/admin/dish_creation_modal.fxml"));
        try {
            ModalService.openModal((Stage) table_dish.getScene().getWindow(), loader, loader.load());
            updateDishTable();
        } catch (IOException e) {
            e.printStackTrace();
            AlertService.showError();
        }

    }

    /**
     * logs user out, set UserSession to null and redirects to login page
     */
    public void logout() {
        Stage stage = (Stage) table_dish.getScene().getWindow();
        try {
            stage.setScene(new Scene(FXMLLoader.load(Objects.requireNonNull(getClass().getResource("../../../resources/views/login.fxml")))));
        } catch (IOException e) {
            e.printStackTrace();
            AlertService.showError();
        }
        UserSessionSingleton.currentSession().cleanUserSession();
    }

    /**
     * opens AccountEditModal
     */
    public void openAccountEditModal() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../../../resources/views/admin/admin_profile_edit_modal.fxml"));
        try {
            ModalController.ModalStatus result = ModalService.openModal((Stage) table_dish.getScene().getWindow(), loader, loader.load());
            if (result == ModalController.ModalStatus.SUCCESS) {
                AlertService.showAlert(Alert.AlertType.CONFIRMATION, "Erfolgreich", "Änderungen wurden gespeichert", ButtonType.OK);
                setMenuBtnNameToCurrent();
            } else if (result == ModalController.ModalStatus.FAILURE)
                AlertService.showAlert(Alert.AlertType.ERROR, "Fehler", "Ein Fehler ist aufgetreten, Änderungen konnten nicht gespeichert werden.", ButtonType.OK);
        } catch (IOException e) {
            e.printStackTrace();
            AlertService.showError();
        }

    }

    /**
     * deletes selected dish from dish table
     */
    public void deleteDish() {
        TableDish td = table_dish.getSelectionModel().getSelectedItem();
        if (td == null) return;

        try {
            List<Order> orders = orderDAO.getAll();
            if (orders.stream().anyMatch(o -> o.getOrderpositions().stream().anyMatch(p -> p.getDish().getId() == td.getId()))) {
                AlertService.showAlert(Alert.AlertType.ERROR, "Fehlgeschlagen",
                        "Mindestens eine Bestellung in der Historie enthält dieses Gericht. Löschen Sie die Historie oder deaktivieren Sie das Gericht einfach", ButtonType.OK);
                return;
            }

            ButtonType result = AlertService.showAlert(Alert.AlertType.CONFIRMATION, "Gericht löschen",
                    "Soll das Gericht " + td.getName() + " wirklich gelöscht werden? Alternativ können Sie es auch deaktivieren, " +
                            "sodass Kunden es weder sehen noch bestellen können bis zur Reaktivierung.", ButtonType.YES, ButtonType.NO);

            if (result.equals(ButtonType.YES)) {
                dishDAO.delete(td.getId());
                updateDishTable();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            AlertService.showError();
        }
    }

    /**
     * sets displayed name in menu button to currently logged in user
     */
    private void setMenuBtnNameToCurrent() {
        menu_btn.setText(UserSessionSingleton.currentSession().getUser().getLastname() + ", " + UserSessionSingleton.currentSession().getUser().getFirstName());
    }

    /**
     * deletes all orders and their orderpositions
     */
    public void deleteOrderHistory() {
        ButtonType result = AlertService.showAlert(Alert.AlertType.CONFIRMATION, "Bestellhistorie löschen", "Soll die Bestellhistorie wirklich gelöscht werden? Die Löschung kann nicht mehr rückgängig gemacht werden.", ButtonType.YES, ButtonType.NO);

        if (result.equals(ButtonType.YES)) {
            try {
                orderDAO.deleteAll();
                updateOrderTable();
            } catch (SQLException e) {
                e.printStackTrace();
                AlertService.showError();
            }
        }
    }

    /**
     * either activates or deactivates selected dish
     */
    public void changeDishActivation() {
        TableDish td = table_dish.getSelectionModel().getSelectedItem();
        String content = td.getActive().equals("Inaktiv") ? "Soll das Gericht wirklich aktiviert werden? Alle Kunden können es dann sehen und bestellen."
                : "Soll das Gericht wirklich deaktiviert werden? Danach ist es bis zu Reaktivierung nicht mehr bestellbar.";

        ButtonType result = AlertService.showAlert(Alert.AlertType.CONFIRMATION, "Gericht-" + (td.getActive().equals("Inaktiv") ? "Aktivierung" : "Deaktivierung"), content, ButtonType.YES, ButtonType.NO);

        if (result.equals(ButtonType.YES)) {
            try {
                dishDAO.setActivation(td.getActive().equals("Inaktiv"), td.getId());
                updateDishTable();
            } catch (SQLException e) {
                e.printStackTrace();
                AlertService.showError();
            }

        }
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
            this.orderlist = new SimpleStringProperty(s.substring(0, s.length() - 2));
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
        private final StringProperty active;

        TableDish(int id, String name, List<Category> categories, double price, boolean active) {
            this.id = new SimpleIntegerProperty(id);
            this.name = new SimpleStringProperty(name);
            this.active = new SimpleStringProperty(active ? "Aktiv" : "Inaktiv");

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
        public String getActive() { return active.get(); }

    }
}
