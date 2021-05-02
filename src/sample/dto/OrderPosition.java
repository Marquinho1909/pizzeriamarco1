package sample.dto;

public class OrderPosition {
    private int id;
    private Dish dish;
    private int amount;

    public OrderPosition(int id, Dish dish, int amount) {
        this.id = id;
        this.dish = dish;
        this.amount = amount;
    }

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

    public void setAmount(int amount) {
        this.amount = amount;
    }


}
