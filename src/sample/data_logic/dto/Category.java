package sample.data_logic.dto;

import java.util.Objects;

/**
 * DTO for Categories, dish can have non or multiple, Category can exists without dishes
 */
public class Category {
    private int id;
    private String name;

    /**
     * Constructor with id for fetched object from database
     */
    public Category(int id, String name) {
        this.id = id;
        this.name = name;
    }

    /**
     * Constructor without id for new object
     */
    public Category(String name) {
        this.name = name;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Category category = (Category) o;
        return name.equals(category.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return "Category{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
