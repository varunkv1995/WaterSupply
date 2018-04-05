package com.pentateuch.watersupply.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.pentateuch.watersupply.R;
import com.pentateuch.watersupply.model.Product;

import java.util.List;

/**
 * Created by varu on 04-04-2018.
 */

public class GridViewAdapter extends BaseAdapter {

    private List<Product> products;
    private LayoutInflater layoutInflater;

    public GridViewAdapter(Context context,List<Product> products) {
        this.products = products;
        layoutInflater=LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return products.size();
    }

    @Override
    public Product getItem(int i) {
        return products.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if(view==null){
            view =layoutInflater.inflate(R.layout.list_item_product,viewGroup,false);

        }
        Product item = getItem(i);
        ImageView imageView = view.findViewById(R.id.image_product);
        TextView textView = view.findViewById(R.id.text_product);
        imageView.setImageResource(item.getDrawable());
        textView.setText(String.format("Price\n %.2f/%s",item.getPrice(),item.getType()));
        return view;
    }
}
