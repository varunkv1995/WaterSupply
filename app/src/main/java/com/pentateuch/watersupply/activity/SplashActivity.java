package com.pentateuch.watersupply.activity;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.pentateuch.watersupply.App;
import com.pentateuch.watersupply.R;
import com.pentateuch.watersupply.model.User;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        new Handler().postDelayed(new Runnable() {

            // Using handler with postDelayed called runnable run method

            @Override
            public void run() {
                FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                if (currentUser == null) {
                    Intent i = new Intent(SplashActivity.this, LoginActivity.class);
                    startActivity(i);
                } else {
                    App.getInstance().setUser(new User(currentUser.getUid(), currentUser.getDisplayName(),currentUser.getPhoneNumber(),currentUser.getEmail()));
                    Intent i = new Intent(SplashActivity.this, MainActivity.class);
                    i.putExtra("email",currentUser.getEmail());
                    startActivity(i);
                }

                // close this activity
                finish();
            }
        }, 5 * 1000); // wait for 5 seconds
    }
}
