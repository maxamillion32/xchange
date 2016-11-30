package xchange.itesm.mx.xchange;

/**
 * Created by Miguel on 29/11/2016.
 */

public class User {
    public String userId;
    public String userName;
    public String name;
    public String phone;
    public double rating;
    public String address;
    public String description;

    public User() {

    }

    public User(String userId, String userName, String name, String phone, double rating, String address, String description) {
        this.userId = userId;
        this.userName = userName;
        this.name = name;
        this.phone = phone;
        this.rating = rating;
        this.address = address;
        this.description = description;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
