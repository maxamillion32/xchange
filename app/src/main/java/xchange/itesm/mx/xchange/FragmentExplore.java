package xchange.itesm.mx.xchange;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by erikiado on 11/30/16.
 */

public class FragmentExplore extends Fragment {
    private Context context;
    private View baseView;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseUser user;
    private DatabaseReference mFirebaseDatabaseReference;

    private ArrayList<Product> mProducts;
    private ArrayList<Visit> mVisits;
    private RecyclerView mRecyclerView;
    private AdapterCustomerProducts mAdapter;
    private String mId;
    private int currentIndex;
    private String currentKey;
    private ImageView currentImage;
    private EditText editBid;
    private TextView productName, productDescription, noProducts;
    private ScrollView scrollView;

    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_GALLERY_IMAGE = 2;

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        baseView = inflater.inflate(R.layout.fragment_explore, container, false);
        context = this.getActivity().getBaseContext();

        currentImage = (ImageView)baseView.findViewById(R.id.current_image);
        productDescription = (TextView) baseView.findViewById(R.id.product_description);
        productName = (TextView) baseView.findViewById(R.id.product_name);
        editBid = (EditText) baseView.findViewById(R.id.edit_bid);

        scrollView = (ScrollView)baseView.findViewById(R.id.scroll);
        noProducts = (TextView)baseView.findViewById(R.id.no_products);
//        mRecyclerView = (RecyclerView) baseView.findViewById(R.id.rvProducts);
        mProducts = new ArrayList<>();
        mVisits = new ArrayList<>();
        currentIndex = 0;

        mId = Settings.Secure.getString(getActivity().getContentResolver(), Settings.Secure.ANDROID_ID);
//        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        //mRecyclerView.setItemAnimator(new SlideInOutLeftItemAnimator(mRecyclerView));
//        mAdapter = new AdapterCustomerProducts(context,mProducts);
//        mRecyclerView.setAdapter(mAdapter);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabaseReference = FirebaseDatabase.getInstance().getReference();

//        mAuthListener = new FirebaseAuth.AuthStateListener() {
//            @Override
//            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
//                user = firebaseAuth.getCurrentUser();
//            }
//        };
        user = mAuth.getCurrentUser();
        mFirebaseDatabaseReference.child("Visits").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot vistSnap : dataSnapshot.getChildren()) {
                    Visit model = vistSnap.getValue(Visit.class);
                    model.setId(vistSnap.getKey());
                    if(model.getUserKey().equals(user.getUid())){
                        mVisits.add(model);
                    }
                }
                getProducts(baseView);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        setListeners(baseView);


        return baseView;
    }

    public void setListeners(final View baseView){
        baseView.findViewById(R.id.button_accept).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int cbid = Integer.valueOf(editBid.getText().toString());
                if(cbid > 0){
                    registerVisit(mProducts.get(currentIndex),user,cbid,true);
                    Toast.makeText(context,"Apuesta aceptada",Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(context,"La apuesta debe ser mayor a 0",Toast.LENGTH_LONG).show();
                }
            }
        });

        baseView.findViewById(R.id.button_like).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerVisit(mProducts.get(currentIndex),user,0,true);
                Toast.makeText(context,"Te ha gustado el producto",Toast.LENGTH_LONG).show();

            }
        });

        baseView.findViewById(R.id.button_reject).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerVisit(mProducts.get(currentIndex),user,0,false);
                Toast.makeText(context,"Producto Rechazado",Toast.LENGTH_LONG).show();

            }
        });
    }

    public void getProducts(final View base){
        mFirebaseDatabaseReference.child("Products").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot != null && dataSnapshot.getValue() != null) {
                    try{
                        for (DataSnapshot productSnap : dataSnapshot.getChildren()) {
                            Product model = productSnap.getValue(Product.class);
                            model.setId(productSnap.getKey());
                            if(!model.getSellerKey().equals(user.getUid())){
                                mProducts.add(model);
                            }
                        }
                        ArrayList remove = new ArrayList();
                        for (Visit visit: mVisits) {
                            for(int i = 0; i < mProducts.size();i++){
                                if(visit.getProductKey().equals(mProducts.get(i).getId())){
                                    if(!remove.contains(i))
                                        remove.add(i);
                                }
                            }
                        }
                        Collections.sort(remove);
                        Collections.reverse(remove);
                        for(int i = 0; i < remove.size(); i++){
                            Product p = mProducts.get((Integer) remove.get(i));
                            mProducts.remove(p);
                        }
//                        mRecyclerView.scrollToPosition(0);
//                        mAdapter.notifyItemInserted(0);
                    } catch (Exception ex) {
                        Log.e("TAG1", ex.getMessage());

                    }

                    if(currentIndex == 0){
                        if(mProducts.size() > 0){
                            updateUI(mProducts.get(0));
                        }else{
                            scrollView.setVisibility(View.GONE);
                            noProducts.setVisibility(View.VISIBLE);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void registerVisit(Product model, FirebaseUser user,int bid,boolean like) {
        DatabaseReference visits = mFirebaseDatabaseReference.child("Visits");
        currentKey = visits.push().getKey();
        String ukey = user.getUid();
        String pkey = model.getId();

        Visit visit = new Visit(ukey, pkey, bid, like);

        try {
            mFirebaseDatabaseReference.child("Visits").child(currentKey).setValue(visit);

        } catch (Exception e) {
            Toast.makeText(context, "Upload error", Toast.LENGTH_SHORT).show();

        }

        currentIndex++;
        if(currentIndex >= mProducts.size()){
            scrollView.setVisibility(View.GONE);
            noProducts.setVisibility(View.VISIBLE);
        }else{
            updateUI(mProducts.get(currentIndex));
        }

    }

    public void updateUI(Product model) {
        scrollView.setVisibility(View.VISIBLE);
        noProducts.setVisibility(View.GONE);
        editBid.setHint(String.valueOf(model.getPrice()));
//        ((EditText)getActivity().findViewById(R.id.edit_bid)).setHint(model.getPrice());
        productName.setText(model.getTitle());
        productDescription.setText(model.getDescription());
        Picasso.with(context).load(model.getImagePath()).into(currentImage);
        editBid.setText("");
    }
}
