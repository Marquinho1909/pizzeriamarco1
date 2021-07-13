package sample.functional_logic.service;

import sample.data_logic.DAOFactory;
import sample.data_logic.dao.CategoryDAO;
import sample.data_logic.dto.Category;
import sample.data_logic.idao.iCategoryDAO;
import sample.functional_logic.ParentService;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Observer that observes ProfileEditController, when notified changes menu button name to newly edited
 */
public class CategoryService extends ParentService {

    public List<Category> categories;

    public iCategoryDAO categoryDAO;

    public CategoryService() {
        this.categoryDAO = (CategoryDAO) DAOFactory.getInstance().getDAO("Category");
        categories = new ArrayList<>();

        load();
    }

    public void load() {
        try {
            categories = categoryDAO.getAll();
        } catch(SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Category> get() {
        return categories;
    }

    public void delete(List<Category> categories) throws SQLException {
        categoryDAO.deleteAll(categories);
        load();
        notifyObservers();
    }

    public void save(Category category) throws SQLException {
        categoryDAO.save(category);
        load();
        notifyObservers();
    }
}
