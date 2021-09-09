package com.easyshopping.adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.easyshopping.R;
import com.easyshopping.databinding.CategoryItemBinding;
import com.easyshopping.models.SuccessResGetCategory;

import java.util.List;

/**
 * Created by Ravindra Birla on 10,June,2021
 */
public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {

    private Context context;
    CategoryItemBinding binding;
    List<SuccessResGetCategory.Result> categories;
    private boolean fromHome;

    public CategoryAdapter(Context context,List<SuccessResGetCategory.Result> categories,boolean fromHome)
    {
        this.context = context;
        this.categories = categories;
        this.fromHome = fromHome;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding= CategoryItemBinding.inflate(LayoutInflater.from(context));
        //View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_item, parent, false);
        return new CategoryViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {

        binding.tvName.setText(categories.get(position).getCategoryName());

        Glide.with(context)
                .load(categories.get(position).getImage())
                .fitCenter()
                .into(binding.ivProfile);

        binding.rlCategory.setOnClickListener(v ->

                {

                    if (fromHome)
                    {
                        Bundle bundle = new Bundle();
                        bundle.putString("categoryId",categories.get(position).getId());
                        Navigation.findNavController(v).navigate(R.id.action_navigation_home_to_subCategoryFragment,bundle);
                    }
                    else
                    {
                        Bundle bundle = new Bundle();
                        bundle.putString("categoryId",categories.get(position).getId());
                        Navigation.findNavController(v).navigate(R.id.action_searchCatFragment_to_subCategoryFragment,bundle);
                    }

                }
                );

    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    public class CategoryViewHolder extends RecyclerView.ViewHolder
    {

        public CategoryViewHolder(CategoryItemBinding binding1) {
            super(binding1.getRoot());
        }
    }

}
