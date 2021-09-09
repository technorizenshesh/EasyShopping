package com.easyshopping.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.easyshopping.R;
import com.easyshopping.databinding.CartItemBinding;
import com.easyshopping.databinding.CartItemBinding;
import com.easyshopping.models.SuccessResGetCart;
import com.easyshopping.util.UpdateAndDeleteCart;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Ravindra Birla on 10,June,2021
 */
public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CategoryViewHolder> {

    private Context context;
    CartItemBinding binding;
    List<SuccessResGetCart.Result> cartItems;
    private HashMap<String,Integer> cartMap = new HashMap<>();
    private UpdateAndDeleteCart addUpdateCart;

    public CartAdapter(Context context,List<SuccessResGetCart.Result> cartItems,HashMap<String,Integer> cartMap,UpdateAndDeleteCart addUpdateCart)
    {
        this.context = context;
        this.cartItems = cartItems;
        this.cartMap = cartMap;
        this.addUpdateCart = addUpdateCart;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding= CartItemBinding.inflate(LayoutInflater.from(context));
        //View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_item, parent, false);
        return new CategoryViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {

        ImageView ivDelete,ivPlus,ivMinus;
        TextView tvQuantity,tvPrice;
        ivDelete = holder.itemView.findViewById(R.id.ic_delete);
        ivPlus = holder.itemView.findViewById(R.id.ivPlus);
        ivMinus = holder.itemView.findViewById(R.id.ivMinus);
        tvQuantity = holder.itemView.findViewById(R.id.tvQuantity);
        tvPrice = holder.itemView.findViewById(R.id.tvPrice);

        binding.tvName.setText(cartItems.get(position).getProductDetails().getName());

        if(cartItems.get(position).getImages()!=null)
        {
            Glide.with(context)
                    .load(cartItems.get(position).getImages().get(0))
                    .fitCenter()
                    .into(binding.ivProduct);

        }

        tvQuantity.setText(cartItems.get(position).getQuntity());
        tvPrice.setText(cartItems.get(position).getPrice());

        ivDelete.setOnClickListener(v ->
                {

                    new AlertDialog.Builder(context)
                            .setTitle("Remove Item")
                            .setMessage("Are you sure you want to remove item?")

                            // Specifying a listener allows you to take an action before dismissing the dialog.
                            // The dialog is automatically dismissed when a dialog button is clicked.

                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // Continue with delete operation

                                    addUpdateCart.deleteCart(cartItems.get(position).getId());

                                }
                            })
                            // A null listener allows the button to dismiss the dialog and take no further action.
                            .setNegativeButton(android.R.string.no, null)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();

                }
        );

        ivPlus.setOnClickListener(v ->
                {

                    int quan = cartMap.get(cartItems.get(position).getId());
                    quan = quan +1 ;
                    this.cartMap.put(cartItems.get(position).getId(),quan);
                    addUpdateCart.updateCart(cartItems.get(position).getProductDetails().getId(),cartItems.get(position).getId(),quan+"");
                    tvQuantity.setText(quan+"");

                  /*
                   itemQuantity =  itemQuantity+1;
                    addUpdateCart.addCart(menu.restaurantId,menu.id,itemQuantity+"");
                    tvQuantity.setText(itemQuantity+"");
                   */

                }
        );

        ivMinus.setOnClickListener(v ->
                {

                  /*selectedPosition = childPosition ;

                  itemQuantity =  itemQuantity-1;

                    if(itemQuantity==0)
                    {
                        llAdd.setVisibility(View.VISIBLE);
                        llPlusMinus.setVisibility(View.GONE);
                        addUpdateCart.deleteCart("",menu.getId());
                    }else
                    {
                        addUpdateCart.addCart(menu.restaurantId,menu.id,itemQuantity+"");
                        tvQuantity.setText(itemQuantity+"");
                    }*/

                    int quan = cartMap.get(cartItems.get(position).getId());
                    quan = quan -1 ;

                    if(quan==0)
                    {
                        return;
                    }
                    else
                    {
                        cartMap.put(cartItems.get(position).getId(),quan);
                        addUpdateCart.updateCart(cartItems.get(position).getProductDetails().getId(),cartItems.get(position).getId(),quan+"");
                        tvQuantity.setText(quan+"");
                    }

                }
        );


    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    public class CategoryViewHolder extends RecyclerView.ViewHolder
    {

        public CategoryViewHolder(CartItemBinding binding1) {
            super(binding1.getRoot());
        }
    }

}
