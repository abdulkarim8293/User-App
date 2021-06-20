package com.abdulkarim.userapp.fragments;

import android.content.Context;
import android.nfc.Tag;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.abdulkarim.userapp.Cart;
import com.abdulkarim.userapp.CustomProgress;
import com.abdulkarim.userapp.Order;
import com.abdulkarim.userapp.OrderItem;
import com.abdulkarim.userapp.Product;
import com.abdulkarim.userapp.R;
import com.abdulkarim.userapp.User;
import com.abdulkarim.userapp.adapter.OrderAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;


public class ProfileFragment extends Fragment {

    private FirebaseFirestore firebaseFirestore;
    private List<Order> orderList = new ArrayList<>();
    private RecyclerView recyclerView;
    private OrderAdapter orderAdapter;

    private TextView nameTv,emailTv;
    private ImageView profileIv;

    private List<OrderItem> cartList = new ArrayList<>();

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        firebaseFirestore = FirebaseFirestore.getInstance();
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        getUserInfo();

        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        getMyOrders(userId);



        Log.d("TAG", "onSuccess: " + orderList+"and size : "+orderList.size());

        Toast.makeText(getContext(), ""+ orderList+"and size : "+orderList.size(), Toast.LENGTH_SHORT).show();
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        nameTv = view.findViewById(R.id.profile_name);
        emailTv = view.findViewById(R.id.profile_email);
        profileIv = view.findViewById(R.id.profile_image);

        recyclerView = view.findViewById(R.id.profile_order_recycler_view);
        orderAdapter = new OrderAdapter(orderList,getContext(),cartList);



        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(orderAdapter);


    }

    private void getMyOrders(String user_id) {

        firebaseFirestore.collection("orders").whereEqualTo("user_id",user_id).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                    Order order = documentSnapshot.toObject(Order.class);
                    orderList.add(order);
                    Log.i("TAG","order details : "+order);
                    orderAdapter.notifyDataSetChanged();

                }
                getProductList();

                orderAdapter.notifyDataSetChanged();

                Log.i("TAG","success"+orderList.size());


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.i("TAG",""+e.getMessage());
            }
        });
    }

    private  void  getProductList(){

        for (Order order : orderList){

            firebaseFirestore.collection("orders").document(order.getProduct_id()).collection("order_details").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    List<OrderItem> carts = new ArrayList<>();
                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){

                        OrderItem order = documentSnapshot.toObject(OrderItem.class);
                        carts.add(order);
                        Log.i("TAG","Product name : "+order);
                        Log.i("TAG","cart list size : "+carts.size());
                        Log.i("TAG","order list size : "+orderList.size());

                    }
                    cartList.addAll(carts);
                    Log.i("TAG","cart list size after add carts : "+cartList.size());
                    orderAdapter.notifyDataSetChanged();

                }
            });

        }
        Log.i("TAG","Last cart list size : "+cartList.size());

    }


    private void getUserInfo(){

        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        firebaseFirestore.collection("users").document(userId).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                User user = documentSnapshot.toObject(User.class);
                Log.i("TAG",""+user);

                nameTv.setText(""+user.getName());
                emailTv.setText(""+user.getEmail());

                Picasso.get().load(user.getImage_url())
                        .placeholder(R.drawable.ic_launcher_background)
                        .error(R.drawable.ic_launcher_background)
                        .into(profileIv);



            }
        });

    }

}