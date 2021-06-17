package com.abdulkarim.userapp.my_address;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.abdulkarim.userapp.Product;
import com.abdulkarim.userapp.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class AddressListActivity extends AppCompatActivity implements OnAddressClicked{

    private RecyclerView recyclerView;
    private AddressAdapter addressAdapter;
    private List<MyAddress> myAddressList = new ArrayList<>();

    private FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_list);

        getAddressList();

        recyclerView = findViewById(R.id.address_recycler_view);
        addressAdapter = new AddressAdapter(myAddressList,this);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(addressAdapter);

        findViewById(R.id.add_address_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AddressListActivity.this, AddAddressActivity.class));
            }
        });
    }

    @Override
    public void onClicked(MyAddress myAddress) {

        if (myAddress != null){
            Intent intent = new Intent();
            intent.putExtra("my_address",myAddress);
            setResult(2,intent);
            finish();
        }

    }

    private void getAddressList(){

        firebaseFirestore = FirebaseFirestore.getInstance();

        firebaseFirestore.collection("addresses").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                if (error!=null){
                    Log.i("TAG",error.getMessage());
                }
                for (DocumentChange dc : value.getDocumentChanges()){

                    if (dc.getType() == DocumentChange.Type.ADDED){
                        myAddressList.add(dc.getDocument().toObject(MyAddress.class));
                    }

                    addressAdapter.notifyDataSetChanged();
                }
            }
        });

    }
}