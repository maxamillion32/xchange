package xchange.itesm.mx.xchange;

/**
 * Created by Ciria on 30/11/2016.
 */

public class Visits {

    public String userKey;
    public String prodKey;
    public boolean list;
    public long bid;
    public String status;

    public Visits(String userKey,String prodkey, boolean like, long bid, String status){
        this.userKey=userKey;
        this.prodKey=prodkey;
        this.list=list;
        this.bid=bid;
        this.status=status;
    }


    public String getUserKey() {
        return userKey;
    }

    public void setUserKey(String userKey) {
        this.userKey = userKey;
    }

    public String getProdKey() {
        return prodKey;
    }

    public void setProdKey(String prodKey) {
        this.prodKey = prodKey;
    }

    public long getBid() {
        return bid;
    }

    public void setBid(long bid) {
        this.bid = bid;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
