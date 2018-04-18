package com.pentateuch.watersupply.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.pentateuch.watersupply.R;
import com.pentateuch.watersupply.fragment.MyOrderFragment;
import com.pentateuch.watersupply.model.Product;
import com.pentateuch.watersupply.model.Status;
import com.tech.imageloader.core.ImageFetcher;

import java.util.List;
import java.util.Locale;

/**
 * Created by varu on 08-04-2018.
 */
public class MyOrderAdapter extends RecyclerView.Adapter<MyOrderAdapter.MyOrderViewHolder> {
    private List<Product> products;
    private LayoutInflater inflater;
    private ImageFetcher fetcher;

    public MyOrderAdapter(Context context, List<Product> products, MyOrderFragment myOrederFragment) {
        this.products = products;
        inflater = LayoutInflater.from(context);
        fetcher = new ImageFetcher(context);
    }

    @Override
    public MyOrderViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.list_item_myorder, parent, false);
        return new MyOrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyOrderAdapter.MyOrderViewHolder holder, int position) {
        Product product = products.get(position);
        holder.bind(product);
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public class MyOrderViewHolder extends RecyclerView.ViewHolder {
        private ImageView productImageView;
        private TextView priceTextView, quantityTextView, dataTextView, TimeTextViwe, statusTextViwe;


        MyOrderViewHolder(View itemView) {
            super(itemView);
            priceTextView = itemView.findViewById(R.id.tv_myorder_price);
            productImageView = itemView.findViewById(R.id.image_myorder);
            quantityTextView = itemView.findViewById(R.id.tv_myorder_quantity);
            dataTextView = itemView.findViewById(R.id.tv_myorder_date);
            TimeTextViwe = itemView.findViewById(R.id.tv_myorder_time);
            statusTextViwe = itemView.findViewById(R.id.tv_myorder_status);
        }

        public void bind(Product product) {
            fetcher.from(product.getImageUrl()).into(productImageView);
            String price = String.format(Locale.ENGLISH, "Price :%s", product.getCostInRs());
            Spannable princeSpannable = new SpannableString(price);
            princeSpannable.setSpan(new ForegroundColorSpan(Color.BLUE), 7, price.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            priceTextView.setText(princeSpannable);

            String quantity = String.format(Locale.ENGLISH, "Quantity :%d", product.getQuantity());
            Spannable quantityWordSpan = new SpannableString(quantity);
            quantityWordSpan.setSpan(new ForegroundColorSpan(Color.BLUE), 10, quantity.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            quantityTextView.setText(quantityWordSpan);

            String Date = String.format(Locale.ENGLISH, "Date :%s", product.getDate());
            Spannable DateWordSpan = new SpannableString(Date);
            DateWordSpan.setSpan(new ForegroundColorSpan(Color.BLUE), 6, Date.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            dataTextView.setText(DateWordSpan);

            String Time = String.format(Locale.ENGLISH, "Time :%s", product.getTime());
            Spannable TimeWordSpan = new SpannableString(Time);
            TimeWordSpan.setSpan(new ForegroundColorSpan(Color.BLUE), 6, Time.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            TimeTextViwe.setText(TimeWordSpan);
            if(product.getStatus() == null)
                product.setStatus(new Status("Pending"));
            String Status = String.format(Locale.ENGLISH, "Status:%s", product.getStatus());
            Spannable StatusWordSpan = new SpannableString(Status);
            StatusWordSpan.setSpan(new ForegroundColorSpan(Color.BLUE), 6, Status.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            statusTextViwe.setText(StatusWordSpan);
        }
    }
}
