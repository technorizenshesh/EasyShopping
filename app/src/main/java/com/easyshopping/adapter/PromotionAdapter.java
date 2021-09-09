package com.easyshopping.adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.easyshopping.R;
import com.easyshopping.databinding.PromotionItemBinding;
import com.easyshopping.databinding.PromotionItemBinding;
import com.easyshopping.models.SuccessResGetCart;
import com.easyshopping.models.SuccessResGetProductByTitle;
import com.easyshopping.util.MyClick;

import java.util.LinkedList;
import java.util.List;

import static com.easyshopping.activities.HomeActivity.navController;

/**
 * Created by Ravindra Birla on 10,June,2021
 */
public class PromotionAdapter extends RecyclerView.Adapter<PromotionAdapter.CategoryViewHolder> {

    private Context context;
    PromotionItemBinding binding;
    private List<SuccessResGetProductByTitle.Result> productsList;
    private List<SuccessResGetCart.Result> cartItems;

    private MyClick myClick;

    private String companyId = "";

    public PromotionAdapter(Context context,List<SuccessResGetProductByTitle.Result> productsList, List<SuccessResGetCart.Result> cartItems,MyClick myClick)
    {
        this.context = context;
        this.productsList = productsList;
        this.myClick = myClick;
        this.cartItems = cartItems;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding= PromotionItemBinding.inflate(LayoutInflater.from(context));

        //View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_item, parent, false);
        return new CategoryViewHolder(binding);

    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {

        companyId = productsList.get(position).getCompanyId();

        AppCompatButton button =  holder.itemView.findViewById(R.id.btnAddToCart);
        AppCompatButton buttonCart =  holder.itemView.findViewById(R.id.btnViewCart);

        button.setOnClickListener(v ->
                {
                    button.setVisibility(View.GONE);
                    buttonCart.setVisibility(View.VISIBLE);
                    myClick.getClick(productsList.get(position).getCompanyId(),productsList.get(position).getId(),productsList.get(position).getPrice(),"1");
                }
        );

        binding.tvTitle.setText(productsList.get(position).getName());

        Glide.with(context)
                .load(productsList.get(position).getImage())
                .fitCenter()
                .into(binding.ivProfile);

        binding.tvPrice.setText("$"+productsList.get(position).getPrice());

        binding.btnViewCart.setOnClickListener(v ->
                {
                    navController.navigate(R.id.navigation_cart);
                }
        );


       /* binding.btnAddToCart.setOnClickListener(v ->
                {

                    binding.btnAddToCart.setVisibility(View.GONE);
                    binding.btnViewCart.setVisibility(View.VISIBLE);
                    myClick.getClick(products.get(position).getId(),products.get(position).getPrice(),"1");

                }
                );*/

        if(hasItemInCart(productsList.get(position).getId()))
        {
            binding.btnAddToCart.setVisibility(View.GONE);
            binding.btnViewCart.setVisibility(View.VISIBLE);
        }else
        {
            binding.btnAddToCart.setVisibility(View.VISIBLE);
            binding.btnViewCart.setVisibility(View.GONE);
        }


    }


    private boolean hasItemInCart(String id)
    {
        for (SuccessResGetCart.Result item:cartItems)
        {

            if(item.getProductDetails().getId().equalsIgnoreCase(id))
            {
                return true;
            }

        }

        return false;

    }

    @Override
    public int getItemCount() {
        return productsList.size();
    }

    public class CategoryViewHolder extends RecyclerView.ViewHolder
    {

        public CategoryViewHolder(PromotionItemBinding binding1) {
            super(binding1.getRoot());
        }
    }

}
