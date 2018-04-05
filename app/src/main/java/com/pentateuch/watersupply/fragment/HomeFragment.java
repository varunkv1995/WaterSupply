package com.pentateuch.watersupply.fragment;


import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.pentateuch.watersupply.R;
import com.pentateuch.watersupply.activity.ProductActivity;
import com.pentateuch.watersupply.adapter.GridViewAdapter;
import com.pentateuch.watersupply.model.Product;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment implements AdapterView.OnItemClickListener {

    private View mView;
    private List<Product> products;
    private GridView gridView;
    private GridViewAdapter gridViewAdapter;
    public HomeFragment() {
        // Required empty public constructor
        products = new ArrayList<>();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_home, container, false);
        gridView=mView.findViewById(R.id.gridview);
        gridView.setOnItemClickListener(this);
        products.clear();
        products.add(new Product("Product1", 0, "Large can",R.drawable.can_1,40.0f));
        products.add(new Product("Product2", 1, "Two Large can",R.drawable.can_2,80.0f,"2 Can"));
        products.add(new Product("Product3", 2, "Small can",R.drawable.can_3,15.0f,"Piece"));
        products.add(new Product("Product4", 3, "Multiple Small can",R.drawable.can_4,250.0f, "Carton"));
        return mView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        gridViewAdapter =new GridViewAdapter(mView.getContext(),products);
        gridView.setAdapter(gridViewAdapter);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Product product = (Product) adapterView.getItemAtPosition(i);
        Intent intent = new Intent(mView.getContext(), ProductActivity.class);
        intent.putExtra("product",product);
        startActivity(intent);
    }
}
