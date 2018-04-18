package com.pentateuch.watersupply.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.pentateuch.watersupply.R;
import com.pentateuch.watersupply.model.ProductItem;
import com.tech.imageloader.core.ImageFetcher;
import com.tech.imageloader.view.ImageViewer;

import java.util.List;
import java.util.Locale;

/**
 * Created by varu on 04-04-2018.
 */

public class GridViewAdapter extends BaseAdapter {

    private List<ProductItem> products;
    private LayoutInflater layoutInflater;
    private ImageFetcher fetcher;

    public GridViewAdapter(Context context, List<ProductItem> products) {
        this.products = products;
        layoutInflater = LayoutInflater.from(context);
        fetcher = new ImageFetcher(context);
    }

    @Override
    public int getCount() {
        return products.size();
    }

    @Override
    public ProductItem getItem(int i) {
        return products.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = layoutInflater.inflate(R.layout.list_item_product, viewGroup, false);

        }
        ProductItem item = getItem(i);
        ImageView imageViewer = view.findViewById(R.id.image_product);
        TextView textView = view.findViewById(R.id.text_product);
        fetcher.from(item.getImageUrl()).into(imageViewer);
        textView.setText(String.format(Locale.ENGLISH, "Price\n%.2f/%s", item.getPrice(), item.getType()));
        return view;
    }
}
