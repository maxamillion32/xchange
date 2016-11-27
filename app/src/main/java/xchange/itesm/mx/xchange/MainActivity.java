package xchange.itesm.mx.xchange;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Initialize authentication
        mAuth = FirebaseAuth.getInstance();

        Intent intent = getIntent();

        String username = intent.getStringExtra("username");
        String userId = intent.getStringExtra("userId");

        TextView user = (TextView) findViewById(R.id.username);
        TextView id = (TextView) findViewById(R.id.userid);

        user.setText(username);
        id.setText(userId);
    }

    protected void signOut() {
        mAuth.signOut();
    }
}
