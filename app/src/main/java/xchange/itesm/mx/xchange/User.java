package xchange.itesm.mx.xchange;

/**
 * Created by Miguel on 29/11/2016.
 */

public class User {
    public String name;
    public String phone;
    public double rating;
    public String address;
    public String description;

    public User(String name, String phone, double rating, String address, String description) {
        this.name = name;
        this.phone = phone;
        this.rating = rating;
        this.address = address;
        this.description = description;
    }
}
