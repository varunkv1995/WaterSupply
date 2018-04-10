package com.pentateuch.watersupply.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.pentateuch.watersupply.App;
import com.pentateuch.watersupply.R;
import com.pentateuch.watersupply.model.Product;
import com.pentateuch.watersupply.model.User;

public class CashOnDelivery extends AppCompatActivity implements OnCompleteListener<Void> {

    ImageView product_cash_on_delivery;
    TextView quantity, totalTextView, adressTextView, dateTimeTextView;
    private Product product;
    private User user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cash_on_delivery);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            product = extras.getParcelable("product");
        }
        dateTimeTextView = findViewById(R.id.date_and_time);
        user = App.getInstance().getUser();
        quantity = findViewById(R.id.tv_quantity);
        product_cash_on_delivery = findViewById(R.id.image_product_view_cash);
        product_cash_on_delivery.setImageResource(product.getDrawable());
        adressTextView = findViewById(R.id.tv_adress);
        quantity = findViewById(R.id.tv_quantity);
        quantity.setText(String.valueOf(product.getQuantity()));
        totalTextView = findViewById(R.id.tv_total_amount);
        totalTextView.setText(product.getTotalCostInRs());
        adressTextView.setText(this.user.getAddress());
        dateTimeTextView.setText(product.getTime());
        dateTimeTextView.append(product.getDate());

    }


    public void ConfirmOrder(View view) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        product.setStatus("pending");
        reference.child("MyOrder").child(user.getUid()).push().setValue(product).addOnCompleteListener(this);

    }

    @Override
    public void onComplete(@NonNull Task<Void> task) {
            if(task.isSuccessful()){
                Toast.makeText(this, "Order is Confirmed", Toast.LENGTH_LONG).show();
            }
    }
}
