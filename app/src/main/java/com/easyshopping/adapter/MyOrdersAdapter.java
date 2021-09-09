package com.easyshopping.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.easyshopping.R;
import com.easyshopping.databinding.MyOrderItemBinding;
import com.easyshopping.databinding.MyOrderItemBinding;
import com.easyshopping.models.SuccessResGetAddresses;
import com.easyshopping.models.SuccessResGetOrders;
import com.easyshopping.util.ItemDetail;
import com.easyshopping.util.UpdateAndDeleteAddress;

import java.util.List;

/**
 * Created by Ravindra Birla on 10,June,2021
 */
public class MyOrdersAdapter extends RecyclerView.Adapter<MyOrdersAdapter.CategoryViewHolder> {

    private Context context;
    MyOrderItemBinding binding;

    private List<SuccessResGetOrders.Result> addressList;
    private ItemDetail itemDetail ;


    public MyOrdersAdapter(Context context, List<SuccessResGetOrders.Result> addressList,ItemDetail itemDetail)
    {
        this.context = context;
        this.addressList= addressList;
        this.itemDetail = itemDetail ;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding= MyOrderItemBinding.inflate(LayoutInflater.from(context));

        //View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_item, parent, false);
        return new CategoryViewHolder(binding);

    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {

        TextView tvAddress,tvCompanyName,tvOrderId,tvTotalAmount,tvDate,tvTime,tvStatus;
        TextView btnDetail;
        ImageView ivCompany;

        LinearLayout llParent = holder.itemView.findViewById(R.id.llAddress);

        tvCompanyName = holder.itemView.findViewById(R.id.tvTitle);
        tvOrderId = holder.itemView.findViewById(R.id.tvOrderID);
        tvTotalAmount = holder.itemView.findViewById(R.id.tvTotalAmount);
        tvDate = holder.itemView.findViewById(R.id.tvDate);
        tvTime = holder.itemView.findViewById(R.id.tvTime);
        tvStatus = holder.itemView.findViewById(R.id.tvStatus);
        ivCompany = holder.itemView.findViewById(R.id.ivProduct);
        tvAddress = holder.itemView.findViewById(R.id.tvAddress);

        btnDetail = holder.itemView.findViewById(R.id.tvOrderDetail);

        String date_time = addressList.get(position).getDateTime();

        String[] date_time_array =date_time.split("\\s+");

        String date = date_time_array[0];
        String time = date_time_array[1];

        tvCompanyName.setText(addressList.get(position).getCompanyName());
        tvOrderId.setText("Order ID: "+addressList.get(position).getId());
        tvTotalAmount.setText(addressList.get(position).getTotalAmount());
        tvDate.setText(date);
        tvTime.setText(time);
        tvStatus.setText("Status: "+addressList.get(position).getStatus());
        tvAddress.setText(addressList.get(position).getAddress());


        Glide.with(context)
                .load(addressList.get(position).getCompanyImage())
                .fitCenter()
                .into(ivCompany);


        btnDetail.setOnClickListener(v ->
                {

                    itemDetail.getClick(position);

                }
                );

    }

    @Override
    public int getItemCount() {
        return addressList.size();
    }

    public class CategoryViewHolder extends RecyclerView.ViewHolder
    {

        public CategoryViewHolder(MyOrderItemBinding binding1) {
            super(binding1.getRoot());
        }
    }

}
