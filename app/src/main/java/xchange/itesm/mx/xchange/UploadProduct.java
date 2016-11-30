package xchange.itesm.mx.xchange;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UploadProduct extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseUser user;
    private DatabaseReference mFirebaseDatabaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_product);

        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabaseReference = FirebaseDatabase.getInstance().getReference();
        user = mAuth.getCurrentUser();

        Toast.makeText(this, user.getUid(), Toast.LENGTH_SHORT).show();
    }

    public void uploadProduct(View v) {
        EditText title = (EditText) findViewById(R.id.Title);
        EditText price = (EditText) findViewById(R.id.Price);
        EditText description = (EditText) findViewById(R.id.Description);

        String titleVal = title.getText().toString();
        String priceVal = price.getText().toString();
        String descriptionVal = description.getText().toString();
        String sellerKey = user.getUid();
        String imagePath = "";
        String status = "Available";

        Product product = new Product(titleVal, Integer.parseInt(priceVal), sellerKey, descriptionVal,
                imagePath, status);

        DatabaseReference products = mFirebaseDatabaseReference.child("Products");
        String key = mFirebaseDatabaseReference.child("Products").push().getKey();

        try {
            mFirebaseDatabaseReference.child("Products").child(key).setValue(product);
            this.finish();
        } catch(Exception e) {
            Toast.makeText(this.getApplicationContext(), "Upload error", Toast.LENGTH_SHORT).show();
        }
    }
}
