package xchange.itesm.mx.xchange;

/**
 * Created by erikiado on 11/30/16.
 */

public class Visit {
    public String key;
    public String userKey;
    public String productKey;
    public int bid;
    public boolean like;

    public Visit(){

    }

    public String getId() {
        return key;
    }

    public void setId(String userKey) {
        this.key = userKey;
    }

    public String getUserKey() {
        return userKey;
    }

    public void setUserKey(String userKey) {
        this.userKey = userKey;
    }

    public String getProductKey() {
        return productKey;
    }

    public void setProductKey(String productKey) {
        this.productKey = productKey;
    }

    public int getBid() {
        return bid;
    }

    public void setBid(int bid) {
        this.bid = bid;
    }

    public boolean isLike() {
        return like;
    }

    public void setLike(boolean like) {
        this.like = like;
    }

    public Visit(String ukey, String pkey, int bid, boolean like) {
        this.userKey = ukey;
        this.productKey = pkey;
        this.bid = bid;
        this.like = like;
    }

}
