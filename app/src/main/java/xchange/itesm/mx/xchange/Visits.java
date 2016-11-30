package xchange.itesm.mx.xchange;

/**
 * Created by Ciria on 30/11/2016.
 */

public class Visits {
    public String userKey;
    public String prodKey;
    public boolean wish;
    public long bid;
    public String status;

    public Visits(String userKey,String prodkey, boolean wish, long bid, String status){
        this.userKey=userKey;
        this.prodKey=prodkey;
        this.wish=wish;
        this.bid=bid;
        this.status=status;
    }

}
