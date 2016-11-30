package xchange.itesm.mx.xchange;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DrawerLayout drawer;
    private ActionBarDrawerToggle toggle;
    private Toolbar toolbar;
    private NavigationView navigationView;
    private ActionBar actionBar;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Initialize authentication
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                user = firebaseAuth.getCurrentUser();
            }
        };


        initializeToolbar();
        initializeDrawer();
        initializeFragment();
    }

    protected void signOut() {
        try {
            mAuth.signOut();
            Intent intent = new Intent(this.getApplicationContext(), ActivityLogin.class);
            startActivity(intent);
            this.finish();
        } catch (Exception e) {
            Toast.makeText(this.getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        Fragment fragment = null;
        Class fragmentClass = null;
        Bundle bundle = new Bundle();

        switch(id) {
            case R.id.nav_products:
                fragmentClass = FragmentProduct.class;
                break;
            //case R.id.nav_wish:
            //    fragmentClass = FragmentProduct.class;
            //   break;
            case R.id.nav_explore:
                fragmentClass = FragmentExplore.class;
                break;
            case R.id.logout:
                signOut();
                break;
            case R.id.update_user:
                fragmentClass = FragmentProfile.class;
                break;
            default:
                break;
        }

        try {
            if(fragmentClass != null){
                fragment = (Fragment) fragmentClass.newInstance();
                fragment.setArguments(bundle);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(id != R.id.logout) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.content_fragments, fragment).commit();
        }
        return true;
    }

    public void initializeToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("XChange");
            toolbar.setTitleTextColor(Color.WHITE);
        }
    }

    public void initializeDrawer(){
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.getMenu().getItem(0).setChecked(true);
    }

    public void initializeFragment(){
        //TODO add principal fragment
        Fragment fragment = null;
        Class fragmentClass = FragmentExplore.class;
        Bundle bundle = new Bundle();
        try {
            if(fragmentClass != null){
                fragment = (Fragment) fragmentClass.newInstance();
                fragment.setArguments(bundle);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_fragments, fragment).commit();
    }
}
