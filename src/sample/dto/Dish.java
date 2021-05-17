package sample.dto;

import java.util.List;
import java.util.Objects;

public class Dish {
    private int id;
    private String name;
    private List<Category> categories;
    private double price;

    public Dish(int id, String name, List<Category> categories, double price) {
        this.id = id;
        this.name = name;
        this.categories = categories;
        this.price = price;
    }

    public Dish(String name, List<Category> categories, double price) {
        this.name = name;
        this.categories = categories;
        this.price = price;
    }

    public Dish(String name, double price) {
        this.name = name;
        this.price = price;
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
