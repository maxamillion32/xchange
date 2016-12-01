package xchange.itesm.mx.xchange;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
//import com.google.firebase.storage.FirebaseStorage;
//import com.google.firebase.storage.StorageReference;
//import com.google.firebase.storage.UploadTask;

public class FragmentProduct extends Fragment {
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
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        baseView = inflater.inflate(R.layout.fragment_fragment_product, container, false);
        context = this.getActivity();

        mRecyclerView = (RecyclerView) baseView.findViewById(R.id.rvProducts);
        mProducts = new ArrayList<>();


        mId = Settings.Secure.getString(getActivity().getContentResolver(), Settings.Secure.ANDROID_ID);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));

        mAdapter = new AdapterSellerProduct(context,mProducts);
        mRecyclerView.setAdapter(mAdapter);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabaseReference = FirebaseDatabase.getInstance().getReference();

        user = mAuth.getCurrentUser();

        mFirebaseDatabaseReference.child("Products").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot != null && dataSnapshot.getValue() != null) {
                    try{
                        for (DataSnapshot productSnap : dataSnapshot.getChildren()) {
                            Product model = productSnap.getValue(Product.class);
                            model.setId(productSnap.getKey());
                            if(model.getSellerKey().equals(user.getUid())){
                                if(!mProducts.contains(model))
                                    mProducts.add(model);
                            }
                        }
                        mRecyclerView.scrollToPosition(0);
                        mAdapter.notifyItemInserted(0);
                    } catch (Exception ex) {
                        Log.e("TAG1", ex.getMessage());

                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        FloatingActionButton fab = (FloatingActionButton) baseView.findViewById(R.id.buttonAddProduct);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mFirebaseDatabaseReference.child("Users").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        boolean x = false;
                        for (DataSnapshot userSnap : dataSnapshot.getChildren()) {
                            User model = userSnap.getValue(User.class);
//                            model.setUserId(userSnap.getKey());
                            if(model.getUserId().equals(user.getUid())){
                                Intent intent = new Intent(context.getApplicationContext(), UploadProduct.class);
                                startActivity(intent);
                                x = true;
                            }
                        }
                        if(!x){
                            Toast.makeText(context,"Llenar tus datos del perfil para poder subir productos",Toast.LENGTH_LONG).show();
                        }
//                        FragmentManager fragmentManager = getSupportFragmentManager();
//                        fragmentManager.beginTransaction().replace(R.id.content_fragments, fragment).commit();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }
        });

        return baseView;
    }

    public void insertNewProduct(Uri file) {
        String fileName = file.getLastPathSegment();
        Toast.makeText(context, fileName, Toast.LENGTH_SHORT).show();
    }

    public void takePhoto(View v) {
        if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)){
            // this device has a camera
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (takePictureIntent.resolveActivity(context.getPackageManager()) != null) {
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        } else {
            // no camera on this device
            Toast.makeText(context, "Cannot initiate camera", Toast.LENGTH_SHORT).show();
        }
    }

    public void getPhotoGallery(View v) {
        Intent intent = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_GALLERY_IMAGE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            Uri selectedImage = data.getData();
            insertNewProduct(selectedImage);
            /*Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");

            ImageView mImageView = (ImageView) baseView.findViewById(R.id.actualPicture);
            mImageView.setImageBitmap(imageBitmap);*/
        } else if (requestCode == REQUEST_GALLERY_IMAGE && resultCode == Activity.RESULT_OK) {
            try {
                Uri selectedImage = data.getData();
                insertNewProduct(selectedImage);

            } catch (Exception e) {
                Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }
}
