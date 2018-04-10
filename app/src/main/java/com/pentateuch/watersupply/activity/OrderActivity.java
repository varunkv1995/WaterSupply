package com.pentateuch.watersupply.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.pentateuch.watersupply.R;
import com.pentateuch.watersupply.model.Product;

public class OrderActivity extends AppCompatActivity {

    EditText fName, pNumber, emailAddress;
    private Product product;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_u);

        fName = findViewById(R.id.f_name);
        pNumber = findViewById(R.id.p_number);
        emailAddress = findViewById(R.id.email_Address);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            product = extras.getParcelable("product");
        }


        TextView totalTextView = findViewById(R.id.tv_total_price);
        totalTextView.setText(product.getTotalCostInRs());
        Button paynowButton = findViewById(R.id.Paynow);

        paynowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String getFname = fName.getText().toString().trim();
                String getPhone = pNumber.getText().toString().trim();
                String getEmail = emailAddress.getText().toString().trim();
                if (TextUtils.isEmpty(getFname)) {
                    fName.setError("This field can' be empty");
                    return;
                }
                if (TextUtils.isEmpty(getPhone)) {
                    pNumber.setError("This field can' be empty");
                    return;
                }

                if (TextUtils.isEmpty(getEmail)) {
                    emailAddress.setError("This field can' be empty");
                    return;
                }
                if (getPhone.length() < 10) {
                    pNumber.setError("numbers must be min 10 digits");
                    return;
                }
                if (!getEmail.matches("^[a-zA-Z0-9.]+@[a-zA-Z]+.(com|in)")) {
                    emailAddress.setError("Invalid Email");
                    return;
                }
                Intent intent = new Intent(getApplicationContext(), PayMentGateWay.class);
                intent.putExtra("FIRST_NAME", getFname);
                intent.putExtra("PHONE_NUMBER", getPhone);
                intent.putExtra("EMAIL_ADDRESS", getEmail);
                intent.putExtra("TEXT_PRICE", String.valueOf(product.getPrice() * product.getQuantity()));
                startActivity(intent);

            }
        });

    }
}
