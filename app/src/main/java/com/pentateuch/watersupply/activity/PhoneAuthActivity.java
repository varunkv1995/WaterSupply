package com.pentateuch.watersupply.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.pentateuch.watersupply.App;
import com.pentateuch.watersupply.R;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class PhoneAuthActivity extends AppCompatActivity implements
        View.OnClickListener, OnCompleteListener<Void> {

    private static final String TAG = "PhoneAuthActivity";
    private static final String KEY_VERIFY_IN_PROGRESS = "key_verify_in_progress";

    private static final int STATE_INITIALIZED = 1;
    private static final int STATE_CODE_SENT = 2;
    private static final int STATE_VERIFY_FAILED = 3;
    private static final int STATE_VERIFY_SUCCESS = 4;
    private static final int STATE_SIGNIN_FAILED = 5;
    private static final int STATE_SIGNIN_SUCCESS = 6;
    // [START declare_auth]
    private boolean mVerificationInProgress = false;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private EditText mVerificationField;
    private RelativeLayout layoutMain;
    private String phoneNumber = "9008980341";
    private FirebaseUser user;
    private String mVerificationId;
    private ProgressBar progressBar;

    public PhoneAuthActivity(){
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_auth);

        // Restore instance state
        if (savedInstanceState != null) {
            onRestoreInstanceState(savedInstanceState);
        }
        Bundle extras = getIntent().getExtras();
        if (extras != null)
            phoneNumber = extras.getString("phone", "9008980341");
        user = FirebaseAuth.getInstance().getCurrentUser();
        // Assign views


        TextView mDetailText = (TextView) findViewById(R.id.tv_phone_otp);
        mDetailText.setText(String.format("%s %s", getString(R.string.otp_text), phoneNumber));

        mVerificationField = (EditText) findViewById(R.id.field_verification_code);

        Button mVerifyButton = (Button) findViewById(R.id.button_verify_phone);
        Button mResendButton = (Button) findViewById(R.id.btn_resend);
        progressBar = findViewById(R.id.auth_progress);
        layoutMain = findViewById(R.id.root_auth);

        mVerifyButton.setOnClickListener(this);
        mResendButton.setOnClickListener(this);

        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {

                Log.d(TAG, "onVerificationCompleted:" + credential);

                mVerificationInProgress = false;
                if (user != null)
                    user.updatePhoneNumber(credential);
                App.getInstance().setValue("isPhoneVerified",true);
                updateUI(STATE_VERIFY_SUCCESS, credential);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {

                Log.w(TAG, "onVerificationFailed", e);
                // [START_EXCLUDE silent]
                mVerificationInProgress = false;

                updateUI(STATE_VERIFY_FAILED, null);
            }

            @Override
            public void onCodeSent(String verificationId,
                                   PhoneAuthProvider.ForceResendingToken token) {

                Log.d(TAG, "onCodeSent:" + verificationId);
                mVerificationId = verificationId;
                mResendToken = token;

                updateUI(STATE_CODE_SENT, null);
            }
        };
    }

    @Override
    public void onStart() {
        super.onStart();
        if (!mVerificationInProgress && validatePhoneNumber()) {
            startPhoneNumberVerification(phoneNumber);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(KEY_VERIFY_IN_PROGRESS, mVerificationInProgress);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mVerificationInProgress = savedInstanceState.getBoolean(KEY_VERIFY_IN_PROGRESS);
    }


    private void startPhoneNumberVerification(String phoneNumber) {
        // [START start_phone_auth]
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+91" + phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks);

        mVerificationInProgress = true;
    }

    // [START resend_verification]
    private void resendVerificationCode(String phoneNumber,
                                        PhoneAuthProvider.ForceResendingToken token) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+91" + phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks,         // OnVerificationStateChangedCallbacks
                token);             // ForceResendingToken from callbacks
    }


    private void updateUI(int uiState, PhoneAuthCredential credential) {
        switch (uiState) {
            case STATE_INITIALIZED:

                break;
            case STATE_CODE_SENT:
                //  mDetailText.setText(R.string.status_code_sent);
                Toast.makeText(PhoneAuthActivity.this, "OTP Sent", Toast.LENGTH_LONG).show();
                break;
            case STATE_VERIFY_FAILED:
                //  mDetailText.setText(R.string.status_verification_failed);
                Toast.makeText(PhoneAuthActivity.this, "Verification failed", Toast.LENGTH_LONG).show();
                break;
            case STATE_VERIFY_SUCCESS:
                Toast.makeText(PhoneAuthActivity.this, "Verification success", Toast.LENGTH_LONG).show();
                updateDb();
                break;
            case STATE_SIGNIN_FAILED:
                // No-op, handled by sign-in check
                //  mDetailText.setText(R.string.status_sign_in_failed);
                break;
            case STATE_SIGNIN_SUCCESS:
                // Np-op, handled by sign-in check
                break;
        }
    }

    private boolean validatePhoneNumber() {
        return !TextUtils.isEmpty(phoneNumber);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_verify_phone:
                String code = mVerificationField.getText().toString();
                if (TextUtils.isEmpty(code)) {
                    mVerificationField.setError("Cannot be empty.");
                    return;
                }
                verify(code);
                break;
            case R.id.btn_resend:
                resendVerificationCode(phoneNumber, mResendToken);
                break;
        }
    }

    private void verify(String code) {
        showProgress(true);
        FirebaseAuth auth = FirebaseAuth.getInstance();
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId,code);
        auth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                    updateDb();
                else {
                    showProgress(false);
                    mVerificationField.setError("Wrong OTP");
                }
            }
        });
    }

    private void updateDb(){
        FirebaseDatabase.getInstance().getReference().child("RegisteredUsers").child(user.getUid()).child("verified").setValue(true).addOnCompleteListener(this);
        showProgress(true);
    }

    private void showProgress(boolean show){
        if(show) {
            progressBar.setVisibility(View.VISIBLE);
            layoutMain.setVisibility(View.GONE);
        }
        else {
            progressBar.setVisibility(View.GONE);
            layoutMain.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onComplete(@NonNull Task<Void> task) {
        App.getInstance().setValue("isPhoneVerified", true);
        Intent intent = new Intent(PhoneAuthActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
