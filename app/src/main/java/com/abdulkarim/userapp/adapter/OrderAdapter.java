package com.abdulkarim.userapp.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.abdulkarim.userapp.Cart;
import com.abdulkarim.userapp.Order;
import com.abdulkarim.userapp.OrderItem;
import com.abdulkarim.userapp.Product;
import com.abdulkarim.userapp.ProductAdapter;
import com.abdulkarim.userapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Handler;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {

    private List<Order> orderList;
    private Context context;

    private ChildProductAdapter checkoutAdapter;

    FirebaseFirestore firebaseFirestore;
    private List<OrderItem> cartList;



    public OrderAdapter(List<Order> orderList,Context context,List<OrderItem> cartList) {
        this.orderList = orderList;
        this.context = context;

        this.cartList = cartList;

        firebaseFirestore = FirebaseFirestore.getInstance();
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order_recycler_view,parent,false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {

        Order order = orderList.get(position);


        //List<Cart> carts = getProductList(order.getProduct_id());
        List<OrderItem> carts = getProductLists(order.getProduct_id());

        holder.order_id.setText("Order Id :"+order.getProduct_id());
        holder.order_price.setText(order.getPrice());
        holder.order_status.setText("Order Status : "+order.getOrder_status());


        checkoutAdapter = new ChildProductAdapter(getProductLists(order.getProduct_id()));
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        holder.order_details.setLayoutManager(linearLayoutManager);
        holder.order_details.setAdapter(checkoutAdapter);
        checkoutAdapter.notifyDataSetChanged();


    }

    private List<OrderItem> getProductLists(String id) {
        List<OrderItem> carts = new ArrayList<>();

        for (OrderItem cart:cartList){
            if (cart.getOrder_id().equals(id)){
                carts.add(cart);
                Log.i("TAG","cart item equals "+carts);
            }

        }

        Log.i("TAG","get product list "+carts.size());

        return carts;
    }

    private List<Cart> getProductList(String order_id){

        List<Cart> cartList = new ArrayList<>();


        firebaseFirestore.collection("orders").document(order_id).collection("order_details").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                    Cart order = documentSnapshot.toObject(Cart.class);
                    cartList.add(order);
                    //Log.i("TAG","order details : "+order);
                    checkoutAdapter.notifyDataSetChanged();

                }
                checkoutAdapter.notifyDataSetChanged();

            }
        });

        return cartList;
    }


    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public class OrderViewHolder extends RecyclerView.ViewHolder {

        private TextView order_id,order_price,order_status;
        private RecyclerView order_details;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            order_id = itemView.findViewById(R.id.item_order_id_text_view);
            order_price = itemView.findViewById(R.id.item_order_price_text_view);
            order_status = itemView.findViewById(R.id.item_order_status_text_view);
            order_details = itemView.findViewById(R.id.item_order_details_recycler_view);
        }
    }
}
