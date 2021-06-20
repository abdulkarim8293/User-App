package com.abdulkarim.userapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.abdulkarim.userapp.adapter.CheckoutAdapter;
import com.abdulkarim.userapp.my_address.AddressListActivity;
import com.abdulkarim.userapp.my_address.MyAddress;
import com.google.android.gms.common.api.Batch;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.WriteBatch;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CheckoutActivity extends AppCompatActivity {

    private TextView sub_total, total_payable;
    private double total = 0.0;


    private List<Cart> cartList = new ArrayList<>();

    private CheckoutAdapter checkoutAdapter;
    private RecyclerView recyclerView;
    private String product_id;

    private Cart cart;
    private SQLiteDatabaseHelper sqLiteDatabaseHelper;

    // For delivery address;
    private Button select_address;
    private TextView addressText,editAddress;
    private MyAddress deliveryAddress;
    private LinearLayout ll_address;

    private Button paymentBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);


        sqLiteDatabaseHelper = new SQLiteDatabaseHelper(CheckoutActivity.this);

        String from = getIntent().getStringExtra("from");
        product_id = getIntent().getStringExtra("id");

        if (from.equals("product_activity")) {

            String name = getIntent().getStringExtra("name");
            String image = getIntent().getStringExtra("image");
            String price = getIntent().getStringExtra("price");
            int quantity = getIntent().getIntExtra("quantity", 1);

            cart = new Cart(product_id, name, image, price, quantity);
            cartList.add(cart);


            Log.i("TAG", "product id : " + product_id);
            Log.i("TAG", "product id : " + quantity);

        } else {

            cartList = new SQLiteDatabaseHelper(this).getAllCartProduct();
            Log.i("TAG", "product List size : " + cartList.size());
            Log.i("TAG", "where : " + from);
        }


        recyclerView = findViewById(R.id.checkoutRecyclerView);
        checkoutAdapter = new CheckoutAdapter(cartList);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(checkoutAdapter);

        sub_total = findViewById(R.id.checkout_sub_total_text_view);
        total_payable = findViewById(R.id.checkout_total_payable_text_view);

        for (Cart cart : cartList) {
            total += (Double.parseDouble(cart.getPrice()) * cart.getQuantity());

        }
        sub_total.setText("৳ " + total);
        total_payable.setText("৳ " + (total + 150));


        paymentBtn = findViewById(R.id.place_order_button);
        ll_address = findViewById(R.id.llAddress);
        addressText = findViewById(R.id.addressTextView);
        editAddress = findViewById(R.id.editText);
        paymentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(CheckoutActivity.this, PaymentActivity.class);

                if (product_id == null) {
                    // go to payment activity with out product id

                } else {
                    if (sqLiteDatabaseHelper.isInCart(product_id)) {
                        // product is contain in the cart already
                        // go to payment activity with product id
                        intent.putExtra("product_id", product_id);
                        intent.putExtra("contain", "contain");
                        sqLiteDatabaseHelper.updateCartProduct(cart);
                        Log.i("TAG", "update product");

                    } else {
                        // product is not contain in the cart insert it and go to payment activity with product id
                        //sqLiteDatabaseHelper.addToCart(cart);
                        intent.putExtra("product_id", product_id);
                        intent.putExtra("product_name", cart.getName());
                        intent.putExtra("product_image", cart.getImage());
                        intent.putExtra("product_price", cart.getPrice());
                        intent.putExtra("product_quantity", cart.getQuantity());
                        // add all product value to payment activity;
                      
                    }
                }

                if (deliveryAddress == null){
                    Snackbar snackbar = Snackbar.make(view,"Please select your pick address",Snackbar.LENGTH_SHORT).setAnchorView(view);
                    snackbar.show();
                }else {
                    intent.putExtra("address",deliveryAddress);
                    startActivity(intent);
                }


            }
        });


        select_address = findViewById(R.id.select_address_button);
        select_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CheckoutActivity.this, AddressListActivity.class);
                startActivityForResult(intent, 0);

            }
        });

        editAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CheckoutActivity.this, AddressListActivity.class);
                startActivityForResult(intent, 0);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 2) {
            MyAddress myAddress = (MyAddress) data.getSerializableExtra("my_address");

            if (requestCode == 0) {
                deliveryAddress = myAddress;
                /*address_type.setText("" + myAddress.getType());
                name.setText("" + myAddress.getName());
                phone.setText("" + myAddress.getPhone());
                address.setText("" + myAddress.getAddress());
                city.setText("" + myAddress.getCity() + " - " + myAddress.getZip());
                select_address.setText("Edit");*/
                //address_type.setText(""+myAddress.getType());
                addressText.setText(""+myAddress.getType()+"\n"+myAddress.getName()+"\n"+myAddress.getPhone()+"\n"+myAddress.getAddress()+"\n"+myAddress.getCity()+" - "+myAddress.getZip());
                ll_address.setVisibility(View.VISIBLE);
                select_address.setVisibility(View.GONE);
                paymentBtn.setVisibility(View.VISIBLE);
                editAddress.setVisibility(View.VISIBLE);
            } else if (requestCode == 1) {
                //textView.setText("Name is : "+myAddress.getName()+"\nPhone is : "+myAddress.getPhone());
            }

            Log.i("TAG", "Address is : " + myAddress);

        } else {
            Log.i("TAG", "No Address Selected");
        }

    }

}