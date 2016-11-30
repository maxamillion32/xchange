package xchange.itesm.mx.xchange;

/**
 * Created by Miguel on 29/11/2016.
 */

public class Product {
    public String key;
    public String title;
    public int price;
    public String sellerKey;
    public String description;
    public String imagePath;
    public String status;

    public Product(){

    }

    public Product(String title, int price, String sellerKey, String description, String imagePath, String status) {
        this.title = title;
        this.price = price;
        this.sellerKey = sellerKey;
        this.description = description;
        this.imagePath = imagePath;
        this.status = status;
    }

    public String getId() {
        return key;
    }

    public void setId(String k) {
        key= k;
    }

    public String getSellerKey() {
        return sellerKey;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String message) {
        this.title = message;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String message) {
        this.imagePath = message;
    }

    public void setPrice(int message) {
        this.price = message;
    }

    public int getPrice() {
        return (price);
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String message) {
        this.description = message;
    }


}
