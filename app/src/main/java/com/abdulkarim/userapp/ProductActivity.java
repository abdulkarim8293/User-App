package com.abdulkarim.userapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        sqLiteDatabaseHelper = new SQLiteDatabaseHelper(this);

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
                cart.setQuantity(1);

                if (sqLiteDatabaseHelper.isInCart(cart.getId())){
                    Snackbar snackbar = Snackbar.make(view,"Already added in your cart",Snackbar.LENGTH_SHORT).setAnchorView(view);
                    snackbar.show();

                }else {
                    sqLiteDatabaseHelper.addToCart(cart);
                    Snackbar snackbar = Snackbar.make(view,"Added to cart successfully!",Snackbar.LENGTH_SHORT).setAnchorView(view);
                    snackbar.show();
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
                intent.putExtra("quantity",2);
                startActivity(intent);

            }
        });


    }
}