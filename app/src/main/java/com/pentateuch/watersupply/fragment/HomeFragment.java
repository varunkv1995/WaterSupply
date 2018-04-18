package com.pentateuch.watersupply.fragment;


import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.pentateuch.watersupply.R;
import com.pentateuch.watersupply.activity.ProductActivity;
import com.pentateuch.watersupply.adapter.GridViewAdapter;
import com.pentateuch.watersupply.model.ProductItem;
import com.pentateuch.watersupply.utils.ProgressDialog;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment implements AdapterView.OnItemClickListener, OnCompleteListener<QuerySnapshot> {

    private View mView;
    private ArrayList<ProductItem> products;
    private GridView gridView;
    private GridViewAdapter gridViewAdapter;
    private ProgressDialog dialog;

    public HomeFragment() {
        // Required empty public constructor
        products = new ArrayList<>();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_home, container, false);
        gridView = mView.findViewById(R.id.gridview);
        gridView.setOnItemClickListener(this);
        return mView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        gridViewAdapter = new GridViewAdapter(mView.getContext(), products);
        gridView.setAdapter(gridViewAdapter);
        dialog = new ProgressDialog(mView.getContext(), ProgressDialog.FLAG_FULL);
        if (products.size() == 0)
            loadData();
    }

    private void loadData() {
        dialog.showProgressAt(mView);
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        firestore.collection("products").get().addOnCompleteListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        ProductItem product = (ProductItem) adapterView.getItemAtPosition(i);
        Intent intent = new Intent(mView.getContext(), ProductActivity.class);
        intent.putExtra("product", product);
        startActivity(intent);
    }

    @Override
    public void onComplete(@NonNull Task<QuerySnapshot> task) {
        if (task.isSuccessful()) {
            products.clear();
            for (DocumentSnapshot snapshot : task.getResult()
                    ) {
                ProductItem productItem = snapshot.toObject(ProductItem.class);
                products.add(productItem);
            }
            gridViewAdapter.notifyDataSetChanged();
            dialog.dismiss();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList("products", products);
    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if ((savedInstanceState != null)) {
            products = savedInstanceState.getParcelableArrayList("products");
        }

    }
}
