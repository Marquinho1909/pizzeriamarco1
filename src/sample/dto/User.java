package sample.dto;

import java.util.Objects;

public abstract class User {
    private int id;
    private String firstName;
    private String lastname;
    private char gender;
    private String email;
    private String password;

    public User(int id, String firstName, String lastname, char gender, String email, String password) {
        this.id = id;
        this.firstName = firstName;
        this.lastname = lastname;
        setGender(gender);
        setEmail(email);
        this.password = password;
    }

    public User(String firstName, String lastname, char gender, String email, String password) {
        this.firstName = firstName;
        this.lastname = lastname;
        setGender(gender);
        setEmail(email);
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public char getGender() {
        return gender;
    }

    public void setGender(char gender) {
        if (gender != 'm' && gender != 'f' && gender != 'd')
            return;
        this.gender = gender;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        if (email.contains("@"))
            this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return gender == user.gender && firstName.equals(user.firstName) && lastname.equals(user.lastname) && email.equals(user.email) && password.equals(user.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstName, lastname, gender, email, password);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastname='" + lastname + '\'' +
                ", gender=" + gender +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}

