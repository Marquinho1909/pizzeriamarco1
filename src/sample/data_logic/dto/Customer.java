package sample.data_logic.dto;

public class Customer extends User {
    private Address address;
    private Coupon coupon;

    public Customer(String firstName, String lastname, Address address, char gender, String email, String password) {
        super(firstName, lastname, gender, email, password);
        this.address = address;
    }
    public Customer(int id, String firstName, String lastname, Address address, char gender, String email, String password) {
        super(id, firstName, lastname, gender, email, password);
        this.address = address;
    }
    public Customer(Coupon coupon, String firstName, String lastname, Address address, char gender, String email, String password) {
        super(firstName, lastname, gender, email, password);
        this.address = address;
        this.coupon = coupon;
    }

    public Coupon getCoupon() { return coupon; }
    public Address getAddress() {
        return address;
    }
    public void setAddress(Address address) { this.address = address; }
    public void setCoupon(Coupon coupon) { this.coupon = coupon; }

    @Override
    public String toString() {
        return "Costumer{" +
                "id=" + getId() +
                ", firstName='" + getFirstName() + '\'' +
                ", lastname='" + getLastname() + '\'' +
                ", gender=" + getGender() +
                ", email='" + getEmail() + '\'' +
                ", password='" + getPassword() + '\'' +
                ", address=" + address +
                '}';
    }

    public static class Address {
        private String street;
        private String houseNumber;
        private int zipCode;

        public Address(String street, String houseNumber, int zipCode) {
            this.street = street;
            this.houseNumber = houseNumber;
            this.zipCode = zipCode;
        }

        public String getStreet() {
            return street;
        }
        public String getHouseNumber() {
            return houseNumber;
        }
        public int getZipCode() {
            return zipCode;
        }
        public void setStreet(String street) { this.street = street; }
        public void setHouseNumber(String houseNumber) { this.houseNumber = houseNumber; }
        public void setZipCode(int zipCode) { this.zipCode = zipCode; }

        @Override
        public String toString() {
            return "Address{" +
                    "street='" + street + '\'' +
                    ", houseNumber='" + houseNumber + '\'' +
                    ", zipCode=" + zipCode +
                    '}';
        }
    }
}
