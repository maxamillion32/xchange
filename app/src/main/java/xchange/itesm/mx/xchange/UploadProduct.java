package xchange.itesm.mx.xchange;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Handler;
import android.provider.MediaStore;
import android.provider.SyncStateContract;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import static android.R.attr.bitmap;

public class UploadProduct extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseUser user;
    private DatabaseReference mFirebaseDatabaseReference;
    private StorageReference mStorageRef;
    private FirebaseStorage storage;
    public Uri downloadUrl;
    private Bitmap photo;

    private ByteArrayOutputStream bytes;
    Uri uriImage;

    private static final int CAMERA_REQUEST = 1001;
    private static final int GALLERY_REQUEST = 1002;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_product);

        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabaseReference = FirebaseDatabase.getInstance().getReference();
        storage = FirebaseStorage.getInstance();
        mStorageRef = FirebaseStorage.getInstance().getReference();
        user = mAuth.getCurrentUser();
    }

    public void cameraImage(View v) {
        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, CAMERA_REQUEST);
    }

    public void galleryImage(View v) {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");

        startActivityForResult(intent, GALLERY_REQUEST);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        ImageView imageView = (ImageView)findViewById(R.id.imageUpload);
        bytes = new ByteArrayOutputStream();

        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            photo = (Bitmap) data.getExtras().get("data");

            imageView.setImageBitmap(photo);
        } else if (requestCode == GALLERY_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            try {
                Uri selectedImage = data.getData();
                InputStream imageStream = getContentResolver().openInputStream(selectedImage);
                photo = BitmapFactory.decodeStream(imageStream);

                imageView.setImageBitmap(photo);
            } catch(Exception e) {
                Toast.makeText(this, "File not found", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Please select an image", Toast.LENGTH_SHORT).show();
        }
    }

    public void encodeBitmapAndSaveToFirebase(Bitmap bitmap, final String key) {
        StorageReference storageRef = storage.getReferenceFromUrl("gs://movilesfinal-b448c.appspot.com");
        StorageReference mountainsRef = storageRef.child("images/" + key + ".jpg");

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = mountainsRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Toast.makeText(UploadProduct.this, exception.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                //Toast.makeText(UploadProduct.this, "Success on upload image", Toast.LENGTH_SHORT).show();
                downloadUrl = taskSnapshot.getDownloadUrl();
                registerProduct(downloadUrl.toString(), key);
            }
        });
    }

    public void uploadProduct(View v) {
            DatabaseReference products = mFirebaseDatabaseReference.child("Products");
            String key = mFirebaseDatabaseReference.child("Products").push().getKey();

        try {
            encodeBitmapAndSaveToFirebase(photo, key);
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void registerProduct(String path, String key) {
        EditText title = (EditText) findViewById(R.id.Title);
        EditText price = (EditText) findViewById(R.id.Price);
        EditText description = (EditText) findViewById(R.id.Description);

        String titleVal = title.getText().toString();
        String priceVal = price.getText().toString();
        String descriptionVal = description.getText().toString();
        String sellerKey = user.getUid();
        String imagePath = "";
        try {
            imagePath = path;
        } catch (Exception e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        String status = "Available";

        Product product = new Product(titleVal, Integer.parseInt(priceVal), sellerKey, descriptionVal,
                imagePath, status);

        try {
            mFirebaseDatabaseReference.child("Products").child(key).setValue(product);
            Toast.makeText(UploadProduct.this, "Product registered", Toast.LENGTH_SHORT).show();
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    finish();
                }
            },2000);

        } catch(Exception e) {
            Toast.makeText(this.getApplicationContext(), "Upload error", Toast.LENGTH_SHORT).show();
        }
    }
}
