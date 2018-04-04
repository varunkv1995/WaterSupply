package com.pentateuch.watersupply.fragment;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pentateuch.watersupply.R;
import com.pentateuch.watersupply.model.Product;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    private View mView;
    private List<Product> products;
    public HomeFragment() {
        // Required empty public constructor
        products = new ArrayList<>();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_home, container, false);
        products.add(new Product("Product1","Large can",R.drawable.can_1,40.0f));
        products.add(new Product("Product2","Two Large can",R.drawable.can_2,80.0f,"2 Can"));
        products.add(new Product("Product3","Small can",R.drawable.can_3,15.0f));
        products.add(new Product("Product4","Multiple Small can",R.drawable.can_4,250.0f, "Carton"));

        return mView;
    }

}
