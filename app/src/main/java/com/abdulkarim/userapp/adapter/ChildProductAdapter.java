package com.abdulkarim.userapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.abdulkarim.userapp.Cart;
import com.abdulkarim.userapp.OrderItem;
import com.abdulkarim.userapp.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ChildProductAdapter extends RecyclerView.Adapter<ChildProductAdapter.CheckoutViewHolder> {

    private List<OrderItem> cartList;

    public ChildProductAdapter(List<OrderItem> cartList) {
        this.cartList = cartList;
    }

    @NonNull
    @Override
    public CheckoutViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_child,parent,false);
        return new CheckoutViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CheckoutViewHolder holder, int position) {

        OrderItem cart = cartList.get(position);

        holder.product_name.setText("Name : "+cart.getName());
        holder.product_price_quantity.setText(""+cart.getPrice()+" X "+cart.getQuantity());
        holder.total_price.setText("Sub Total : "+Double.parseDouble(cart.getPrice())* cart.getQuantity());

        Picasso.get().load(cart.getImage())
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_background)
                .into(holder.product_image);

    }

    @Override
    public int getItemCount() {
        return cartList.size();
    }

    public class CheckoutViewHolder extends RecyclerView.ViewHolder {

        private ImageView product_image;
        private TextView product_name,product_price_quantity,total_price;

        public CheckoutViewHolder(@NonNull View itemView) {
            super(itemView);

            product_image = itemView.findViewById(R.id.item_child_item_image);
            product_name = itemView.findViewById(R.id.item_child_item_name);
            product_price_quantity = itemView.findViewById(R.id.item_child_item_price_quantity_text);

            total_price = itemView.findViewById(R.id.item_child_price_text);
        }
    }
}
