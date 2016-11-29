package xchange.itesm.mx.xchange;

/**
 * Created by Miguel on 29/11/2016.
 */

public class Product {
    public String name;
    public int price;
    public String seller;

    public Product() {

    }

    public Product(String name, int price, String seller) {
        this.name = name;
        this.price = price;
        this.seller = seller;
    }
}
