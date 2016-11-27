package xchange.itesm.mx.xchange;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Initialize authentication
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
            }
        };

        Intent intent = getIntent();

        String username = intent.getStringExtra("username");
        String userId = intent.getStringExtra("userId");

        TextView user = (TextView) findViewById(R.id.username);
        TextView id = (TextView) findViewById(R.id.userid);

        user.setText(username);
        id.setText(userId);
    }

    protected void signOut() {
        try {
            mAuth.signOut();
        } catch (Exception e) {
            Toast.makeText(this.getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}
