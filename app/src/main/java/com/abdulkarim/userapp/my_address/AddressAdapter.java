package com.abdulkarim.userapp.my_address;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.abdulkarim.userapp.R;

import java.util.List;

public class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.AddressViewHolder> {

    private List<MyAddress> myAddressList;
    private OnAddressClicked onAddressClicked;


    public AddressAdapter(List<MyAddress> myAddressList,OnAddressClicked onAddressClicked ) {
        this.myAddressList = myAddressList;
        this.onAddressClicked = onAddressClicked;

    }

    @NonNull
    @Override
    public AddressViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_my_address, parent, false);
        return new AddressViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AddressViewHolder holder, int position) {

        MyAddress myAddress = myAddressList.get(position);

        holder.name.setText("Name : "+myAddress.getName());
        holder.phone.setText("Phone : "+myAddress.getPhone());
        holder.address.setText("Address : "+myAddress.getAddress());
        holder.type.setText("Type : "+myAddress.getType());
        holder.city.setText("City Name : "+myAddress.getCity()+"-"+myAddress.getZip());

        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onAddressClicked.onClicked(myAddress);
            }
        });

    }

    @Override
    public int getItemCount() {
        return myAddressList.size();
    }

    public class AddressViewHolder extends RecyclerView.ViewHolder {

        private TextView name,phone,city,address,type;
        private LinearLayout linearLayout;

        public AddressViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.item_address_name_text);
            phone = itemView.findViewById(R.id.item_address_phone_text);
            city = itemView.findViewById(R.id.item_address_city_text);
            type = itemView.findViewById(R.id.item_address_type_text);
            address = itemView.findViewById(R.id.item_address_address_text);
            linearLayout = itemView.findViewById(R.id.item_address_ll_address);

        }

    }

}
