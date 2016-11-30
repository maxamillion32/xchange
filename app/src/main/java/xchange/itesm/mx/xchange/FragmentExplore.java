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
    private ImageView currentImage;


    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_GALLERY_IMAGE = 2;

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        baseView = inflater.inflate(R.layout.fragment_explore, container, false);
        context = this.getActivity().getBaseContext();


        currentImage = (ImageView)baseView.findViewById(R.id.current_image);
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
                getProducts();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        return baseView;
    }

    public void getProducts(){
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
                        if(currentIndex == 0){
                            if(mProducts.size() > 0){
                                Product model = mProducts.get(currentIndex);
                                Picasso.with(context).load(model.getImagePath()).into(currentImage);
                                DatabaseReference visits = mFirebaseDatabaseReference.child("Visits");
                                String key = visits.push().getKey();
                                registerVisit(key, model, user);
                                currentIndex ++;
                            }
                        }

//                        mRecyclerView.scrollToPosition(0);
//                        mAdapter.notifyItemInserted(0);
                    } catch (Exception ex) {
                        Log.e("TAG1", ex.getMessage());

                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void registerVisit(String key, Product model, FirebaseUser user) {

        String ukey = user.getUid();
        String pkey = model.getId();

        Visit visit = new Visit(ukey,pkey,0,false);

        try {
            mFirebaseDatabaseReference.child("Visits").child(key).setValue(visit);

        } catch(Exception e) {
            Toast.makeText(context, "Upload error", Toast.LENGTH_SHORT).show();
        }
    }
}
