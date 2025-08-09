package models;

public class User {

    private String firstName;
    private String lastName;
    private String password;
    private String reEnteredPassword;
    private String email;

    public User(String firstName, String lastName, String password, String reEnteredPassword, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.reEnteredPassword = reEnteredPassword;
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPassword() {
        return password;
    }

    public String getReEnteredPassword() {
        return reEnteredPassword;
    }

    public String getEmail() {
        return email;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setReEnteredPassword(String reEnteredPassword) {
        this.reEnteredPassword = reEnteredPassword;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
