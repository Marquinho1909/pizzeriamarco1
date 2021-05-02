package sample.dto;

public class Admin extends User {
    public Admin(int id, String firstName, String lastname, char gender, String email, String password) {
        super(id, firstName, lastname, gender, email, password);
    }
    public Admin(String firstName, String lastname, char gender, String email, String password) {
        super(firstName, lastname, gender, email, password);
    }
}
