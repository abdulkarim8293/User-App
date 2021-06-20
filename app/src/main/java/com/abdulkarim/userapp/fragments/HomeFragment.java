package com.abdulkarim.userapp.fragments;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.abdulkarim.userapp.Product;
import com.abdulkarim.userapp.ProductAdapter;
import com.abdulkarim.userapp.R;
import com.abdulkarim.userapp.slider.SliderAdapter;
import com.abdulkarim.userapp.slider.SliderItem;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;


public class HomeFragment extends Fragment {

    private RecyclerView recyclerView;
    private ArrayList<Product> productList = new ArrayList<>();
    private ProductAdapter productAdapter;
    private FirebaseFirestore firebaseFirestore;

    // slider
    private SliderView sliderView;
    private SliderAdapter sliderAdapter;


    public HomeFragment() {
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
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        // for slider
        sliderView = view.findViewById(R.id.imageSlider);
        sliderAdapter = new SliderAdapter(getContext());


        recyclerView = view.findViewById(R.id.recycler_view);
        getAllProduct();
        productAdapter = new ProductAdapter(getContext(), productList);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        recyclerView.setAdapter(productAdapter);


        // for slider
        sliderAdapter.addItem(new SliderItem(R.drawable.offer1));
        sliderAdapter.addItem(new SliderItem(R.drawable.offter2));
        sliderAdapter.addItem(new SliderItem(R.drawable.offer3));
        sliderAdapter.addItem(new SliderItem(R.drawable.offter2));
        sliderView.setSliderAdapter(sliderAdapter);

        sliderView.setIndicatorAnimation(IndicatorAnimationType.WORM); //set indicator animation by using IndicatorAnimationType. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
        sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        sliderView.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH);
        sliderView.setIndicatorSelectedColor(Color.WHITE);
        sliderView.setIndicatorUnselectedColor(Color.GRAY);
        sliderView.setScrollTimeInSec(2); //set scroll delay in seconds :
        sliderView.startAutoCycle();

    }

    private void getAllProduct() {
        firebaseFirestore.collection("products").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                if (error != null) {
                    Log.i("TAG", error.getMessage());
                }
                for (DocumentChange dc : value.getDocumentChanges()) {

                    if (dc.getType() == DocumentChange.Type.ADDED) {
                        productList.add(dc.getDocument().toObject(Product.class));
                    }

                    productAdapter.notifyDataSetChanged();
                }
            }
        });
    }
}