package com.pentateuch.watersupply.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseUser;
import com.pentateuch.watersupply.App;
import com.pentateuch.watersupply.R;

public class MainActivity extends AppCompatActivity {
    private FirebaseUser user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        boolean verified = App.getInstance().getValue("isPhoneVerified",false);
        if(!verified){
            Intent intent = new Intent(this,PhoneAuthActivity.class);
            startActivity(intent);
        }
    }
}
