package sample.dto;

public class Customer extends User {
    private Address address;

    public Customer(int id, String firstName, String lastname, Address address, char gender, String email, String password) {
        super(id, firstName, lastname, gender, email, password);
        this.address = address;
    }
    public Customer(String firstName, String lastname, Address address, char gender, String email, String password) {
        super(firstName, lastname, gender, email, password);
        this.address = address;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) { this.address = address; }

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

        public void setStreet(String street) {
            this.street = street;
        }

        public String getHouseNumber() {
            return houseNumber;
        }

        public void setHouseNumber(String houseNumber) {
            this.houseNumber = houseNumber;
        }

        public int getZipCode() {
            return zipCode;
        }

        public void setZipCode(int zipCode) {
            this.zipCode = zipCode;
        }

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
