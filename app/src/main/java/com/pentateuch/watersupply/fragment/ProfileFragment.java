package com.pentateuch.watersupply.fragment;


import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;
import com.pentateuch.watersupply.App;
import com.pentateuch.watersupply.R;
import com.pentateuch.watersupply.activity.AccountActivity;
import com.pentateuch.watersupply.model.User;
import com.pentateuch.watersupply.utils.AlertDialogMaker;
import com.pentateuch.watersupply.utils.LocationHelper;
import com.pentateuch.watersupply.utils.LocationUpdateListener;
import com.pentateuch.watersupply.utils.ProgressDialog;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment implements View.OnClickListener, OnCompleteListener<Void>, LocationUpdateListener {
    private View rootView;
    private TextView addressTextView, pinTextView, phoneTextView;
    private User user;
    private boolean updated;
    private ProgressDialog dialog;
    private boolean isVerified;

    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_profile, container, false);
        user = App.getInstance().getUser();
        addressTextView = rootView.findViewById(R.id.tv_address_profile);
        pinTextView = rootView.findViewById(R.id.tv_pin_address);
        phoneTextView = rootView.findViewById(R.id.tv_phone_profile);
        addressTextView.setText(user.getAddress());
        pinTextView.setText(user.getPinCode());
        phoneTextView.setText(user.getNumber());
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Button addressButton = view.findViewById(R.id.btn_address_change);
        Button pinButton = view.findViewById(R.id.btn_pin_change);
        Button phoneButton = view.findViewById(R.id.btn_phone_change);
        Button accountButton = view.findViewById(R.id.btn_account_setting);
        Button updateButton = view.findViewById(R.id.btn_profile_update);
        Button locationButton = view.findViewById(R.id.btn_location_profile);
        addressButton.setOnClickListener(this);
        phoneButton.setOnClickListener(this);
        pinButton.setOnClickListener(this);
        accountButton.setOnClickListener(this);
        updateButton.setOnClickListener(this);
        locationButton.setOnClickListener(this);
        dialog = new ProgressDialog(rootView.getContext());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_address_change:
                AlertDialogMaker.getEditTextAlertDialog(rootView.getContext(), "Address", addressTextView.getText().toString(), InputType.TYPE_TEXT_FLAG_MULTI_LINE, new AlertDialogMaker.AlertClickListener() {
                    @Override
                    public void onClick(String text) {
                        addressTextView.setText(text);
                        updated = true;
                    }
                });
                break;
            case R.id.btn_pin_change:
                AlertDialogMaker.getEditTextAlertDialog(rootView.getContext(), "PinCode", pinTextView.getText().toString(), InputType.TYPE_TEXT_FLAG_MULTI_LINE, new AlertDialogMaker.AlertClickListener() {
                    @Override
                    public void onClick(String text) {
                        pinTextView.setText(text);
                        updated = true;
                    }
                });
                break;
            case R.id.btn_phone_change:
                AlertDialogMaker.getEditTextAlertDialog(rootView.getContext(), "Phone", user.getNumber(), InputType.TYPE_TEXT_FLAG_MULTI_LINE, new AlertDialogMaker.AlertClickListener() {
                    @Override
                    public void onClick(String text) {
                        phoneTextView.setText(text);
                        updated = true;
                    }
                });
                break;
            case R.id.btn_account_setting:
                Intent intent = new Intent(rootView.getContext(), AccountActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_profile_update:
                updateSettings();
                break;
            case R.id.btn_location_profile:
                dialog.showProgressAt(rootView);
                LocationHelper helper = new LocationHelper(getActivity(), this);
                helper.findCurrentLocation();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        user.setVerified(App.getInstance().getValue("isPhoneVerified",false));
    }

    private void updateSettings() {
        dialog.showProgressAt(rootView);
        if (updated) {
            String address = addressTextView.getText().toString();
            String pin = pinTextView.getText().toString();
            String phone = phoneTextView.getText().toString();
            if (!phone.equals(user.getNumber()))
                user.setVerified(false);
            user.setPinCode(pin);
            user.setNumber(phone);
            user.setAddress(address);
            User updatedUser = new User(user.getName(), phone, user.getEmail(), address, pin, user.getToken(), user.isVerified());
            FirebaseDatabase.getInstance().getReference().child("RegisteredUsers").child(user.getUid()).setValue(updatedUser).addOnCompleteListener(this);
        } else {
            Toast.makeText(rootView.getContext(), "Nothing to Update", Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        }
    }

    @Override
    public void onComplete(@NonNull Task<Void> task) {
        Toast.makeText(rootView.getContext(), "Updated Successfully", Toast.LENGTH_SHORT).show();
        App.getInstance().setUser(user);
        App.getInstance().setValue("current", user);
        App.getInstance().setValue("isPhoneVerified", user.isVerified());
        updated = false;
        dialog.dismiss();
    }

    @Override
    public void onChange(String... texts) {
        dialog.dismiss();
        addressTextView.setText(texts[0]);
        pinTextView.setText(texts[1]);
        updated = true;
    }

    @Override
    public void onCancel() {
        dialog.dismiss();
    }
}
