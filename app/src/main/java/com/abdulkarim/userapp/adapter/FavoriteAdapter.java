package com.abdulkarim.userapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.abdulkarim.userapp.Favorite;
import com.abdulkarim.userapp.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder> {

    private List<Favorite> favoriteList;

    public FavoriteAdapter(List<Favorite> favoriteList) {
        this.favoriteList = favoriteList;
    }

    @NonNull
    @Override
    public FavoriteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_favorite_product_recycler_view,parent,false);
        return new FavoriteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteViewHolder holder, int position) {

        Favorite favorite = favoriteList.get(position);

        holder.favorite_product_name.setText("Name "+favorite.getProduct_name());
        holder.favorite_product_price.setText("Price "+favorite.getProduct_price());

        Picasso.get().load(favorite.getProduct_image())
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_background)
                .into(holder.favorite_product_image);

    }

    @Override
    public int getItemCount() {
        return favoriteList.size();
    }

    public class FavoriteViewHolder extends RecyclerView.ViewHolder {

        private ImageView favorite_product_image;
        private TextView favorite_product_name,favorite_product_price;

        public FavoriteViewHolder(@NonNull View itemView) {
            super(itemView);

            favorite_product_image = itemView.findViewById(R.id.item_favorite_product_image_view);
            favorite_product_name = itemView.findViewById(R.id.item_favorite_product_name_text_view);
            favorite_product_price = itemView.findViewById(R.id.item_favorite_product_price_text_view);
        }
    }
}
