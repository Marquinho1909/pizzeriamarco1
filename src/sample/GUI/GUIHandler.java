package sample.GUI;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import sample.GUI.controller.*;
import sample.GUI.controller.modal.DishCreationModalController;
import sample.GUI.controller.modal.OrderHistoryModalController;
import sample.GUI.controller.modal.OrderModalController;
import sample.GUI.controller.modal.ProfileEditModalController;
import sample.data_logic.UserSessionSingleton;
import sample.data_logic.dto.*;
import sample.functional_logic.Controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

public class GUIHandler {
    private AdminPageController adminPageController;
    private CustomerPageController customerPageController;
    private LoginController loginController;
    private OrderModalController orderModalController;
    private RegisterController registerController;

    private DishCreationModalController dishCreationModalController;
    private OrderHistoryModalController orderHistoryModalController;
    private ProfileEditModalController profileEditModalController;

    private List<OrderPosition> cart;

    private final Controller controller;
    private final Router router;
    private final Stage stage;

    public GUIHandler(Controller controller, Stage stage) {
        this.controller = controller;
        this.stage = stage;
        this.router = new Router(stage);
        cart = new ArrayList<>();
    }

    public void start() {
        initControllers();
        initObserver();
        initFXMLLoader();
        startApplication();
    }

    public void startApplication() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../../resources/views/login.fxml"));
            loader.setController(loginController);

        stage.setTitle("Pizzeria Marco");
        stage.setMinHeight(400);
        stage.setMinWidth(640);
        stage.setScene(new Scene(loader.load()));
        stage.centerOnScreen();
        stage.show();
        } catch (IOException e) {
        e.printStackTrace();
    }
    }

    public void initControllers() {
        adminPageController = new AdminPageController(this);
        customerPageController = new CustomerPageController(this);
        loginController = new LoginController(this);
        orderModalController = new OrderModalController(this);
        registerController = new RegisterController(this);
        dishCreationModalController = new DishCreationModalController(this);
        orderHistoryModalController = new OrderHistoryModalController(this);
        profileEditModalController = new ProfileEditModalController(this);
    }

    public void initObserver() {
        controller.setCategoryObserver(List.of(customerPageController));
        controller.setCouponObserver(List.of());
        controller.setDishObserver(List.of(adminPageController, customerPageController));
        controller.setOrderObserver(List.of(adminPageController));
        controller.setUserObserver(List.of(adminPageController, customerPageController));
    }

    public void initFXMLLoader() {
        Object[][] fxml = {{
            customerPageController, "customer/customer_page.fxml"},
                {adminPageController, "admin/admin_page.fxml"}};

        for(Object[] c: fxml) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../../resources/views/" + c[1]));
            loader.setController(c[0]);
            try {
                loader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void logout() {
        UserSessionSingleton.currentSession().cleanUserSession();
    }

    public void register(Customer customer) throws SQLException {
        UserSessionSingleton.currentSession().setUser(customer);
        controller.saveCustomer(customer);
    }

    public boolean login(String email, String password) {
        Optional<User> user = getUsers().stream().filter(u ->
                u.getEmail().equals(email) && u.getPassword().equals(password)).findFirst();

        if (user.isPresent()) {
            UserSessionSingleton.currentSession().setUser(user.get());
            return true;
        }
        return false;
    }

    public boolean canDishBeDeleted(int id) {
        return getOrders().stream().noneMatch(o -> o.getOrderpositions().stream().anyMatch(p -> p.getDish().getId() == id));
    }

    public Coupon getCouponOfLoggedInUser() {
        if (controller.isUserCustomer())
            return ((Customer) UserSessionSingleton.currentSession().getUser()).getCoupon();
        else return null;
    }

    public void order(boolean coupon, Date date) throws SQLException {
        controller.saveOrder(
                new Order(
                        cart,
                        date,
                        UserSessionSingleton.currentSession().getUser(),
                        coupon ? getCouponOfLoggedInUser().getValue() : 0
                )
        );

        if (coupon) {
            controller.deleteCouponOfLoggedInUser();
        }
        getCart().clear();
    }

    public boolean doesCategoryExist(String name) {
        return getCategories().stream().anyMatch(c -> c.getName().equals(name));
    }

    public boolean isUserCustomer() {
        return controller.isUserCustomer();
    }

    public boolean isUserAdmin() {
        return controller.isUserAdmin();
    }

    public boolean canAllCategoriesBeDeleted(List<Category> categories) {
        // check if dishes have any selected category as category, if so deletion is cancelled
        for (Category category : categories)
            if (getDishes().stream().anyMatch(d -> d.getCategories().contains(category))) {
                return false;
            }
        return true;
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

    public boolean addedToExistingOrder(Dish dish, Integer amount) {
        for (OrderPosition o : cart) {
            if (o.getDish().getId() == dish.getId()) {
                o.setAmount(Math.min(o.getAmount() + amount, 20));
                return true;
            }
        }
        return false;
    }

    public void addOrderToCart(OrderPosition orderPosition) {
        cart.add(orderPosition);
    }

    public List<OrderPosition> getCart() {
        return cart;
    }

    public void removeOrderFromCart(int id) {
        cart.removeIf(c -> c.getId() == id);
    }

    public void deleteOrderHistory() throws SQLException {
        controller.deleteOrderHistory();
    }

    public void deleteDish(int id) throws SQLException {
        controller.deleteDish(id);
    }

    public void deleteCategories(List<Category> checked_categories) throws SQLException {
        controller.deleteCategories(checked_categories);
    }

    public void saveDish(Dish dish) throws SQLException {
        controller.saveDish(dish);
    }

    public void saveProfileChanges(int id, User user) throws SQLException {
        controller.updateProfile(id, user);
    }

    public void makeCustomerAdmin(int id) throws SQLException {
        User user = getUsers().stream().filter(u -> u.getId() == id).findFirst().orElseThrow();

        controller.updateUser(id, new Admin(
                user.getId(),
                user.getFirstName(),
                user.getLastname(),
                user.getGender(),
                user.getEmail(),
                user.getPassword()));
    }

    public void setDishActivation(int id) throws SQLException {
        Dish dish = getDishes().stream().filter(d -> d.getId() == id).findFirst().orElseThrow();

        dish.setActive(!dish.isActive());
        controller.updateDish(id, dish);
    }

    //getters for lists

    public List<Customer> getCustomers() {
        return getUsers().stream().filter(u -> u.getClass().equals(Customer.class)).map(c -> (Customer) c).collect(Collectors.toList());
    }

    public List<User> getUsers() {
        return controller.getUsers();
    }

    public List<Order> getOrders() {
        return controller.getOrders();
    }

    public List<Dish> getDishes() {
        return controller.getDishes();
    }

    public List<Category> getCategories() {
        return controller.getCategories();
    }

    public List<Order> getOrdersFromLoggedInUser() {
        return controller.getOrders().stream()
                .filter(o -> o.getUser().getId() == UserSessionSingleton.currentSession().getUser().getId())
                .collect(Collectors.toList());
    }

    public void saveCategory(Category category) throws SQLException {
        controller.saveCategory(category);
    }

    public void navigateToRegisterPage() throws IOException {
        router.navigateTo(registerController, "register.fxml");
    }

    public void navigateToLoginPage() throws IOException {
        router.navigateTo(loginController, "login.fxml");
    }

    public void navigateToAdminPage() throws IOException {
        router.navigateTo(adminPageController, "admin/admin_page.fxml");
    }

    public void navigateToCustomerPage() throws IOException {
        router.navigateTo(customerPageController, "customer/customer_page.fxml");
    }

    public Modal.ModalStatus openDishCreationModal() throws IOException {
        return router.openModal(dishCreationModalController, "admin/dish_creation_modal.fxml");
    }

    public Modal.ModalStatus openAdminProfileEditModal() throws IOException {
        return router.openModal(profileEditModalController, "admin/admin_profile_edit_modal.fxml");
    }

    public Modal.ModalStatus openCustomerProfileEditModal() throws IOException {
        return router.openModal(profileEditModalController, "customer/customer_profile_edit_modal.fxml");
    }

    public Modal.ModalStatus openOrderHistoryModal() throws IOException {
        return router.openModal(orderHistoryModalController, "customer/order_history_modal.fxml");
    }

    public Modal.ModalStatus openOrderModal() throws IOException {
        return router.openModal(orderModalController, "customer/order_modal.fxml");
    }

}
