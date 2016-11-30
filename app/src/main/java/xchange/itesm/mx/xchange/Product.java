package xchange.itesm.mx.xchange;

/**
 * Created by Miguel on 29/11/2016.
 */

public class Product {
    public String title;
    public int price;
    public String sellerKey;
    public String description;
    public String imagePath;
    public String status;

    public Product(String title, int price, String sellerKey, String description, String imagePath, String status) {
        this.title = title;
        this.price = price;
        this.sellerKey = sellerKey;
        this.description = description;
        this.imagePath = imagePath;
        this.status = status;
    }
}
