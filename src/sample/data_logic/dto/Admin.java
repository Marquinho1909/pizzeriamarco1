package sample.data_logic.dto;

/**
 * DTO for Admins, extends Superclass User
 */
public class Admin extends User {

    /**
     * Constructor with id for fetched object from database
     */
    public Admin(int id, String firstName, String lastname, char gender, String email, String password) {
        super(id, firstName, lastname, gender, email, password);
    }

    /**
     * Constructor without id for new object
     */
    public Admin(String firstName, String lastname, char gender, String email, String password) {
        super(firstName, lastname, gender, email, password);
    }
}
