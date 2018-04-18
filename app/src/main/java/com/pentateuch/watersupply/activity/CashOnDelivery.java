package com.pentateuch.watersupply.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.pentateuch.watersupply.App;
import com.pentateuch.watersupply.R;
import com.pentateuch.watersupply.model.Product;
import com.pentateuch.watersupply.model.User;
import com.pentateuch.watersupply.utils.Helper;
import com.pentateuch.watersupply.utils.ProgressDialog;
import com.tech.imageloader.core.ImageFetcher;

public class CashOnDelivery extends AppCompatActivity implements OnCompleteListener<Void> {

    ImageView product_cash_on_delivery;
    TextView quantity;
    TextView totalTextView;
    TextView addressTextView;
    TextView dateTimeTextView;
    private Product product;
    private User user;
    private String transactionID;
    private ProgressDialog dialog;
    private RelativeLayout layout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cash_on_delivery);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            product = extras.getParcelable("product");
        }
        layout = findViewById(R.id.root_cod);
        dateTimeTextView = findViewById(R.id.tv_date_time);
        user = App.getInstance().getUser();
        quantity = findViewById(R.id.tv_quantity);
        product_cash_on_delivery = findViewById(R.id.image_product_view_cash);
        dateTimeTextView.setText(product.getDate());
        ImageFetcher.with(this).from(product.getImageUrl()).into(product_cash_on_delivery);
        addressTextView = findViewById(R.id.tv_address);
        quantity = findViewById(R.id.tv_quantity);
        quantity.setText(String.valueOf(product.getQuantity()));
        totalTextView = findViewById(R.id.tv_amount);
        totalTextView.setText(product.getTotalCostInRs());
        addressTextView.setText(this.user.getAddress());
        dateTimeTextView.append(" " + product.getDate());

    }


    public void ConfirmOrder(View view) {
        dialog = new ProgressDialog(this);
        dialog.showProgressAt(layout);
        product.setDest(addressTextView.getText().toString());
        transactionID = new Helper().getTransactionID();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        reference.child("MyOrder").child(user.getUid()).child(transactionID).setValue(product).addOnCompleteListener(this);

    }

    @Override
    public void onComplete(@NonNull Task<Void> task) {
        if (task.isSuccessful()) {
            dialog.dismiss();
            Intent intent = new Intent(this, OrderSummaryActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.putExtra("transactionID", transactionID);
            intent.putExtra("amount",product.getTotalCostInRs());
            startActivity(intent);
        }
    }
}
