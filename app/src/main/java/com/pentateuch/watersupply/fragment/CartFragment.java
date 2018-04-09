package com.pentateuch.watersupply.fragment;


import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
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
import com.pentateuch.watersupply.activity.ProductActivity;
import com.pentateuch.watersupply.adapter.CartAdapter;
import com.pentateuch.watersupply.model.Product;
import com.pentateuch.watersupply.model.User;
import com.pentateuch.watersupply.utils.CartTouchHelper;
import com.pentateuch.watersupply.utils.OnItemClickListener;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class CartFragment extends Fragment implements ValueEventListener, CartTouchHelper.RecyclerItemTouchListener, OnItemClickListener {

    private View rootView;
    private List<Product> products;
    private CartAdapter adapter;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private DatabaseReference carts;
    private boolean loaded;

    public CartFragment() {
        products = new ArrayList<>();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_cart, container, false);
        adapter = new CartAdapter(rootView.getContext(),products, this);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.recycle_cart);
        recyclerView.setAdapter(adapter);
        progressBar = view.findViewById(R.id.progress_cart);
        User user = App.getInstance().getUser();
        if(user!= null) {
            carts = FirebaseDatabase.getInstance().getReference().child("Carts").child(user.getUid());
            carts.addValueEventListener(this);
        }
        else{
            progressBar.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
        ItemTouchHelper.SimpleCallback callback = new CartTouchHelper(0,ItemTouchHelper.LEFT,this);
        new ItemTouchHelper(callback).attachToRecyclerView(recyclerView);
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        if(loaded)return;
        int start = products.size();
        for (DataSnapshot snapshot :
                dataSnapshot.getChildren()) {
            Product product = snapshot.getValue(Product.class);
            if(product != null) {
                product.setKey(snapshot.getKey());
                products.add(product);
            }
        }
        adapter.notifyItemRangeChanged(start,products.size());
        progressBar.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
        loaded = true;
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int pos) {
        if (viewHolder instanceof CartAdapter.CartViewHolder) {
            final int deletedPos = viewHolder.getAdapterPosition();
            final Product product = products.get(deletedPos);
            carts.child(product.getKey()).removeValue();
            adapter.removeItem(viewHolder.getAdapterPosition());

        }
    }

    @Override
    public void onClick(int position) {
        Product product = products.get(position);
        Intent intent = new Intent(getActivity(), ProductActivity.class);
        intent.putExtra("product",product);
        intent.putExtra("enableCart",false);
        startActivity(intent);
    }
}
