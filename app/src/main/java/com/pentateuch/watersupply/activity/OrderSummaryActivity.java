package com.pentateuch.watersupply.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import com.pentateuch.watersupply.R;

public class OrderSummaryActivity extends AppCompatActivity {
    private String transactionID;
    private String amount;
    private String status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_summary);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            transactionID = extras.getString("transactionID");
            amount = extras.getString("amount");
            status = extras.getString("status", "Pending");
        }
        TextView transactionTextView = findViewById(R.id.tv_transaction_id);
        TextView amountTextView = findViewById(R.id.tv_transaction_amount);
        TextView statusTextView = findViewById(R.id.tv_transaction_status);
        transactionTextView.setText(transactionID);
        amountTextView.setText(amount);
        statusTextView.setText(status);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_summary, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_summary_ok:
                goHome();
                break;
        }
        return true;
    }

    private void goHome() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}
