package com.pentateuch.watersupply.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.pentateuch.watersupply.R;
import com.pentateuch.watersupply.model.User;

public class SignUpActivity extends AppCompatActivity implements OnCompleteListener<AuthResult> {
    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;
    private EditText editTextUser,editTextNumber,editTextPass,editTextCPass,editTextAddress,editTextPin,editTextEmail;
    private ProgressDialog dialog;
    private User user;
    private LinearLayout rootLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        editTextUser = (EditText) findViewById(R.id.editTextUser);
        editTextNumber = (EditText) findViewById(R.id.editTextNumber);
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextPass = (EditText) findViewById(R.id.editTextPass);
        editTextCPass = (EditText) findViewById(R.id.editTextCPass);
        editTextAddress = (EditText) findViewById(R.id.editTextAddress);
        editTextPin = (EditText) findViewById(R.id.editTextPin);
        Button button = (Button) findViewById(R.id.buttonNext);
        mAuth = FirebaseAuth.getInstance();
        databaseReference= FirebaseDatabase.getInstance().getReference().child("RegisteredUsers");
        rootLayout = findViewById(R.id.root_sign_up);

        button.setOnClickListener(new View.OnClickListener() {



            @Override
            public void onClick(final View v) {
                dialog = new ProgressDialog(SignUpActivity.this);
                dialog.setMessage("Registering User");

                final String name = editTextUser.getText().toString();
                final String number = editTextNumber.getText().toString();
                final String password = editTextPass.getText().toString();
                String confirm = editTextCPass.getText().toString();
                final String address = editTextAddress.getText().toString();
                final String pin = editTextPin.getText().toString();
                final String email = editTextEmail.getText().toString();


                if (TextUtils.isEmpty(name)) {
                    editTextUser.setError("This field can' be empty");
                    return;
                }
                if (TextUtils.isEmpty(number)) {
                    editTextNumber.setError("This field can' be empty");
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    editTextPass.setError("This field can' be empty");
                    return;
                }
                if (TextUtils.isEmpty(email)) {
                    editTextEmail.setError("This field can' be empty");
                    return;
                }
                if (!password.equals(confirm)) {
                    editTextCPass.setError("passwords not match");
                    return;
                }
                if (password.length() < 6) {
                    editTextPass.setError("passwords must be min 4 char");
                    return;
                }
                if (TextUtils.isEmpty(address)) {
                    editTextAddress.setError("This field can' be empty");
                    return;
                }
                if (TextUtils.isEmpty(pin)) {
                    editTextPin.setError("This field can' be empty");
                    return;
                }
                if (pin.length() < 6) {
                    editTextPin.setError("Invalid Pincode");
                    return;
                }
                if (!email.matches("^[a-zA-Z0-9.]+@[a-zA-Z]+.(com|in)")) {
                    editTextEmail.setError("Invalid Email");
                    return;
                }
                dialog.show();

                user=new User(name,number,email,password,address,pin);
                mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(SignUpActivity.this);

            }
        });

    }

    @Override
    public void onComplete(@NonNull Task<AuthResult> task) {
        dialog.dismiss();
        if (task.isSuccessful()){
            databaseReference.push().setValue(user);
            Intent intent = new Intent(SignUpActivity.this, PhoneAuthActivity.class);
            intent.putExtra("phone",user.getNumber());
            startActivity(intent);
        }
        else {
            try {
                throw task.getException();
            } catch (FirebaseAuthUserCollisionException e) {
                Snackbar.make(rootLayout, e.getMessage(), Snackbar.LENGTH_LONG).show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
