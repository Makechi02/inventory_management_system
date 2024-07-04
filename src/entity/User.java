package entity;

import java.io.Serializable;

public class User implements Serializable {
    private String name;
    private String password;
    private UserType userType;

    public User(String name, String password, UserType userType) {
        this.name = name;
        this.password = password;
        this.userType = userType;
    }

    public User() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UserType getUserType() {
        return userType;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }

    public void display() {
        System.out.println("\nUSER DETAILS");
        System.out.println("Name: " + name);
        System.out.println("User type: " + userType);
    }

    @Override
    public String toString() {
        return String.format("%s - %s", name, userType);
    }
}
