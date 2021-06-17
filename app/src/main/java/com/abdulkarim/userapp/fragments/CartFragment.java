package com.abdulkarim.userapp.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.abdulkarim.userapp.Cart;
import com.abdulkarim.userapp.CheckoutActivity;
import com.abdulkarim.userapp.R;
import com.abdulkarim.userapp.SQLiteDatabaseHelper;
import com.abdulkarim.userapp.adapter.CartAdapter;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;


public class CartFragment extends Fragment {
    private RecyclerView recyclerView;
    private Button checkout_button;
    public TextView total_amount,total_item;
    private double total = 0.0;

    private SQLiteDatabaseHelper sqLiteDatabaseHelper;
    private CartAdapter cartAdapter;

    public CartFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        sqLiteDatabaseHelper = new SQLiteDatabaseHelper(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_cart, container, false);
        recyclerView = view.findViewById(R.id.cart_recycler_view);
        checkout_button = view.findViewById(R.id.check_out_button);
        total_amount = view.findViewById(R.id.cart_total_amount_text_view);
        total_item = view.findViewById(R.id.cart_total_item_text_view);


        List<Cart> cartList = new SQLiteDatabaseHelper(getContext()).getAllCartProduct();

        for (Cart cart : cartList){
            total += (Double.parseDouble(cart.getPrice())* cart.getQuantity());

        }
        total_amount.setText("Total Amount : "+total);
        total_item.setText("Total Item : "+cartList.size());

        cartAdapter = new CartAdapter(this,sqLiteDatabaseHelper.getAllCartProduct());

        checkout_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<Cart> carts = new SQLiteDatabaseHelper(getContext()).getAllCartProduct();
                if (carts.size()>0){
                    Intent intent = new Intent(getContext(), CheckoutActivity.class);
                    intent.putExtra("from","cart_fragment");
                    startActivity(intent);
                }else {
                    Snackbar snackbar = Snackbar.make(view,"There is no product to checkout",Snackbar.LENGTH_SHORT).setAnchorView(view);
                    snackbar.show();
                }
            }
        });

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(cartAdapter);

    }
}