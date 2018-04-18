package com.pentateuch.watersupply.activity;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.messaging.FirebaseMessaging;
import com.pentateuch.watersupply.App;
import com.pentateuch.watersupply.R;
import com.pentateuch.watersupply.fragment.CartFragment;
import com.pentateuch.watersupply.fragment.HomeFragment;
import com.pentateuch.watersupply.fragment.MyOrderFragment;
import com.pentateuch.watersupply.fragment.ProfileFragment;
import com.pentateuch.watersupply.model.User;
import com.pentateuch.watersupply.utils.NotifyConfig;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private CoordinatorLayout rootLayout;
    private int currentFragmentId;
    private DrawerLayout drawer;
    private Handler mHandler;
    private NavigationView navigationView;
    private BroadcastReceiver mRegistrationBroadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rootLayout = findViewById(R.id.root_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        //drawer.setDrawerListener(toggle);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        mHandler = new Handler();
        createBroadCast();
    }

    private void createBroadCast() {
        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                // checking for type intent filter
                if (intent.getAction().equals(NotifyConfig.REGISTRATION_COMPLETE)) {
                    // gcm successfully registered
                    // now subscribe to `global` topic to receive app wide notifications
                    FirebaseMessaging.getInstance().subscribeToTopic(NotifyConfig.USER);

                } else if (intent.getAction().equals(NotifyConfig.PUSH_NOTIFICATION)) {
                    // new push notification is received

                    String message = intent.getStringExtra("message");

                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                }
            }
        };

    }

    @Override
    protected void onStart() {
        super.onStart();
        loadFragment(R.id.menu_home);
        boolean verified = App.getInstance().getValue("isPhoneVerified", false);
        User user = App.getInstance().getValueFromJson("current", new User());
        App.getInstance().setUser(user);
        if (!verified) {
            if (!user.isVerified())
                checkUserVerified(user);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        startBroadCast();
    }

    private void startBroadCast() {
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(NotifyConfig.REGISTRATION_COMPLETE));

        // register new push message receiver
        // by doing this, the activity will be notified each time a new message arrives
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(NotifyConfig.PUSH_NOTIFICATION));
    }

    private void checkUserVerified(final User user) {
        Snackbar snackbar = Snackbar.make(rootLayout, "Verify your Phone No", Snackbar.LENGTH_INDEFINITE);
        snackbar.setAction("Verify", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, PhoneAuthActivity.class);
                intent.putExtra("phone", user.getNumber());
                startActivity(intent);
                finish();
            }
        });
        snackbar.show();
    }

    private void logout() {
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(navigationView)) {
            drawer.closeDrawer(Gravity.START);
            return;
        }
        if (currentFragmentId != R.id.menu_home) {
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

            case R.id.menu_profile:
                loadFragment(itemId);
                break;
            case R.id.menu_logout:
                logout();
                break;
            case R.id.menu_order:
                loadFragment(itemId);
                break;
        }

        drawer.closeDrawer(Gravity.START);
        return true;
    }

    private void loadFragment(int id) {
        if (currentFragmentId == id) {
            if (drawer.isDrawerOpen(navigationView))
                drawer.closeDrawer(Gravity.START);
            return;
        }
        currentFragmentId = id;
        final Fragment fragment = getFragment(id);
        if (fragment != null) {
            final String tag = fragment.getClass().getSimpleName();
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.fragment_main, fragment, tag);
                    fragmentTransaction.addToBackStack(tag);
                    fragmentTransaction.commitAllowingStateLoss();
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
            case R.id.menu_profile:
                setTitle("Profile");
                return new ProfileFragment();
            case R.id.menu_order:
                setTitle("MyOrder");
                return new MyOrderFragment();
        }
        return null;
    }
}
