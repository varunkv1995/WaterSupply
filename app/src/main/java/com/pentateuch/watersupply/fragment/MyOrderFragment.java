package com.pentateuch.watersupply.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pentateuch.watersupply.App;
import com.pentateuch.watersupply.R;
import com.pentateuch.watersupply.adapter.MyOrderAdapter;
import com.pentateuch.watersupply.model.Product;
import com.pentateuch.watersupply.model.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by varu on 08-04-2018.
 */

public class MyOrderFragment extends Fragment implements ValueEventListener {


    private View rootView;
    private List<Product> products;
    private MyOrderAdapter adapter;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private DatabaseReference myorder;
    private boolean loaded;

    public MyOrderFragment() {
        products = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_myorder, container, false);
        adapter = new MyOrderAdapter(rootView.getContext(), products, this);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.recycle_myorder);
        recyclerView.setAdapter(adapter);
        progressBar = view.findViewById(R.id.progress_myorder);
        User user = App.getInstance().getUser();
        myorder = FirebaseDatabase.getInstance().getReference().child("MyOrder").child(user.getUid());
        myorder.addValueEventListener(this);
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        if (loaded) return;
        int start = products.size();
        for (DataSnapshot snapshot :
                dataSnapshot.getChildren()) {
            Product product = snapshot.getValue(Product.class);
            if (product != null) {
                product.setKey(snapshot.getKey());
                products.add(product);
            }
        }
        adapter.notifyItemRangeChanged(start, products.size());
        progressBar.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
        loaded = true;
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }

}
