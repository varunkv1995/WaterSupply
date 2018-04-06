package com.pentateuch.watersupply.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.pentateuch.watersupply.App;
import com.pentateuch.watersupply.R;
import com.pentateuch.watersupply.model.User;

public class AccountActivity extends AppCompatActivity implements OnCompleteListener<Void> {
    private EditText mailEditText, passwordEditText;
    private User user;
    private ProgressBar progressBar;
    private LinearLayout rootLayout;
    private int taskCount;
    private Button submitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        user = App.getInstance().getUser();
        mailEditText = findViewById(R.id.et_account_mail);
        mailEditText.setText(user.getEmail());
        passwordEditText = findViewById(R.id.et_account_pass);
        progressBar = findViewById(R.id.progress_account);
        rootLayout = findViewById(R.id.root_account);
        submitButton = findViewById(R.id.btn_account_submit);
    }

    public void submit(View view) {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            showProgress(true);
            String mail = mailEditText.getText().toString();
            String password = passwordEditText.getText().toString();
            if (!user.getEmail().equals(mail)) {
                user.setEmail(mail);
                App.getInstance().setUser(user);
                App.getInstance().setValue("current", user);
                progressBar.setVisibility(View.VISIBLE);
                taskCount++;
                currentUser.updateEmail(mail).addOnCompleteListener(this);
            }
            if (!password.isEmpty() && password.length() > 5) {
                taskCount++;
                currentUser.updatePassword(password).addOnCompleteListener(this);
            } else {
                Snackbar.make(rootLayout, "Password should have min 6 char", Snackbar.LENGTH_SHORT).show();
                view.setVisibility(View.VISIBLE);
                showProgress(false);
            }
        }
    }

    private void showProgress(boolean visible) {
        if (visible) {
            progressBar.setVisibility(View.VISIBLE);
            rootLayout.setVisibility(View.GONE);
            submitButton.setVisibility(View.GONE);
        } else {
            progressBar.setVisibility(View.GONE);
            rootLayout.setVisibility(View.VISIBLE);
            submitButton.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onComplete(@NonNull Task<Void> task) {
        if (task.isSuccessful()) {
            taskCount--;
            if (taskCount == 0)
                FirebaseDatabase.getInstance().getReference().child("RegisteredUsers").child(user.getUid()).child("email").setValue(user.getEmail()).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(AccountActivity.this, "Account has been updated", Toast.LENGTH_SHORT).show();
                            App.getInstance().clearSetting();
                            FirebaseAuth.getInstance().signOut();
                            Intent intent = new Intent(AccountActivity.this, LoginActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                        }
                    }
                });
        }
        else
        {
            Exception exception = task.getException();
            showProgress(false);
            try {
                if (exception != null) {
                    throw exception;
                }
            } catch (Exception e) {
                Snackbar.make(rootLayout,e.getMessage(),Snackbar.LENGTH_SHORT).show();
            }
        }
    }
}
