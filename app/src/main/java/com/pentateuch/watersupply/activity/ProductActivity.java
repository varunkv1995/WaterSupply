package com.pentateuch.watersupply.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v7.app.AppCompatActivity;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

public class ProductActivity extends AppCompatActivity implements OnCompleteListener<Void> {
    private Product product;
    private TextView quantityTextView,totalTextView;
    private BottomSheetBehavior sheetBehavior;
    private LinearLayout bottomLayout;
    private TextView addressTextView;
    private User user;

    public ProductActivity(){
        user = App.getInstance().getUser();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);
        Bundle extras = getIntent().getExtras();
        if (extras != null && extras.containsKey("product")) {
            product = extras.getParcelable("product");
        }
        ImageView productImageView = findViewById(R.id.image_product_view);
        productImageView.setImageResource(product.getDrawable());
        quantityTextView = findViewById(R.id.tv_product_quantity);
        TextView priceTextView = findViewById(R.id.tv_product_price);
        String price = String.format("Price : %s Rs", product.getPrice());
        Spannable wordSpan = new SpannableString(price);
        wordSpan.setSpan(new ForegroundColorSpan(Color.BLUE), 8, price.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        priceTextView.setText(wordSpan);
        totalTextView = findViewById(R.id.tv_total_cost);
        totalTextView.setText(product.getCostInRs());
        bottomLayout = findViewById(R.id.bottom_sheet);
        sheetBehavior = BottomSheetBehavior.from(bottomLayout);
        addressTextView = findViewById(R.id.tv_product_address);
        addressTextView.setText(user.getAddress());
    }

    public void onIncrease(View view) {
        product.increaseQuantity();
        quantityTextView.setText(String.valueOf(product.getQuantity()));
        totalTextView.setText(product.getCostInRs());
    }

    public void onDecrease(View view) {
        if(product.getQuantity() > 1) {
            product.decreamentQuantity();
            quantityTextView.setText(String.valueOf(product.getQuantity()));
            totalTextView.setText(product.getCostInRs());
        }
    }

    public void onBuy(View view) {
        if (sheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
            sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        } else {
            sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        }
    }

    public void addCart(View view) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        reference.child("Carts").child(user.getUid()).push().setValue(product).addOnCompleteListener(this);
    }

    @Override
    public void onComplete(@NonNull Task<Void> task) {
        if(task.isSuccessful()){
            Toast.makeText(this,"Added to Cart",Toast.LENGTH_SHORT).show();
        }
    }
}
