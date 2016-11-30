package xchange.itesm.mx.xchange;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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

        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabaseReference = FirebaseDatabase.getInstance().getReference();
        user = mAuth.getCurrentUser();

        Button buttonUpdate = (Button)baseView.findViewById(R.id.registerUser);
        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateUser();
            }
        });

        mFirebaseDatabaseReference.child("Users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                
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

                            break;
                        }
                    }

                } catch (Exception ex) {
                    Toast.makeText(context.getApplicationContext(), ex.getMessage(), Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return baseView;
    }

    public void fillUserInformation(String userId, String userName, String name, String phone, double rating, String address, String description) {
        EditText editName = (EditText)baseView.findViewById(R.id.userName);
        EditText fullName = (EditText)baseView.findViewById(R.id.fullName);
        EditText userPhone = (EditText)baseView.findViewById(R.id.userPhone);
        EditText userRating = (EditText)baseView.findViewById(R.id.userRating);
        EditText userAddress = (EditText)baseView.findViewById(R.id.userAddress);
        EditText userDescription = (EditText)baseView.findViewById(R.id.userDescription);

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

    public void updateUser() {
        DatabaseReference users = mFirebaseDatabaseReference.child("Users");
        String key = mFirebaseDatabaseReference.child("Users").push().getKey();

        EditText username = (EditText)baseView.findViewById(R.id.userName);
        EditText fullName = (EditText)baseView.findViewById(R.id.fullName);
        EditText userPhone = (EditText)baseView.findViewById(R.id.userPhone);
        EditText userAddress = (EditText)baseView.findViewById(R.id.userAddress);
        EditText userDescription = (EditText)baseView.findViewById(R.id.userDescription);

        User userObject = new User(user.getUid(), username.getText().toString(), fullName.getText().toString(),
                userPhone.getText().toString(), 2.5, userAddress.getText().toString(), userDescription.getText().toString());

        try {
            mFirebaseDatabaseReference.child("Users").child(key).setValue(userObject);
            Toast.makeText(context.getApplicationContext(), "User details updated", Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            Toast.makeText(context.getApplicationContext(), "Failed to update User", Toast.LENGTH_SHORT).show();
        }
    }

}
