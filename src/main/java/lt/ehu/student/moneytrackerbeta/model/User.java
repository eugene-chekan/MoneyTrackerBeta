package lt.ehu.student.moneytrackerbeta.model;

import java.sql.Timestamp;
import java.util.Date;

public class User extends AbstractModel {
    private int id;
    private String login;
    private String passwordHash;
    private String firstName;
    private String lastName;
    private int defaultCurrency;
    private String email;
    private Timestamp registrationDate;

    public User(int id, String login, String passwordHash, String firstName, String lastName, int defaultCurrency, String email, Timestamp registrationDate) {
        this.id = id;
        this.login = login;
        this.passwordHash = passwordHash;
        this.firstName = firstName;
        this.lastName = lastName;
        this.defaultCurrency = defaultCurrency;
        this.email = email;
        this.registrationDate = registrationDate;
    }

    public User() {}

    public int getId() {
        return id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getDefaultCurrency() {
        return defaultCurrency;
    }

    public void setDefaultCurrency(int defaultCurrency) {
        this.defaultCurrency = defaultCurrency;
    }

    public Date getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(Timestamp registrationDate) {
        this.registrationDate = registrationDate;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", login='" + login + '\'' +
                ", password='" + passwordHash + '\'' +
                ", email='" + email + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                '}';
    }
}
