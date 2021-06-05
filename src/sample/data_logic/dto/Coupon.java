package sample.data_logic.dto;

import java.util.Objects;

/**
 * DTO for coupons, one coupon can be retrieved when registering as new customer,
 * coupon is bound to address, only one coupon per address, is deleted after usage
 */
public class Coupon {
    private int id;
    private double value;

    /**
     * Constructor with id for fetched object from database
     */
    public Coupon(int id, double value) {
        this.id = id;
        this.value = value;
    }

    /**
     * Constructor without id for new object
     */
    public Coupon(double value) {
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
    public void setValue(double value) { this.value = value; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Coupon coupon = (Coupon) o;
        return Double.compare(coupon.value, value) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return "Coupon{" +
                "id=" + id +
                ", value=" + value +
                '}';
    }
}
