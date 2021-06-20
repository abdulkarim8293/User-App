package com.abdulkarim.userapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.squareup.picasso.Picasso;
import com.varunest.sparkbutton.SparkButton;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.MyViewHolder> {

    private Context context;
    private List<Product> productList;

    // favorite filed
    private boolean isFavorite = false;

    public ProductAdapter(Context context, List<Product> productList) {
        this.context = context;
        this.productList = productList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_product_recycler_view,parent,false);
        return new MyViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Product product = productList.get(position);
        holder.product_name_text.setText(""+product.getName());
        holder.product_price_text.setText("৳ "+product.getPrice());

        double old_price = (Double.valueOf(product.getPrice())+Double.valueOf(product.getPrice())*0.25);
        int price = (int) old_price;
        holder.old_price.setText("৳ "+price);


        Picasso.get().load(product.getImage_url())
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_background)
                .into(holder.product_image);

        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context,ProductActivity.class);
                intent.putExtra("id",product.getId());
                context.startActivity(intent);
            }
        });


        // Favourite status check and set status;
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        String userId = firebaseUser.getUid();
        String productId = product.getId();
        holder.getFavouriteStatus(productId,userId);

        holder.sparkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isFavorite = true;
                FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
                DocumentReference docRef = firebaseFirestore.collection("favorites").document(userId).collection("favorite_list").document(productId);
                docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (isFavorite){
                            if (documentSnapshot.exists()){
                                String users = documentSnapshot.getString("user_id");
                                if (users.equals(userId)){
                                    docRef.delete();
                                    holder.sparkButton.setChecked(false);
                                    holder.sparkButton.setActiveImage(R.drawable.ic_favorite_border_24);
                                    isFavorite = false;
                                }
                            }else {
                                Map<String,Object> favoriteMap = new HashMap<>();
                                favoriteMap.put("user_id",userId);
                                favoriteMap.put("product_id",productId);
                                favoriteMap.put("product_name",product.getName());
                                favoriteMap.put("product_price",product.getPrice());
                                favoriteMap.put("product_image",product.getImage_url());
                                docRef.set(favoriteMap);
                                holder.sparkButton.setChecked(true);
                                holder.sparkButton.setActiveImage(R.drawable.ic_favorite_fill);
                                holder.sparkButton.playAnimation();
                                isFavorite = false;
                            }
                        }
                    }
                });

            }
        });

/*        holder.favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isFavorite = true;
                FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
                DocumentReference docRef = firebaseFirestore.collection("favorites").document(userId).collection("favorite_list").document(productId);
                docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                       if (isFavorite){
                           if (documentSnapshot.exists()){
                               String users = documentSnapshot.getString("user_id");
                               if (users.equals(userId)){
                                   docRef.delete();
                                   holder.favorite.setImageResource(R.drawable.ic_favorite_off);
                                   isFavorite = false;
                               }
                           }else {
                               Map<String,Object> favoriteMap = new HashMap<>();
                               favoriteMap.put("user_id",userId);
                               favoriteMap.put("product_id",productId);
                               favoriteMap.put("product_name",product.getName());
                               favoriteMap.put("product_price",product.getPrice());
                               favoriteMap.put("product_image",product.getImage_url());
                               docRef.set(favoriteMap);
                               holder.favorite.setImageResource(R.drawable.ic_favorite_fill);
                               isFavorite = false;
                           }
                       }
                    }
                });
            }
        });*/




    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView product_name_text,product_price_text;
        private ImageView product_image;
        private LinearLayout linearLayout;
        private TextView old_price;

        private SparkButton sparkButton;



        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            product_name_text = itemView.findViewById(R.id.item_product_name_text_view);
            product_price_text = itemView.findViewById(R.id.item_product_price_text_view);
            product_image = itemView.findViewById(R.id.item_image_view);
            old_price = itemView.findViewById(R.id.item_product_old_price);
            linearLayout = itemView.findViewById(R.id.item_ll);

            sparkButton = itemView.findViewById(R.id.spark_button);

            old_price.setPaintFlags(old_price.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG);

        }

        public void getFavouriteStatus(String productId, String userId) {

            FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
            DocumentReference docRef = firebaseFirestore.collection("favorites").document(userId).collection("favorite_list").document(productId);
            docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    if (documentSnapshot.exists()){
                        String users = documentSnapshot.getString("user_id");
                        if (users.equals(userId)){
                            Log.i("TAG","product is exist : "+productId);
                            //favorite.setImageResource(R.drawable.ic_favorite_fill);
                            sparkButton.setActiveImage(R.drawable.ic_favorite_fill);
                            sparkButton.setChecked(true);
                        }else {
                            //favorite.setImageResource(R.drawable.ic_favorite_off);
                            sparkButton.setChecked(false);
                            sparkButton.setActiveImage(R.drawable.ic_favorite_border_24);
                            Log.i("TAG","product is not exist in favorite collections : "+productId);
                        }

                    }

                }
            });

        }

    }
}
