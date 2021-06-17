package com.abdulkarim.userapp.my_address;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.abdulkarim.userapp.Address;
import com.abdulkarim.userapp.Cart;
import com.abdulkarim.userapp.Order;
import com.abdulkarim.userapp.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.WriteBatch;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddAddressActivity extends AppCompatActivity {

    private TextInputEditText nameEt, phoneEt, cityEt, zipEt, addressEt, noteEt;
    private RadioGroup radioGroup;
    private TextInputLayout additional_layout;

    private RadioButton radioButton;

    private FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_address);

        firebaseFirestore = FirebaseFirestore.getInstance();

        additional_layout = findViewById(R.id.til_additional_note);

        nameEt = findViewById(R.id.et_full_name);
        phoneEt = findViewById(R.id.et_phone_number);
        cityEt = findViewById(R.id.et_city_name);
        zipEt = findViewById(R.id.et_zip_code);
        addressEt = findViewById(R.id.et_address);
        noteEt = findViewById(R.id.et_additional_note);
        radioGroup = findViewById(R.id.rg_type);

        findViewById(R.id.btn_submit_address).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int selectedId = radioGroup.getCheckedRadioButtonId();
                radioButton = findViewById(selectedId);

                String name = nameEt.getText().toString().trim();
                String phone = phoneEt.getText().toString().trim();
                String city = cityEt.getText().toString().trim();
                String zip = zipEt.getText().toString().trim();
                String address = addressEt.getText().toString().trim();
                String type = radioButton.getText().toString();
                MyAddress myAddress = new MyAddress();
                if (type.equals("Other")) {
                    String note = noteEt.getText().toString().trim();
                    myAddress.setNote(note);
                }

                myAddress.setName(name);
                myAddress.setPhone(phone);
                myAddress.setCity(city);
                myAddress.setZip(zip);
                myAddress.setAddress(address);
                myAddress.setType(type);

                insertMyAddress(myAddress);


            }
        });


        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.rb_home:
                        additional_layout.setVisibility(View.GONE);
                        break;
                    case R.id.rb_office:
                        additional_layout.setVisibility(View.GONE);
                        break;
                    case R.id.rb_other:
                        additional_layout.setVisibility(View.VISIBLE);
                        break;

                }
            }
        });


    }

    private void insertMyAddress(MyAddress myAddress) {

        DocumentReference documentReference = firebaseFirestore.collection("addresses").document();


        Map<String, Object> my_address = new HashMap<>();

        my_address.put("id", documentReference.getId());
        my_address.put("user_id", FirebaseAuth.getInstance().getCurrentUser().getUid());
        my_address.put("name", myAddress.getName());
        my_address.put("phone", myAddress.getPhone());
        my_address.put("city", myAddress.getCity());
        my_address.put("zip", myAddress.getZip());
        my_address.put("address", myAddress.getAddress());
        my_address.put("type", myAddress.getType());
        if (myAddress.getNote() != null) {
            my_address.put("note", myAddress.getNote());
        }

        documentReference.set(my_address).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(AddAddressActivity.this, "Upload Success", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(AddAddressActivity.this,AddressListActivity.class));
            }
        });


    }

}
