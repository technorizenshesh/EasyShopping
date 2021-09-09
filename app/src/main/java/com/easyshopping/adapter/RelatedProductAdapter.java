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
import com.easyshopping.databinding.RelatedProductItemBinding;
import com.easyshopping.databinding.RelatedProductItemBinding;
import com.easyshopping.models.SuccessResGetProductByCategory;

import java.util.List;

/**
 * Created by Ravindra Birla on 10,June,2021
 */
public class RelatedProductAdapter extends RecyclerView.Adapter<RelatedProductAdapter.CategoryViewHolder> {

    private Context context;
    RelatedProductItemBinding binding;
    private List<SuccessResGetProductByCategory.Result> products;

    public RelatedProductAdapter(Context context,List<SuccessResGetProductByCategory.Result> products)
    {
        this.context = context;
        this.products = products;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding= RelatedProductItemBinding.inflate(LayoutInflater.from(context));

        //View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_item, parent, false);
        return new CategoryViewHolder(binding);

    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {

        binding.tvTitle.setText(products.get(position).getName());
        binding.MyRating.setRating(Float.parseFloat(products.get(position).getRatting()));
        binding.tvRating.setText("("+products.get(position).getRatting()+")");

        if(products.get(position).getImage().size()>0)
        {
            Glide.with(context)
                    .load(products.get(position).getImage().get(0).getImage())
                    .fitCenter()
                    .into(binding.ivProduct);

        }


        binding.tvPrice.setText("$ "+products.get(position).getPrice());

        binding.llProduct.setOnClickListener(v ->
                {

//                    Navigation.findNavController(v).navigate(R.id.action_itemListFragment_to_productFragment,bundle);
                }
        );


    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public class CategoryViewHolder extends RecyclerView.ViewHolder
    {

        public CategoryViewHolder(RelatedProductItemBinding binding1) {
            super(binding1.getRoot());
        }
    }

}
