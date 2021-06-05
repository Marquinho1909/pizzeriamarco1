package sample.data_logic.dto;

import java.util.List;
import java.util.Objects;

/**
 * DTO for dishes
 */
public class Dish {
    private int id;
    private String name;
    private List<Category> categories;
    private double price;
    private boolean active;

    /**
     * Constructor with id for fetched object from database
     */
    public Dish(int id, String name, List<Category> categories, double price, boolean active) {
        this.id = id;
        this.name = name;
        this.categories = categories;
        this.price = price;
        this.active = active;
    }

    /**
     * Constructor without id for new object
     */
    public Dish(String name, List<Category> categories, double price, boolean active) {
        this.name = name;
        this.categories = categories;
        this.price = price;
        this.active = active;
    }


    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public List<Category> getCategories() { return categories; }
    public void setCategories(List<Category> categories) { this.categories = categories; }
    public double getPrice() {
        return price;
    }
    public void setPrice(double price) {
        this.price = price;
    }
    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Dish dish = (Dish) o;
        return Double.compare(dish.price, price) == 0 && name.equals(dish.name);
    }

    @Override
    public String toString() {
        return "DishDTO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                '}';
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, price);
    }
}
