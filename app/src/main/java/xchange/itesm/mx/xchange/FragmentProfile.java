package xchange.itesm.mx.xchange;

import android.content.Context;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by erikiado on 11/30/16.
 */

public class FragmentProfile extends Fragment {
    private Context context;
    private View baseView;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseUser user;
    private DatabaseReference mFirebaseDatabaseReference;

    private List<Product> mProducts;
    private RecyclerView mRecyclerView;
    private AdapterSellerProduct mAdapter;
    private String mId;



    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_GALLERY_IMAGE = 2;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        baseView = inflater.inflate(R.layout.activity_register_user, container, false);
        context = this.getActivity().getBaseContext();

        mRecyclerView = (RecyclerView) baseView.findViewById(R.id.rvProducts);
        mProducts = new ArrayList<>();


//        mId = Settings.Secure.getString(getActivity().getContentResolver(), Settings.Secure.ANDROID_ID);
//        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
//        //mRecyclerView.setItemAnimator(new SlideInOutLeftItemAnimator(mRecyclerView));
//        mAdapter = new AdapterSellerProduct(context,mProducts);
//        mRecyclerView.setAdapter(mAdapter);
//
//        // Initialize Firebase Auth
//        mAuth = FirebaseAuth.getInstance();
//        mFirebaseDatabaseReference = FirebaseDatabase.getInstance().getReference();
//
////        mAuthListener = new FirebaseAuth.AuthStateListener() {
////            @Override
////            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
////                user = firebaseAuth.getCurrentUser();
////            }
////        };
//        user = mAuth.getCurrentUser();
//
//        mFirebaseDatabaseReference.child("Products").addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                if (dataSnapshot != null && dataSnapshot.getValue() != null) {
//                    try{
//                        for (DataSnapshot productSnap : dataSnapshot.getChildren()) {
//                            Product model = productSnap.getValue(Product.class);
//                            model.setId(productSnap.getKey());
//                            if(!model.getSellerKey().equals(user.getUid()))
//                                mProducts.add(model);
//                        }
//
//                        mRecyclerView.scrollToPosition(0);
//                        mAdapter.notifyItemInserted(0);
//                    } catch (Exception ex) {
//                        Log.e("TAG1", ex.getMessage());
//
//                    }
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });


//        FloatingActionButton fab = (FloatingActionButton) baseView.findViewById(R.id.buttonAddProduct);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(context.getApplicationContext(), UploadProduct.class);
//                startActivity(intent);
//            }
//        });

        return baseView;
    }

}
