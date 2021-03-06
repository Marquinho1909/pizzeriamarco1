package sample.functional_logic;

import sample.GUI.GUIHandler;
import sample.GUI.ParentController;
import sample.data_logic.UserSessionSingleton;
import sample.data_logic.dto.*;
import sample.functional_logic.service.*;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class Controller {
    UserService userService;
    CategoryService categoryService;
    CouponService couponService;
    OrderService orderService;
    DishService dishService;

    public Controller(UserService userService, CategoryService categoryService, CouponService couponService, OrderService orderService, DishService dishService) {
        this.userService = userService;
        this.categoryService = categoryService;
        this.couponService = couponService;
        this.orderService = orderService;
        this.dishService = dishService;
    }

    public void start(GUIHandler guiHandler) {
        userService.notifyObservers();
        categoryService.notifyObservers();
        couponService.notifyObservers();
        orderService.notifyObservers();
        dishService.notifyObservers();
    }


    // UPDATE
    public void updateProfile(int id, User user) throws SQLException {
        user.setId(id);
        UserSessionSingleton.currentSession().setUser(user);
        userService.update(id, user);
    }

    public void updateUser(int id, User user) throws SQLException { userService.update(id, user); }
    public void updateDish(int id, Dish dish) throws SQLException { dishService.update(id, dish); }

    // DELETE
    public void deleteCouponOfLoggedInUser() throws SQLException {
        if (isUserCustomer()) {
            couponService.delete(((Customer) (UserSessionSingleton.currentSession().getUser())).getCoupon().getId());
            loadLoggedInUser();
        }
        userService.load();
    }

    public void deleteCategories(List<Category> categories) throws SQLException { categoryService.delete(categories); }
    public void deleteOrderHistory() throws SQLException { orderService.deleteAll(); }
    public void deleteDish(int id) throws SQLException { dishService.delete(id); }

    /**
     * reload and sets current session
     */
    public void loadLoggedInUser() {
        UserSessionSingleton.currentSession().setUser(
                getUsers().stream().filter(
                        u -> u.getId() == UserSessionSingleton.currentSession().getUser().getId()
                ).findFirst().orElseThrow());
    }
    /**
     * returns if logged in User is customer
     * @return if logged in User is customer
     */
    public boolean isUserCustomer() {
        if (UserSessionSingleton.currentSession().getUser() != null)
            return UserSessionSingleton.currentSession().getUser().getClass().equals(Customer.class);
        return false;
    }

    /**
     * returns if logged in User is admin
     * @return if logged in User is admin
     */
    public boolean isUserAdmin() {
        if (UserSessionSingleton.currentSession().getUser() != null)
            return UserSessionSingleton.currentSession().getUser().getClass().equals(Admin.class);
        return false;
    }

    // SAVE
    public void saveCategory(Category category) throws SQLException { categoryService.save(category); }
    public void saveDish(Dish dish) throws SQLException { dishService.save(dish); }
    public void saveCustomer(Customer customer) throws SQLException { userService.save(customer); }
    public void saveOrder(Order order) throws SQLException { orderService.save(order); }

    // OBSERVER SETTER
    public void setCategoryObserver(List<ParentController> observers) { observers.forEach(categoryService::addObserver); }
    public void setCouponObserver(List<ParentController> observers) {
        observers.forEach(couponService::addObserver);
    }
    public void setDishObserver(List<ParentController> observers) {
        observers.forEach(dishService::addObserver);
    }
    public void setOrderObserver(List<ParentController> observers) {
        observers.forEach(orderService::addObserver);
    }
    public void setUserObserver(List<ParentController> observers) {
        observers.forEach(userService::addObserver);
    }

    // GETTER LISTS
    public List<User> getUsers() { return userService.get(); }
    public List<Order> getOrders() { return orderService.get(); }
    public List<Dish> getDishes() { return dishService.get(); }
    public List<Category> getCategories() { return categoryService.get(); }


}
