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
import com.pentateuch.watersupply.model.Product;
import com.pentateuch.watersupply.utils.OnItemClickListener;

import java.util.List;
import java.util.Locale;

/**
 * Created by varu on 04-04-2018.
 */

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {
    private List<Product> products;
    private LayoutInflater inflater;
    private OnItemClickListener listener;

    public CartAdapter(Context context, List<Product> products, OnItemClickListener listener) {
        this.products = products;
        inflater = LayoutInflater.from(context);
        this.listener = listener;
    }

    @Override
    public CartViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.list_item_cart, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CartViewHolder holder, int position) {
        Product product = products.get(position);
        holder.bind(product, position);
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public void removeItem(int position) {
        products.remove(position);
        notifyItemRemoved(position);
    }

    public class CartViewHolder extends RecyclerView.ViewHolder {
        private View backView, frontView;
        private ImageView productImageView;
        private TextView priceTextView, quantityTextView, dataTextView;

        CartViewHolder(View itemView) {
            super(itemView);
            priceTextView = itemView.findViewById(R.id.tv_cart_price);
            productImageView = itemView.findViewById(R.id.image_cart);
            quantityTextView = itemView.findViewById(R.id.tv_cart_quantity);
            dataTextView = itemView.findViewById(R.id.tv_cart_date);
            backView = itemView.findViewById(R.id.root_cart_back);
            frontView = itemView.findViewById(R.id.root_cart_front);
        }

        void bind(Product product, final int position) {
            productImageView.setImageResource(product.getDrawable());
            String price = String.format(Locale.ENGLISH, "Price :%s", product.getCostInRs());
            Spannable princeSpannable = new SpannableString(price);
            princeSpannable.setSpan(new ForegroundColorSpan(Color.BLUE), 7, price.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            priceTextView.setText(princeSpannable);
            String quantity = String.format(Locale.ENGLISH, "Quantity :%d", product.getQuantity());
            Spannable quantityWordSpan = new SpannableString(quantity);
            quantityWordSpan.setSpan(new ForegroundColorSpan(Color.BLUE), 10, quantity.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            quantityTextView.setText(quantityWordSpan);
            dataTextView.setText(product.getDate());
            frontView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onClick(position);
                }
            });
        }

        public View getBackView() {
            return backView;
        }

        public View getFrontView() {
            return frontView;
        }
    }
}
