package com.pentateuch.watersupply.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.pentateuch.watersupply.App;
import com.pentateuch.watersupply.R;
import com.pentateuch.watersupply.model.Product;
import com.pentateuch.watersupply.model.User;

public class OrderActivity extends AppCompatActivity {
 User user;
    TextView fName, pNumber, emailAddress;
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
        user = App.getInstance().getUser();
        fName.setText(this.user.getName());
        pNumber.setText(this.user.getNumber());
        emailAddress.setText(this.user.getEmail());
        paynowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), PayMentGateWay.class);
                intent.putExtra("FIRST_NAME", user.getName().trim());
                intent.putExtra("PHONE_NUMBER", user.getNumber().trim());
                intent.putExtra("EMAIL_ADDRESS", user.getEmail().trim());
                intent.putExtra("TEXT_PRICE", String.valueOf(product.getPrice() * product.getQuantity()));
                startActivity(intent);

            }
        });

    }
}
