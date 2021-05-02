package sample.dto;

import java.util.Date;
import java.util.List;
import java.util.Objects;

public class Order {
    private int id;
    private User user;
    private Date orderDate;
    private List<OrderPosition> orderpositions;

    public Order(int id, List<OrderPosition> orderpositions, User user) {
        this.id = id;
        this.orderpositions = orderpositions;
        this.user = user;
        this.orderDate = new Date();
    }

    public Order(List<OrderPosition> orderpositions, User user) {
        this.orderpositions = orderpositions;
        this.user = user;
        this.orderDate = new Date();
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
}
