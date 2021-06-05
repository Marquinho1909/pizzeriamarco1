package sample.data_logic.dto;

import java.util.Date;
import java.util.List;
import java.util.Objects;

public class Order {
    private int id;
    private User user;
    private Date orderDate;
    private List<OrderPosition> orderpositions;
    private double discount;

    /**
     * Constructor with id for fetched object from database
     */
    public Order(int id, List<OrderPosition> orderpositions, Date orderDate, User user, double discount) {
        this.id = id;
        this.orderpositions = orderpositions;
        this.user = user;
        this.orderDate = orderDate;
        this.discount = discount;
    }

    /**
     * Constructor without id for new object
     */
    public Order(List<OrderPosition> orderpositions, Date orderDate, User user, double discount) {
        this.orderpositions = orderpositions;
        this.user = user;
        this.orderDate = orderDate;
        this.discount = discount;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public List<OrderPosition> getOrderpositions() {
        return orderpositions;
    }
    public void setDishes(List<OrderPosition> orderpositions) {
        this.orderpositions = orderpositions;
    }
    public User getUser() {
        return user;
    }
    public void setUser(User user) {
        this.user = user;
    }
    public Date getOrderDate() {
        return orderDate;
    }
    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }
    public double getDiscount() { return discount; }
    public void setDiscount(double discount) { this.discount = discount; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return orderpositions.equals(order.orderpositions) && user.equals(order.user) && orderDate.equals(order.orderDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderpositions, user, orderDate);
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", user=" + user +
                ", orderDate=" + orderDate +
                ", orderpositions=" + orderpositions +
                ", discount=" + discount +
                '}';
    }
}
