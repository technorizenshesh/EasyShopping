package com.easyshopping.adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.easyshopping.R;
import com.easyshopping.databinding.CartBottomItemListBinding;
import com.easyshopping.databinding.CartBottomItemListBinding;
import com.easyshopping.models.SuccessResGetCart;
import com.easyshopping.models.SuccessResGetCompanies;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Ravindra Birla on 10,June,2021
 */
public class CartBottomItemAdapter extends RecyclerView.Adapter<CartBottomItemAdapter.CategoryViewHolder> {

    private Context context;
    CartBottomItemListBinding binding;
    
    private List<SuccessResGetCart.Result> cartItems ;


    public CartBottomItemAdapter(Context context,List<SuccessResGetCart.Result> cartItems)
    {
        this.context = context;
        this.cartItems = cartItems;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding= CartBottomItemListBinding.inflate(LayoutInflater.from(context));

        //View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_item, parent, false);
        return new CategoryViewHolder(binding);

    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {


        binding.tvName.setText(cartItems.get(position).getProductDetails().getName()+"("+cartItems.get(position).getQuntity()+")");

        int a = Integer.parseInt(cartItems.get(position).getQuntity());
        int b = Integer.parseInt(cartItems.get(position).getPrice());

        int total = a*b;

        binding.tvPrice.setText(""+total);

    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    public class CategoryViewHolder extends RecyclerView.ViewHolder
    {

        public CategoryViewHolder(CartBottomItemListBinding binding1) {
            super(binding1.getRoot());
        }
    }

}
