package sample.functional_logic.service;

import sample.data_logic.DAOFactory;
import sample.data_logic.dao.DishDAO;
import sample.data_logic.dto.Dish;
import sample.data_logic.idao.iDishDAO;
import sample.functional_logic.ParentService;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DishService extends ParentService {

    List<Dish> dishes;
    public iDishDAO dishDAO;

    public DishService() {
        this.dishDAO = (DishDAO) DAOFactory.getInstance().getDAO("Dish");
        dishes = new ArrayList<>();

        load();
    }
    
    public List<Dish> get() {
        return dishes;
    }

    public void save(Dish dish) throws SQLException {
        dishDAO.save(dish);
        load();
        notifyObservers();
    }

    public void delete(int id) throws SQLException {
        dishDAO.delete(id);
        load();
        notifyObservers();
    }

    public void update(int id, Dish dish) throws SQLException {
        dishDAO.update(id, dish);
        load();
        notifyObservers();
    }

    public void load() {
        try {
            dishes = dishDAO.getAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
