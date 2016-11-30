package xchange.itesm.mx.xchange;


import java.util.Enumeration;
import java.util.Hashtable;

/**
 * Created by Ciria on 27/11/2016.
 */
public class AllData {
    //All data is about articles in the wishList
    private int cost[];                 //cost proposed by seller
    private String[] article;           //article url for images
    private String[] description;       //description of articles
    private String[] users;             //url of user image
    private int[] stars;                 //star of users
    private String msj;                 //Message sent from user to seller
    private Hashtable <Integer,Integer>art_seller=new Hashtable<Integer,Integer>();     //relate article with seller
    private int offer=0;                //amount ($) the user offers for the product
    private int[] offerWishList;        //amount ($) the user have offer for all the products in wish list
    private boolean[] sellerAccepted;
    private String[] confirmDone;


    public void refreshAllData(){
        if ()
    }

    public AllData(){
        refreshAllData();
        cost=new int[]{7500,13200,750,590};
        article= new String[]{
                "https://images-na.ssl-images-amazon.com/images/I/51OwBkohn2L._SX425_.jpg",     //bicycle
                "https://katespade.insnw.net/KateSpade/143403NB_667?$large$",                   //sofa
                "https://katespade.insnw.net/KateSpade/S743007MU_786?$large$",                  //shoes
                "http://s7d4.scene7.com/is/image/KateSpade/WBRUD474_974?$large$"                //bracelet
        };
        description=new String[]{
                "NICE BICYCLE",
                "PINK SOFA",
                "AMAZING SHOES",
                "STUNNING BRACELET"
        };

        users=new String[]{
                "http://www.uni-regensburg.de/Fakultaeten/phil_Fak_II/Psychologie/Psy_II/beautycheck/english/durchschnittsgesichter/m(01-32)_gr.jpg", //Man
                "http://www.tattooset.com/images/tattoo/2013/01/25/15120-pinterest-tattoos_large.jpg",                                                //young woman
                "https://s-media-cache-ak0.pinimg.com/236x/f9/c4/0e/f9c40e3544fe9d1a8ee8db6525c2895a.jpg",                                            //woman
                "http://muyuela.com/wp-content/uploads/2014/07/Hairstyles-For-Young-Men-3.jpg"                                                        //young man
        };

        stars=new int[]{3, 1, 5, 4};
        art_seller.put(0,3);
        art_seller.put(1,2);
        art_seller.put(2,1);
        art_seller.put(3,0);

        offerWishList=new int[]{0,50,100,900};
        sellerAccepted=new boolean[] {false,false,true,true};
        confirmDone=new String[]{
                "Seller has accepted your offer. Confirm now!",
                "Seller has accepted your offer. Confirm now!",
                "Seller has accepted your offer. Confirm now!",
                "DONE"
        };
    }

    //this method receives the index of the article selected and seeks for the seller in the hastable
    public int whosSeller(int position){
        Enumeration art= art_seller.keys();
        Integer key;
        while(art.hasMoreElements()) {
            key=(Integer) art.nextElement();
            if (key==position){
                return art_seller.get(key);
            }
        }
        return -1;
    }

    public void updateOfferWishList(int position, int offer){
        offerWishList[position]=offer;
        setOffer(offer);
    }
    public void updateConfirmDone(int position, String status){
        confirmDone[position]=status;
    }
    public int[] getCost() {
        return cost;
    }
    public void setCost(int[] cost) {
        this.cost = cost;
    }

    public String[] getArticle() {
        return article;
    }
    public void setArticle(String[] article) {
        this.article = article;
    }

    public String[] getDescription() {
        return description;
    }
    public void setDescription(String[] description) {
        this.description = description;
    }

    public int[] getStars() {
        return stars;
    }
    public void setStars(int[] stars) {
        this.stars = stars;
    }

    public String[] getUsers() {
        return users;
    }
    public void setUsers(String[] users) {
        this.users = users;
    }

    public String getMsj() {
        return msj;
    }
    public void setMsj(String msj) {
        this.msj = msj;
    }

    public void setOffer(int offer) {
        this.offer=offer;
    }
    public int getOffer() {
        return offer;
    }

    public int[] getOfferWishList() {
        return offerWishList;
    }
    public void setOfferWishList(int[] offerWishList) {
        this.offerWishList = offerWishList;
    }

    public boolean[] getSellerAccepted() {
        return sellerAccepted;
    }
    public void setSellerAccepted(boolean[] sellerAccepted) {
        this.sellerAccepted = sellerAccepted;
    }

    public String[] getConfirmDone() {
        return confirmDone;
    }
    public void setConfirmDone(String[] confirmDone) {
        this.confirmDone = confirmDone;
    }
}
