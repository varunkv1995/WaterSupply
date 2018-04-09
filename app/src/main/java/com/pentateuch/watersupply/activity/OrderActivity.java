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

    private Product product;
    EditText fname, pnumber, emailAddress, rechargeAmt;
    Button Paynow;
    TextView totalTextView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_u);

        fname         = findViewById(R.id.f_name);
        pnumber       = findViewById(R.id.p_number);
        emailAddress  = findViewById(R.id.email_Address);
       // totalTextView = findViewById(R.id.textprice);
      //  Intent i = getIntent();
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            product = extras.getParcelable("product");
        }


        totalTextView = findViewById(R.id.tv_total_price);
        totalTextView.setText(product.getTotalCostInRs());
        //totaltextView.setText(""  + input);
        // rechargeAmt  = (EditText)findViewById(R.id.recharge_Amt);
        Paynow       = findViewById(R.id.Paynow);

        Paynow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String getFname = fname.getText().toString().trim();
                String getPhone = pnumber.getText().toString().trim();
                String getEmail = emailAddress.getText().toString().trim();
                // String getAmt   = rechargeAmt.getText().toString().trim();
               String gettotalTextView=totalTextView.getText().toString().trim();
                if (TextUtils.isEmpty(getFname)) {
                    fname.setError("This field can' be empty");
                    return;
                }
                if (TextUtils.isEmpty(getPhone)) {
                    pnumber.setError("This field can' be empty");
                    return;
                }

                if (TextUtils.isEmpty(getEmail)) {
                    emailAddress.setError("This field can' be empty");
                    return;
                }
                if (getPhone.length() < 10) {
                    pnumber.setError("numbers must be min 10 digits");
                    return;
                }
                if (!getEmail.matches("^[a-zA-Z0-9.]+@[a-zA-Z]+.(com|in)")) {
                    emailAddress.setError("Invalid Email");
                    return;
                }



                Intent intent = new Intent(getApplicationContext(), PayMentGateWay.class);
                intent.putExtra("FIRST_NAME",getFname);
                intent.putExtra("PHONE_NUMBER",getPhone);
                intent.putExtra("EMAIL_ADDRESS",getEmail);
               intent.putExtra("TEXT_PRICE",gettotalTextView);
                // intent.putExtra("RECHARGE_AMT",getAmt);
                startActivity(intent);
                //   startActivity(new Intent(OrderActivity.this, PayMentGateWay.class));

            }
        });

    }
}
