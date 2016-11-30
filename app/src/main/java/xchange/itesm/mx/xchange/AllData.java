package xchange.itesm.mx.xchange;


import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by Ciria on 27/11/2016.
 */
public class AllData {
    //All data is about articles in the wishList
    private Hashtable <String,String>wishProds=new Hashtable<String,String>();
    private long cost[]=new long[100];                 //cost proposed by seller
    private String[] article=new String[100];           //article url for images
    private String[] description=new String[100] ;       //description of articles
    private String[] users=new String[100];             //url of user image
    private double[] stars=new double[100];                 //star of users
    private Hashtable <Integer,Integer>art_seller=new Hashtable<Integer,Integer>();     //relate article with seller
    private int offer=0;                //amount ($) the user offers for the product
    private int[] offerWishList;        //amount ($) the user have offer for all the products in wish list
    private String[] status;
    int size=0;

    private DatabaseReference mFirebaseDatabaseReference;
    FirebaseUser userb;


    public void updateDataProds(final Hashtable wishProds){
        final Iterator <Map.Entry<String,String>> it= wishProds.entrySet().iterator();
        while (it.hasNext()){
            mFirebaseDatabaseReference.child("Products").addValueEventListener(new ValueEventListener() {
            int a=0;
                String usrKey;
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for(DataSnapshot prodObj: dataSnapshot.getChildren()){
                        Product prod= prodObj.getValue(Product.class);
                        Map.Entry<String,String> entry=it.next();
                        if (prod.getId()==entry.getValue()){
                            usrKey=prod.getSellerKey();
                            updateDataUsers(usrKey,a);
                            users[a]="https://support.plymouth.edu/kb_images/Yammer/default.jpeg";
                            article[a]=prod.getImagePath();
                            cost[a]=prod.getPrice();
                            art_seller.put(a,a);
                            description[a]=prod.getDescription();
                            a++;
                        }
                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }
    public void updateDataUsers(final String usrKey, final int a){
            mFirebaseDatabaseReference.child("Users").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for(DataSnapshot userObj: dataSnapshot.getChildren()){
                        User user= userObj.getValue(User.class);
                        if (user.getUserId()==usrKey){
                            stars[a]=user.rating;
                        }
                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
    }

    /*public void updateOfferDB(int offer) {
        //Initialize authentication

        FirebaseAuth mAuth= FirebaseAuth.getInstance();
        FirebaseAuth.AuthStateListener mAuthListener;
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                userb = firebaseAuth.getCurrentUser();
            }
        };


        mFirebaseDatabaseReference.child("Users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot userObj: dataSnapshot.getChildren()){
                    User useraaa= userObj.getValue(User.class);
                    if (useraaa.getUserId()==userb.getUid()){
                        mFirebaseDatabaseReference.child("Users").child()
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }*/


    public AllData(){
        mFirebaseDatabaseReference = FirebaseDatabase.getInstance().getReference();
        mFirebaseDatabaseReference.child("Visit").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    size=0;
                    for (DataSnapshot visitObj : dataSnapshot.getChildren()) {
                        Visit visitas = visitObj.getValue(Visit.class);
                        if (visitas.getLike()) {
                            wishProds.put(visitas.getId(), visitas.getProductKey());
                            offerWishList[size]=visitas.getBid();
                            status[size]=visitas.getStatus();
                            size++;
                        }
                    }

                    updateDataProds(wishProds);

                } catch (Exception e) {
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

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

    public long[] getCost() {
        return cost;
    }
    public void setCost(long[] cost) {
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

    public double[] getStars() {
        return stars;
    }
    public void setStars(double[] stars) {
        this.stars = stars;
    }

    public String[] getUsers() {
        return users;
    }
    public void setUsers(String[] users) {
        this.users = users;
    }

    public void setOffer(int offer) {
        //updateOfferDB(offer);
        this.offer=offer;
        Log.i("INFOOOOO", "MISSING TO UPDATE VALUE IN DB");
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

    public String[] getStatus() {
        return status;
    }

    public void setStatus(String[] status) {
        this.status = status;
    }
}
