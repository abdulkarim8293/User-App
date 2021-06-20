package com.abdulkarim.userapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.abdulkarim.userapp.fragments.CartFragment;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ProductActivity extends AppCompatActivity {

    private FirebaseFirestore firebaseFirestore;

    private Product product;

    private TextView product_name,product_price;
    private ImageView product_image;
    private Button add_card;
    private SQLiteDatabaseHelper sqLiteDatabaseHelper;

    private TextView product_increase,product_decrease,product_quantity,total_price;
    private int quantity = 1;

    private int totalCartItem = 0;
    private TextView cartBadgeTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        sqLiteDatabaseHelper = new SQLiteDatabaseHelper(this);
        totalCartItem = sqLiteDatabaseHelper.getAllCartProduct().size();

        cartBadgeTextView = findViewById(R.id.cart_badge_text_view);
        cartBadgeTextView.setText(String.valueOf(totalCartItem));
        cartBadgeTextView.setVisibility(totalCartItem == 0 ? cartBadgeTextView.GONE : cartBadgeTextView.VISIBLE);


        firebaseFirestore = FirebaseFirestore.getInstance();

        DocumentReference docRef = firebaseFirestore.collection("products").document(getIntent().getStringExtra("id"));
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                product = documentSnapshot.toObject(Product.class);

                Picasso.get().load(product.getImage_url())
                        .placeholder(R.drawable.ic_launcher_background)
                        .error(R.drawable.ic_launcher_background)
                        .into(product_image);

                product_name.setText(""+product.getName());
                product_price.setText(""+product.getPrice());

                total_price.setText("Total "+product.getPrice());
            }
        });

        product_image = findViewById(R.id.product_image_view);
        product_name = findViewById(R.id.product_name_text_view);
        product_price = findViewById(R.id.product_price_text_view);
        add_card = findViewById(R.id.add_card_button);

        add_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Cart cart = new Cart();
                cart.setId(product.getId());
                cart.setName(product.getName());
                cart.setImage(product.getImage_url());
                cart.setPrice(product.getPrice());
                cart.setQuantity(quantity);

                if (sqLiteDatabaseHelper.isInCart(cart.getId())){
                    Snackbar snackbar = Snackbar.make(view,"Already added in your cart",Snackbar.LENGTH_SHORT).setAnchorView(view);
                    snackbar.show();

                }else {
                    sqLiteDatabaseHelper.addToCart(cart);
                    Snackbar snackbar = Snackbar.make(view,"Added to cart successfully!",Snackbar.LENGTH_SHORT).setAnchorView(view);
                    snackbar.show();

                    if (totalCartItem<1){
                        cartBadgeTextView.setText(String.valueOf(1));
                        cartBadgeTextView.setVisibility(View.VISIBLE);
                    }
                    cartBadgeTextView.setText(String.valueOf(totalCartItem+1));

                }

            }
        });
        findViewById(R.id.buy_now_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(ProductActivity.this,CheckoutActivity.class);
                intent.putExtra("from","product_activity");
                intent.putExtra("id",product.getId());
                intent.putExtra("name",product.getName());
                intent.putExtra("image",product.getImage_url());
                intent.putExtra("price",product.getPrice());
                intent.putExtra("quantity",quantity);
                startActivity(intent);

            }
        });

        product_increase = findViewById(R.id.product_increase);
        product_decrease = findViewById(R.id.product_decrease);
        product_quantity = findViewById(R.id.product_quantity);
        total_price = findViewById(R.id.product_total_price);


        product_quantity.setText(""+quantity);

        product_increase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                quantity++;
                product_quantity.setText(""+quantity);

                total_price.setText("Total "+Integer.parseInt(product.getPrice()) * quantity);
            }
        });
        product_decrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (quantity > 1){
                    quantity--;
                    product_quantity.setText(""+quantity);
                    total_price.setText("Total "+Integer.parseInt(product.getPrice()) * quantity);
                }

            }
        });




    }

}