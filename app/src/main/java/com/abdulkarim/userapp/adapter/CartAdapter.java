package com.abdulkarim.userapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.abdulkarim.userapp.Cart;
import com.abdulkarim.userapp.R;
import com.abdulkarim.userapp.SQLiteDatabaseHelper;
import com.abdulkarim.userapp.fragments.CartFragment;
import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.google.firebase.firestore.WriteBatch;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {
    private CartFragment context;
    private List<Cart> cartList;


    public CartAdapter(CartFragment context, List<Cart> cartList) {
        this.context = context;
        this.cartList = cartList;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context.getContext()).inflate(R.layout.item_cart_product_recycler_view,parent,false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {

        Cart cart = cartList.get(position);

        holder.product_name_text.setText(""+cart.getName());
        holder.product_price_text.setText("৳ "+cart.getPrice());
        holder.sub_total_text.setText("Sub Total : "+(Integer.parseInt(cart.getPrice())*cart.getQuantity()));

        Picasso.get().load(cart.getImage())
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_background)
                .into(holder.product_image);

        holder.button_quantity.setNumber(""+cart.getQuantity());
        holder.button_quantity.setOnValueChangeListener(new ElegantNumberButton.OnValueChangeListener() {
            @Override
            public void onValueChange(ElegantNumberButton view, int oldValue, int newValue) {
                cart.setQuantity(newValue);
                new SQLiteDatabaseHelper(context.getContext()).updateCartProduct(cart);

                holder.sub_total_text.setText("Sub Total : "+(Integer.parseInt(cart.getPrice())*cart.getQuantity()));
                // update total price and quantity here...
                double total = 0.0;
                List<Cart> cartList = new SQLiteDatabaseHelper(context.getContext()).getAllCartProduct();

                for (Cart cart : cartList){
                    total += (Double.parseDouble(cart.getPrice())* cart.getQuantity());

                }
                context.total_amount.setText("Total Amount : "+total);
                context.total_item.setText("Total Item : "+cartList.size());
            }
        });

        holder.clear_product_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeItem(cart);

                new SQLiteDatabaseHelper(context.getContext()).removeProductFromCart(cart.getId());

                double total = 0.0;
                List<Cart> cartList = new SQLiteDatabaseHelper(context.getContext()).getAllCartProduct();

                for (Cart cart : cartList){
                    total += (Double.parseDouble(cart.getPrice())* cart.getQuantity());

                }
                context.total_amount.setText("Total Amount : "+total);
                context.total_item.setText("Total Item : "+cartList.size());
            }
        });


    }

    private void removeItem(Cart cartItem) {

        int currPosition = cartList.indexOf(cartItem);
        cartList.remove(currPosition);
        notifyItemRemoved(currPosition);

    }

    @Override
    public int getItemCount() {
        return cartList.size();
    }

    public class CartViewHolder extends RecyclerView.ViewHolder {

        private ImageView product_image;
        private TextView product_name_text,product_price_text,sub_total_text,clear_product_text;
        
        private ElegantNumberButton button_quantity;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);

            product_image = itemView.findViewById(R.id.item_cart_product_image_view);
            product_name_text = itemView.findViewById(R.id.item_cart_product_name_text_view);
            product_price_text = itemView.findViewById(R.id.item_cart_product_price_text_view);
            sub_total_text = itemView.findViewById(R.id.item_cart_sub_total_text_view);
            clear_product_text = itemView.findViewById(R.id.item_cart_product_clear_text_view);

            button_quantity = itemView.findViewById(R.id.btn_quantity);

        }
    }
}
