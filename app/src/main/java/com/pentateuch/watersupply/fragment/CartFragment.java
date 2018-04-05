package com.pentateuch.watersupply.fragment;


import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pentateuch.watersupply.App;
import com.pentateuch.watersupply.R;
import com.pentateuch.watersupply.adapter.CartAdapter;
import com.pentateuch.watersupply.model.Product;
import com.pentateuch.watersupply.model.User;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class CartFragment extends Fragment implements ValueEventListener {

    private View rootView;
    private List<Product> products;
    private CartAdapter adapter;

    public CartFragment() {
        products = new ArrayList<>();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_cart, container, false);
        adapter = new CartAdapter(rootView.getContext(),products);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if(view instanceof RecyclerView){
            RecyclerView recyclerView = (RecyclerView) view;
            recyclerView.setAdapter(adapter);
        }
        User user = App.getInstance().getUser();
        FirebaseDatabase.getInstance().getReference().child("Carts").child(user.getUid()).addValueEventListener(this);
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        int start = products.size();
        for (DataSnapshot snapshot :
                dataSnapshot.getChildren()) {
            Product product = snapshot.getValue(Product.class);
            products.add(product);
        }
        adapter.notifyItemRangeChanged(start,products.size());
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }
}
