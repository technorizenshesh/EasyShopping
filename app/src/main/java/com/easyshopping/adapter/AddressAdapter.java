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
import androidx.recyclerview.widget.RecyclerView;

import com.easyshopping.R;
import com.easyshopping.databinding.AddressItemBinding;
import com.easyshopping.databinding.AddressItemBinding;
import com.easyshopping.models.SuccessResGetAddresses;
import com.easyshopping.util.UpdateAndDeleteAddress;

import java.util.List;

/**
 * Created by Ravindra Birla on 10,June,2021
 */
public class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.CategoryViewHolder> {

    private Context context;
    AddressItemBinding binding;

    private List<SuccessResGetAddresses.Result> addressList;
    private UpdateAndDeleteAddress updateAndDeleteAddress;

    private boolean fromWhere;

    private int selectedPosition = -1;

    public AddressAdapter(Context context,List<SuccessResGetAddresses.Result> addressList,UpdateAndDeleteAddress updateAndDeleteAddress,boolean fromWhere)
    {
        this.context = context;
        this.addressList= addressList;
        this.updateAndDeleteAddress = updateAndDeleteAddress;
        this.fromWhere = fromWhere;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding= AddressItemBinding.inflate(LayoutInflater.from(context));

        //View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_item, parent, false);
        return new CategoryViewHolder(binding);

    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {

        TextView tvAddress,tvName,tvPhone;
        AppCompatButton btnEdit;
        ImageView ivDelete;

        LinearLayout llParent = holder.itemView.findViewById(R.id.llAddress);

        tvName = holder.itemView.findViewById(R.id.tvName);
        tvAddress = holder.itemView.findViewById(R.id.tvAddress);
        tvPhone = holder.itemView.findViewById(R.id.tvPhone);

        btnEdit = holder.itemView.findViewById(R.id.btnEdit);
        ivDelete = holder.itemView.findViewById(R.id.ivDelete);

        tvName.setText(addressList.get(position).getName());
        tvAddress.setText(addressList.get(position).getAddress());
        tvPhone.setText(addressList.get(position).getCountryCode()+""+addressList.get(position).getPhone());


        if(position == selectedPosition)
        {
            llParent.setBackgroundResource(R.drawable.dark_blue_border);
        }
        else
        {
            llParent.setBackgroundResource(R.drawable.light_gray_border);
        }

        if(fromWhere)
        {
            llParent.setOnClickListener(v ->
                    {
                        selectedPosition = position;
                        updateAndDeleteAddress.isSelected(true,position);
                        notifyDataSetChanged();
                    }
            );

        }



        btnEdit.setOnClickListener(v ->
                {

                    new AlertDialog.Builder(context)
                            .setTitle("Edit Address")
                            .setMessage("Are you sure you want to edit address?")

                            // Specifying a listener allows you to take an action before dismissing the dialog.
                            // The dialog is automatically dismissed when a dialog button is clicked.

                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // Continue with delete operation

                                    updateAndDeleteAddress.updateCart(v,addressList.get(position).getName(),addressList.get(position).getAddress(),addressList.get(position).getId(),addressList.get(position).getPhone(),addressList.get(position).getCountryCode());

                                }
                            })
                            // A null listener allows the button to dismiss the dialog and take no further action.
                            .setNegativeButton(android.R.string.no, null)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();

                }
                );

        ivDelete.setOnClickListener(v ->
                {

                    new AlertDialog.Builder(context)
                            .setTitle("Delete Address")
                            .setMessage("Are you sure you want to delete address?")

                            // Specifying a listener allows you to take an action before dismissing the dialog.
                            // The dialog is automatically dismissed when a dialog button is clicked.

                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // Continue with delete operation

                                    updateAndDeleteAddress.deleteCart(addressList.get(position).getId());

                                }
                            })
                            // A null listener allows the button to dismiss the dialog and take no further action.
                            .setNegativeButton(android.R.string.no, null)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();

                }
                );


    }

    @Override
    public int getItemCount() {
        return addressList.size();
    }

    public class CategoryViewHolder extends RecyclerView.ViewHolder
    {

        public CategoryViewHolder(AddressItemBinding binding1) {
            super(binding1.getRoot());
        }
    }

}
