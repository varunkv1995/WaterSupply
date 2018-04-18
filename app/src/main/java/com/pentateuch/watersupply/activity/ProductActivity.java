package com.pentateuch.watersupply.activity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.pentateuch.watersupply.App;
import com.pentateuch.watersupply.R;
import com.pentateuch.watersupply.model.Product;
import com.pentateuch.watersupply.model.ProductItem;
import com.pentateuch.watersupply.model.User;
import com.pentateuch.watersupply.utils.DateTimeUtils;
import com.tech.imageloader.core.ImageFetcher;

import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

public class ProductActivity extends AppCompatActivity implements OnCompleteListener<Void> {
    private Product product;
    private RadioGroup payments;
    private TextView quantityTextView, totalTextView, dateText, timeTextView;
    private BottomSheetBehavior sheetBehavior;
    private User user;
    private LinearLayout bottomLayout;

    public ProductActivity() {
        user = App.getInstance().getUser();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);
        Bundle extras = getIntent().getExtras();
        boolean cartVisible = false;
        if (extras != null) {
            ProductItem item = extras.getParcelable("product");
            product = new Product(item);
            cartVisible = extras.getBoolean("enableCart", true);
        }
        payments = findViewById(R.id.payments);
        dateText = findViewById(R.id.tv_date);
        timeTextView = findViewById(R.id.tv_time);
        ImageView productImageView = findViewById(R.id.image_product_view);
        ImageFetcher.with(this).from(product.getImageUrl()).into(productImageView);
        quantityTextView = findViewById(R.id.tv_product_quantity);
        quantityTextView.setText(String.valueOf(product.getQuantity()));
        TextView priceTextView = findViewById(R.id.tv_product_price);
        String price = String.format("Price : %s Rs", product.getPrice());
        Spannable wordSpan = new SpannableString(price);
        wordSpan.setSpan(new ForegroundColorSpan(Color.BLUE), 8, price.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        priceTextView.setText(wordSpan);
        totalTextView = findViewById(R.id.tv_total_cost);
        totalTextView.setText(product.getTotalCostInRs());
        bottomLayout = findViewById(R.id.bottom_sheet);
        sheetBehavior = BottomSheetBehavior.from(bottomLayout);
        TextView addressTextView = findViewById(R.id.tv_product_address);
        addressTextView.setText(user.getAddress());
        Button cartButton = findViewById(R.id.btn_product_cart);
        if (!cartVisible)
            cartButton.setVisibility(View.GONE);

    }

    public void onIncrease(View view) {
        product.increaseQuantity();
        quantityTextView.setText(String.valueOf(product.getQuantity()));
        totalTextView.setText(product.getTotalCostInRs());
    }

    public void onDecrease(View view) {
        if (product.getQuantity() > 1) {
            product.decreaseQuantity();
            quantityTextView.setText(String.valueOf(product.getQuantity()));
            totalTextView.setText(product.getTotalCostInRs());
        }
    }

    public void onBuy(View view) {
        if (sheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
            sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        } else {
            sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        DateTimeUtils dateUtils = new DateTimeUtils();
        product.setDate(dateUtils.getCurrentDate());
        product.setTime(dateUtils.getCurrentTime(30));
        timeTextView.setText(product.getTime());
        dateText.setText(product.getDate());
    }

    public void addCart(View view) {
        if (user != null) {
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
            reference.child("Carts").child(user.getUid()).push().setValue(product).addOnCompleteListener(this);
        } else {
            Snackbar snackbar = Snackbar.make(bottomLayout, "Please Login Again", Snackbar.LENGTH_INDEFINITE);
            snackbar.setAction("Login", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(ProductActivity.this, LoginActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }
            });
            snackbar.show();
        }
    }

    @Override
    public void onComplete(@NonNull Task<Void> task) {
        if (task.isSuccessful()) {
            Toast.makeText(this, "Added to Cart", Toast.LENGTH_SHORT).show();
        }
    }


    public void orderNow(View view) {
        if (product.getTime() == null && product.getDate() == null) {
            Toast.makeText(getApplicationContext(), "please select time", Toast.LENGTH_LONG).show();
            return;
        }
        int checkedRadioButtonId = payments.getCheckedRadioButtonId();
        switch (checkedRadioButtonId) {
            case R.id.cash_on_delivery:
                Intent intent = new Intent(ProductActivity.this, CashOnDelivery.class);
                intent.putExtra("product", product);
                startActivity(intent);
                break;
            case R.id.online_payment:
                intent = new Intent(ProductActivity.this, OrderActivity.class);
                intent.putExtra("product", product);
                startActivity(intent);
                break;
        }
    }

    public void onEditDate(View view) {
        final Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+5:30"));
        DatePickerDialog dialog = new DatePickerDialog(view.getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                dateText.setText(String.format(Locale.ENGLISH, "%d/%d/%d", year, month, dayOfMonth));
                product.setDate(dateText.getText().toString());

            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        dialog.show();
    }

    public void onEditTime(View view) {
        final Calendar mCurrentTime = Calendar.getInstance();
        mCurrentTime.add(Calendar.MINUTE, 30);
        int hour = mCurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mCurrentTime.get(Calendar.MINUTE);
        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(view.getContext(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                timeTextView.setText(String.format(Locale.ENGLISH, "%d:%d", selectedHour, selectedMinute));
                product.setTime(timeTextView.getText().toString());
                timeTextView.setVisibility(View.VISIBLE);
            }
        }, hour, minute, true);//24 hrs true
        mTimePicker.setTitle("Select Time");
        mTimePicker.show();

    }


}
