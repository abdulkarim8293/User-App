package com.abdulkarim.userapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.abdulkarim.userapp.my_address.MyAddress;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.WriteBatch;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PaymentActivity extends AppCompatActivity {

    private FirebaseFirestore firebaseFirestore;

    public TextView total_amount, total_item;
    private double total = 150.0;
    private List<Cart> cartList = new ArrayList<>();

    private MyAddress myAddress;
    private CustomProgress customProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        firebaseFirestore = FirebaseFirestore.getInstance();

        customProgress = new CustomProgress(this);

        total_amount = findViewById(R.id.cart_total_amount_text_view);
        total_item = findViewById(R.id.cart_total_item_text_view);


        myAddress = (MyAddress) getIntent().getSerializableExtra("address");
        Log.i("TAG","Payment activity address :"+myAddress);
        String product_id = getIntent().getStringExtra("product_id");
        String contain = getIntent().getStringExtra("contain");

        if (product_id == null) {
            // get all product from from database
            // and successfully payment delete all product from database;
            cartList = new SQLiteDatabaseHelper(this).getAllCartProduct();

            for (Cart cart : cartList) {
                total += (Double.parseDouble(cart.getPrice()) * cart.getQuantity());

            }
            total_amount.setText("Total Amount : " + total);
            total_item.setText("Total Item : " + cartList.size());

            Log.i("MES", "From cartList get all data from cart ");

        } else {

            if (contain != null) {
                Cart cart = new SQLiteDatabaseHelper(this).getSingleCartProduct(product_id);
                cartList.add(cart);

                total = (Double.parseDouble(cart.getPrice()) * cart.getQuantity());
                total_amount.setText("Total Amount : " + total);
                total_item.setText("Total Item : 1");
                Log.i("MES", "From contain cart product name : " + cart.getName() + "product image : " + cart.getImage());

            } else {
                String product_name = getIntent().getStringExtra("product_name");
                String product_image = getIntent().getStringExtra("product_image");
                String product_price = getIntent().getStringExtra("product_price");
                int product_quantity = getIntent().getIntExtra("product_quantity", 1);

                Cart cart = new Cart(product_id, product_name, product_image, product_price, product_quantity);
                cartList.add(cart);

                total = (Double.parseDouble(cart.getPrice()) * cart.getQuantity());
                total_amount.setText("Total Amount : " + total);
                total_item.setText("Total Item : 1");

                Log.i("MES", "From not contain cart product name : " + cart.getName() + "product image : " + cart.getImage());
            }

            // don't get single product from database ;
            // check is contain database then received all intent data add add to product list;
            // get single product by product_id from database and after successfully payment delete this product from database


        }

        findViewById(R.id.place_orders_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                customProgress.show();

                Order order = new Order();

                order.setPrice(String.valueOf(total));
                order.setUser_id(FirebaseAuth.getInstance().getCurrentUser().getUid());
                order.setOrder_status("pending");
                order.setDelivery_charge("150");


                insertOrder(order, cartList,myAddress);
            }
        });


    }


    private void insertOrder(Order order, List<Cart> cartList,MyAddress myAddress) {

        DocumentReference documentReference = firebaseFirestore.collection("orders").document(String.valueOf(Timestamp.now().getNanoseconds()));

        WriteBatch batch = firebaseFirestore.batch();

        Map<String, Object> orderMap = new HashMap<>();
        //orderMap.put("product_id", documentReference.getId());
        orderMap.put("product_id", documentReference.getId());
        orderMap.put("user_id", order.getUser_id());
        orderMap.put("price", order.getPrice());
        orderMap.put("delivery_charge", order.getDelivery_charge());
        orderMap.put("order_status", order.getOrder_status());


        batch.set(documentReference, orderMap);

        // for delivery address
        DocumentReference deliveryAddressReference = firebaseFirestore.collection("orders")
                .document(documentReference.getId())
                .collection("delivery_address")
                .document();
        Map<String, Object> address = new HashMap<>();
        address.put("id", myAddress.getId());
        address.put("name", myAddress.getName());
        address.put("phone", myAddress.getPhone());
        address.put("city", myAddress.getCity());
        address.put("zip", myAddress.getZip());
        address.put("address", myAddress.getAddress());
        address.put("type", myAddress.getType());
        if (myAddress.getNote() != null){
            address.put("note", myAddress.getNote());
        }

        batch.set(deliveryAddressReference, address);


        for (Cart cart : cartList) {
            // do something with object
            Map<String, Object> cartMap = new HashMap<>();

            cartMap.put("order_id", documentReference.getId());
            cartMap.put("id", cart.getId());
            cartMap.put("name", cart.getName());
            cartMap.put("price", cart.getPrice());
            cartMap.put("quantity", cart.getQuantity());
            cartMap.put("image", cart.getImage());

            DocumentReference orderDetailsDocument = firebaseFirestore.collection("orders")
                    .document(documentReference.getId())
                    .collection("order_details")
                    .document();


            batch.set(orderDetailsDocument, cartMap);

        }



        batch.commit().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {

                customProgress.cancel();
                startActivity(new Intent(PaymentActivity.this,ConfirmationActivity.class));
                finish();
            }
        });

    }
}