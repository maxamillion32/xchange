package xchange.itesm.mx.xchange;

import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

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

public class ViewProducts extends Fragment{
    private Context context;
    private View baseView;
    private String[] urls=new String[100];
    private String[] price=new String[100];
    private DatabaseReference mFirebaseDatabaseReference;
    private DatabaseReference products;
    int a;
    Random r=new Random();
    long size;

    void initiate(DataSnapshot dataSnapshot){
        a=0;
        for (DataSnapshot messageSnapshot: dataSnapshot.getChildren()) {
            urls[a]=messageSnapshot.child("imagePath").getValue().toString();
            price[a]=messageSnapshot.child("price").getValue().toString();
            a++;
        }
        size=dataSnapshot.getChildrenCount();
        Log.i("INFOOO","Size: "+size);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        baseView = inflater.inflate(R.layout.fragment_fragment_product, container, false);
        context = this.getActivity().getBaseContext();
        ImageView prev=(ImageView)baseView.findViewById(R.id.prevView);
        final ImageView prod=(ImageView)baseView.findViewById(R.id.prodView);
        ImageView next=(ImageView)baseView.findViewById(R.id.nextView);
        EditText offer=(EditText)baseView.findViewById(R.id.bidTxt);
        final TextView wanted=(TextView)baseView.findViewById(R.id.priceView);
        mFirebaseDatabaseReference = FirebaseDatabase.getInstance().getReference();
        products = mFirebaseDatabaseReference.child("products");

        mFirebaseDatabaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                initiate(dataSnapshot);
                a=r.nextInt(3);
                Picasso.with(context).load(urls[a]).into(prod);
                wanted.setText("$"+price[a]);
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

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (a<0){
                    a=(int)size-1;
                }else {
                    a--;
                }
                Picasso.with(context).load(urls[a]).into(prod);
                wanted.setText("$"+price[a]);
            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (a>size-1){
                    a=0;
                }else {
                    a++;
                }
                Picasso.with(context).load(urls[a]).into(prod);
                wanted.setText("$"+price[a]);
            }
        });

        return baseView;
    }
}
