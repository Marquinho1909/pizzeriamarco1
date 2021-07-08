package sample.functional_logic.controllers;

import sample.data_logic.UserSessionSingleton;
import sample.data_logic.dao.CategoryDAO;
import sample.data_logic.dao.DishDAO;
import sample.data_logic.dto.Category;
import sample.data_logic.dto.Dish;
import sample.data_logic.dto.OrderPosition;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Observer that observes ProfileEditController, when notified changes menu button name to newly edited
 */
public class CustomerPageController {

    public List<OrderPosition> dishesInCart = new ArrayList<>();
    public List<Dish> dishes;
    public List<Category> categories;

    DishDAO dishDAO;
    CategoryDAO categoryDAO;

    public CustomerPageController() {
    }

    public CustomerPageController(DishDAO dishDAO, CategoryDAO categoryDAO) {
        this.dishDAO = dishDAO;
        this.categoryDAO = categoryDAO;
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
     * logs user out, sets UserSession to null and switches scene to Login-Scene
     */
    public void logout() {
        UserSessionSingleton.currentSession().cleanUserSession();
    }

    public List<Category> getCategories() throws SQLException {
        return categoryDAO.getAll();
    }

    public List<Dish> getDishes() throws SQLException {
        return dishDAO.getAll();
    }

    public boolean addedToExistingDish(OrderPosition o, int amount, Dish dish) {
        if (o.getDish().getId() == dish.getId()) {
            o.setAmount(Math.min(o.getAmount() + amount, 20));
            return true;
        }
        return false;
    }


    public void addOrderToCart(OrderPosition o) {
        dishesInCart.add(o);
    }

    public void removeOrder(OrderPosition o) {
        dishesInCart.remove(o);
    }
}
