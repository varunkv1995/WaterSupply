package com.pentateuch.watersupply.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.pentateuch.watersupply.R;
import com.pentateuch.watersupply.model.Product;

import java.util.List;

/**
 * Created by varu on 04-04-2018.
 */

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {
    private List<Product> products;
    private LayoutInflater inflater;

    public CartAdapter(Context context,List<Product> products){
        this.products = products;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public CartViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.list_item_cart,parent,false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CartViewHolder holder, int position) {
        Product product = products.get(position);
        holder.bind(product);
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public class CartViewHolder extends RecyclerView.ViewHolder{
        private ImageView productImageView;
        private TextView priceTextView;
        public CartViewHolder(View itemView) {
            super(itemView);
            priceTextView = itemView.findViewById(R.id.tv_cart_price);
            productImageView = itemView.findViewById(R.id.image_cart);
        }

        void bind(Product product){
            productImageView.setImageResource(product.getDrawable());
            priceTextView.setText(product.getCostInRs());
        }
    }
}
