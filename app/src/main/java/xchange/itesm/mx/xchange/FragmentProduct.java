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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        baseView = inflater.inflate(R.layout.fragment_fragment_product, container, false);
        context = this.getActivity().getBaseContext();

        mRecyclerView = (RecyclerView) baseView.findViewById(R.id.rvProducts);
        mProducts = new ArrayList<>();


        mId = Settings.Secure.getString(getActivity().getContentResolver(), Settings.Secure.ANDROID_ID);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        //mRecyclerView.setItemAnimator(new SlideInOutLeftItemAnimator(mRecyclerView));
        mAdapter = new AdapterSellerProduct(context,mProducts);
        mRecyclerView.setAdapter(mAdapter);

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

        mFirebaseDatabaseReference.child("Products").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot != null && dataSnapshot.getValue() != null) {
                    try{
                        for (DataSnapshot productSnap : dataSnapshot.getChildren()) {
                            Product model = productSnap.getValue(Product.class);
                            if(model.getSellerKey().equals(user.getUid()))
                                mProducts.add(model);
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
                Intent intent = new Intent(context.getApplicationContext(), UploadProduct.class);
                startActivity(intent);
            }
        });

        return baseView;
    }

    public void insertNewProduct(Uri file) {
        String fileName = file.getLastPathSegment();
        Toast.makeText(context, fileName, Toast.LENGTH_SHORT).show();
        //FirebaseStorage storage = FirebaseStorage.getInstance();
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
                /*String[] filePathColumn = { MediaStore.Images.Media.DATA };
                Cursor cursor = context.getContentResolver().query(selectedImage,filePathColumn, null, null, null);
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String picturePath = cursor.getString(columnIndex);
                cursor.close();
                ImageView imageView = (ImageView) baseView.findViewById(R.id.actualPicture);
                imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));*/
            } catch (Exception e) {
                Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }
}
