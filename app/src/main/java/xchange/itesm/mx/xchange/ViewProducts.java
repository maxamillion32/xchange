package xchange.itesm.mx.xchange;

import android.media.Image;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.Random;


/**
 * Created by Ciria on 30/11/2016.
 */

public class ViewProducts extends AppCompatActivity {
    private Context context;
    private String[] urls=new String[100];
    private Long[] price=new Long[100];
    private String keys[]=new String[100];
    private Boolean[] inWishLst=new Boolean[100];
    private DatabaseReference mFirebaseDatabaseReference;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private DatabaseReference products;
    int a=0;
    Random r=new Random();
    long size;

    void initiate(DataSnapshot dataSnapshot){
        a=0;
        if(dataSnapshot.getValue().toString().contains("oduct")) {
            for (DataSnapshot messageSnapshot : dataSnapshot.getChildren()) {
                urls[a] = (String) messageSnapshot.child("imagePath").getValue();
                price[a] = (Long) messageSnapshot.child("price").getValue();
                keys[a]=(String)messageSnapshot.getKey();
                a++;
                Log.i("INFOOO","CRYYYYYYYYYYYYY: ");

            }
            size=dataSnapshot.getChildrenCount();
            for(int i=0;i<size;i++){
                Log.i("INFOOO","urls: "+urls[i]);
            }
            for(int i=0;i<size;i++){
                Log.i("INFOOO","price: "+price[i]);
            }
        }

        Log.i("INFOOO","Size: "+size);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_product);

        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabaseReference = FirebaseDatabase.getInstance().getReference();
        user = mAuth.getCurrentUser();

        ImageView prev = (ImageView) findViewById(R.id.prevView);
        final ImageView prod = (ImageView) findViewById(R.id.prodView);
        ImageView nex = (ImageView) findViewById(R.id.nextView);
        ImageView wish_prod=(ImageView)findViewById(R.id.wishButtonView);
        EditText offer = (EditText) findViewById(R.id.bidTxt);
        final TextView wanted = (TextView) findViewById(R.id.priceView);
        context = this.getApplicationContext();

        mFirebaseDatabaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                initiate(dataSnapshot);
                a = r.nextInt(3);
                Picasso.with(context).load(urls[a]).into(prod);
                wanted.setText("$" + price[a]);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                initiate(dataSnapshot);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                initiate(dataSnapshot);
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                initiate(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    if (a - 1 < 0) {
                        a = (int) size - 1;
                    } else {
                        a--;
                    }
                Log.i("INFOOO","AAAA: "+a);
                if(urls[a]!="") {
                    Picasso.with(context).load(urls[a]).into(prod);
                    wanted.setText("$" + price[a]);
                }
            }
        });

        nex.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    if (a+1 > size - 1) {
                        a = 0;
                    } else {
                        a++;
                    }
                Log.i("INFOOO","AAAA: "+a);
                if(urls[a]!="") {
                    Picasso.with(context).load(urls[a]).into(prod);
                    wanted.setText("$" + price[a]);
                }
            }
        });

        DatabaseReference users = mFirebaseDatabaseReference.child("Visits");
        String key = mFirebaseDatabaseReference.child("Visits").push().getKey();
        Visits visits=new Visits(user.getUid(),keys[a],false, 0,"In progress");
        try {
            mFirebaseDatabaseReference.child("Users").child(key).setValue(visits);

        } catch (Exception e) {
        }



    }
}
