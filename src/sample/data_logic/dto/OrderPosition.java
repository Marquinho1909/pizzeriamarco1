package sample.data_logic.dto;

import java.util.Objects;

/**
 * DTO for OrderPositions for one dish, Order contains multiple OrderPositions
 */
public class OrderPosition {
    private int id;
    private Dish dish;
    private int amount;

    /**
     * Constructor with id for fetched object from database
     */
    public OrderPosition(int id, Dish dish, int amount) {
        this.id = id;
        this.dish = dish;
        this.amount = amount;
    }

    /**
     * Constructor without id for new object
     */
    public OrderPosition(Dish dish, int amount) {
        this.dish = dish;
        this.amount = amount;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public Dish getDish() {
        return dish;
    }
    public void setDish(Dish dish) {
        this.dish = dish;
    }
    public int getAmount() {
        return amount;
    }
    public void setAmount(int amount) { this.amount = amount; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderPosition that = (OrderPosition) o;
        return amount == that.amount && dish.equals(that.dish);
    }

    @Override
    public int hashCode() {
        return Objects.hash(dish, amount);
    }

    @Override
    public String toString() {
        return "OrderPosition{" +
                "id=" + id +
                ", dish=" + dish +
                ", amount=" + amount +
                '}';
    }
}
