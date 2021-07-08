package sample.functional_logic.controllers;

import sample.data_logic.DAOFactory;
import sample.data_logic.dao.CategoryDAO;
import sample.data_logic.dao.DishDAO;
import sample.data_logic.dto.Category;
import sample.data_logic.dto.Dish;

import java.sql.SQLException;
import java.util.List;

public class DishCreationModalController extends ModalController {
    CategoryDAO categoryDAO;
    DishDAO dishDAO;

    public DishCreationModalController() {
        categoryDAO = (CategoryDAO) DAOFactory.getInstance().getDAO("Category");
        dishDAO = (DishDAO) DAOFactory.getInstance().getDAO("Dish");

    }

    public void deleteCategories(List<Category> categories) throws SQLException {
        categoryDAO.deleteAll(categories);
    }

    public void saveDish(Dish dish) throws SQLException {
        dishDAO.save(dish);
    }

    public List<Category> getCategories() throws SQLException {
        return categoryDAO.getAll();
    }

    public boolean doesCategoryExist(String name) throws SQLException {
        return categoryDAO.getAll().stream().anyMatch(c -> c.getName().equals(name));
    }

    public void saveCategory(Category category) throws SQLException {
        categoryDAO.save(category);
    }

    public boolean canAllCategoriesBeDeleted(List<Category> categories) throws SQLException {
        List<Dish> dishes = dishDAO.getAll();

        // check if dishes have any selected category as category, if so deletion is cancelled
        for (Category category : categories)
            if (dishes.stream().anyMatch(d -> d.getCategories().contains(category))) {
                return false;
            }
        return true;
    }
}
