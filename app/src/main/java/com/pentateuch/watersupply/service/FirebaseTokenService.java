package com.pentateuch.watersupply.service;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.pentateuch.watersupply.App;
import com.pentateuch.watersupply.utils.NotifyConfig;
import com.pentateuch.watersupply.utils.OnSaveListener;

public class FirebaseTokenService extends FirebaseInstanceIdService implements OnSaveListener<String> {
    private static final String TAG = FirebaseTokenService.class.getSimpleName();
    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        String token = FirebaseInstanceId.getInstance().getToken();
        saveToPref(token);
        saveToFireBase(token,this);
    }

    private void saveToFireBase(final String token, final OnSaveListener<String> listener) {
        Log.e(TAG, "Firebase reg id: " + token);
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if(currentUser == null)
            listener.onCancel();
        FirebaseDatabase instance = FirebaseDatabase.getInstance();
        DatabaseReference registeredUsers = instance.getReference().child("RegisteredUsers").child(currentUser.getUid());
        registeredUsers.child("token").setValue(token).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                listener.onSaved(token);
            }
        });
    }

    private void saveToPref(String token) {
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(App.PREF_NAME, MODE_PRIVATE);
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putString("token",token);
        edit.apply();

    }

    @Override
    public void onCreate() {
        super.onCreate();
        String token = FirebaseInstanceId.getInstance().getToken();
        saveToPref(token);
        saveToFireBase(token,this);
    }

    @Override
    public void onSaved(String refreshedToken) {
        Intent registrationComplete = new Intent(NotifyConfig.REGISTRATION_COMPLETE);
        registrationComplete.putExtra("token", refreshedToken);
        LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);
    }

    @Override
    public void onCancel() {

    }
}
