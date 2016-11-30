package xchange.itesm.mx.xchange;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class RegisterUser extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private DatabaseReference mFirebaseDatabaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);

        Toast.makeText(this.getApplicationContext(), "Entrando a oncreate", Toast.LENGTH_SHORT).show();

        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabaseReference = FirebaseDatabase.getInstance().getReference();
        user = mAuth.getCurrentUser();

        mFirebaseDatabaseReference.child("Users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Toast.makeText(RegisterUser.this, "On data change", Toast.LENGTH_SHORT).show();
                try{
                    for (DataSnapshot userSnap : dataSnapshot.getChildren()) {
                        User userObj = userSnap.getValue(User.class);
                        if(userObj.getUserId().equals(user.getUid())) {
                            String userId = userObj.getUserId();
                            String userName = userObj.getUserName();
                            String name = userObj.getName();
                            String phone = userObj.getPhone();
                            double rating = userObj.getRating();
                            String address = userObj.getAddress();
                            String description = userObj.getDescription();

                            fillUserInformation(userId, userName, name, phone, rating, address, description);
                            Toast.makeText(RegisterUser.this, "Desplegando información", Toast.LENGTH_SHORT).show();
                            break;
                        }
                    }

                } catch (Exception ex) {
                    Toast.makeText(RegisterUser.this, ex.getMessage(), Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public void fillUserInformation(String userId, String userName, String name, String phone, double rating, String address, String description) {
        EditText editName = (EditText)findViewById(R.id.userName);
        EditText fullName = (EditText)findViewById(R.id.fullName);
        EditText userPhone = (EditText)findViewById(R.id.userPhone);
        EditText userRating = (EditText)findViewById(R.id.userRating);
        EditText userAddress = (EditText)findViewById(R.id.userAddress);
        EditText userDescription = (EditText)findViewById(R.id.userDescription);

        editName.setText(userName);
        fullName.setText(name);
        userPhone.setText(phone);
        userRating.setText("Rating: " + rating);
        userRating.setVisibility(View.VISIBLE);
        userAddress.setText(address);
        userDescription.setText(description);

    }

    public void updateInformation(View v) {
        //TODO update user information
    }

    public void updateUser(View v) {
        DatabaseReference users = mFirebaseDatabaseReference.child("Users");
        String key = mFirebaseDatabaseReference.child("Users").push().getKey();

        EditText username = (EditText)findViewById(R.id.userName);
        EditText fullName = (EditText)findViewById(R.id.fullName);
        EditText userPhone = (EditText)findViewById(R.id.userPhone);
        EditText userAddress = (EditText)findViewById(R.id.userAddress);
        EditText userDescription = (EditText)findViewById(R.id.userDescription);

        User userObject = new User(user.getUid(), username.getText().toString(), fullName.getText().toString(),
                userPhone.getText().toString(), 2.5, userAddress.getText().toString(), userDescription.getText().toString());

        try {
            mFirebaseDatabaseReference.child("Users").child(key).setValue(userObject);
            Toast.makeText(RegisterUser.this, "User details updated", Toast.LENGTH_SHORT).show();
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    finish();
                }
            },2000);
        } catch (Exception e) {
            Toast.makeText(this.getApplicationContext(), "Failed to update User", Toast.LENGTH_SHORT).show();
        }
    }
}
