package com.pentateuch.watersupply.activity;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pentateuch.watersupply.App;
import com.pentateuch.watersupply.R;
import com.pentateuch.watersupply.fragment.CartFragment;
import com.pentateuch.watersupply.fragment.HomeFragment;
import com.pentateuch.watersupply.model.User;

public class MainActivity extends AppCompatActivity implements ValueEventListener, NavigationView.OnNavigationItemSelectedListener {
    private CoordinatorLayout rootLayout;
    private int currentFragment;
    private DrawerLayout drawer;
    private Handler mHandler;
    private FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rootLayout = findViewById(R.id.root_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        //drawer.setDrawerListener(toggle);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        mHandler = new Handler();
        database = FirebaseDatabase.getInstance();
    }

    @Override
    protected void onStart() {
        super.onStart();
        loadFragment(R.id.menu_home);
        boolean verified = App.getInstance().getValue("isPhoneVerified", false);
        if (!verified) {
            User user = App.getInstance().getUser();
            DatabaseReference reference = database.getReference().child("RegisteredUsers").child(user.getUid());
            reference.addValueEventListener(this);
        }
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        final User value = dataSnapshot.getValue(User.class);
        if (value != null) {
            App.getInstance().setUser(value);
            if (!value.isVerified()) {
                Snackbar snackbar = Snackbar.make(rootLayout, "Verify your Phone No", Snackbar.LENGTH_INDEFINITE);
                snackbar.setAction("Verify", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(MainActivity.this, PhoneAuthActivity.class);
                        intent.putExtra("phone", value.getNumber());
                        startActivity(intent);
                        finish();
                    }
                });
                snackbar.show();
            }
            else
                App.getInstance().setValue("isPhoneVerified",true);
        }
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_logout:
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (currentFragment != R.id.menu_home) {
            loadFragment(R.id.menu_home);
            return;
        }
        finish();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        switch (itemId) {
            case R.id.menu_home:
                loadFragment(itemId);
                break;
            case R.id.menu_cart:
                loadFragment(itemId);
                break;
        }

        drawer.closeDrawer(Gravity.START);
        return true;
    }

    private void loadFragment(int id) {
        currentFragment = id;
        final Fragment fragment = getFragment(id);
        if (fragment != null) {
            final String tag = fragment.getClass().getSimpleName();
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.fragment_main, fragment, tag);
                    fragmentTransaction.addToBackStack(tag);
                    fragmentTransaction.commit();
                }
            });
        }
    }

    private Fragment getFragment(int id) {
        switch (id) {
            case R.id.menu_home:
                setTitle("Home");
                return new HomeFragment();
            case R.id.menu_cart:
                setTitle("Cart");
                return new CartFragment();
        }
        return null;
    }
}
