package com.pentateuch.watersupply.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pentateuch.watersupply.App;
import com.pentateuch.watersupply.R;
import com.pentateuch.watersupply.model.User;

public class MainActivity extends AppCompatActivity implements ValueEventListener {
    private LinearLayout rootLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rootLayout = findViewById(R.id.root_main);

    }

    @Override
    public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {
        boolean verified = App.getInstance().getValue("isPhoneVerified", false);
        if (!verified) {
            User user = App.getInstance().getUser();
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference reference = database.getReference().child("RegisteredUsers").child(user.getUid());
            reference.addValueEventListener(this);
        }
        return super.onCreateView(parent, name, context, attrs);
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        final User value = dataSnapshot.getValue(User.class);
        if (value != null)
            if (!value.isVerified()) {
                Snackbar snackbar = Snackbar.make(rootLayout,"Verify your Phone No",Snackbar.LENGTH_INDEFINITE);
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
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_logout:
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(this,LoginActivity.class);
                startActivity(intent);
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
