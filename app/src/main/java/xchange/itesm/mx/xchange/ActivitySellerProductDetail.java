package xchange.itesm.mx.xchange;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class ActivitySellerProductDetail extends AppCompatActivity {

    private DatabaseReference mFirebaseDatabaseReference;
    private Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_product_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        context = this;

        Intent entrada = getIntent();
        mFirebaseDatabaseReference = FirebaseDatabase.getInstance().getReference();
        mFirebaseDatabaseReference.child("Products").child(entrada.getStringExtra("productId")).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot != null && dataSnapshot.getValue() != null) {
                    try{
                        Product model = dataSnapshot.getValue(Product.class);
                        ImageView img = (ImageView)findViewById(R.id.iv_product_seller_image);
                        if(!model.getImagePath().equals(""))
                            Picasso.with(context).load(model.getImagePath()).resize(300,300).centerInside().into(img);

                        ((TextView)findViewById(R.id.product_name)).setText(model.getTitle());
                        ((TextView)findViewById(R.id.product_description)).setText(model.getDescription());
                        ((TextView)findViewById(R.id.product_price)).setText(String.valueOf(model.getPrice()));
                    } catch (Exception ex) {
                        Log.e("TAG1", ex.getMessage());

                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

}
