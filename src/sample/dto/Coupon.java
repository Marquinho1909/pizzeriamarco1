package sample.dto;

public class Coupon {
    private int id;
    private double value;

    public Coupon(int id, double value) {
        this.id = id;
        this.value = value;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public Coupon(double value) {
        this.value = value;
    }
}
