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
import com.easyshopping.databinding.SubCategoryItemBinding;
import com.easyshopping.models.SuccessResGetCompanies;

import java.util.List;

/**
 * Created by Ravindra Birla on 10,June,2021
 */
public class SubCategoryAdapter extends RecyclerView.Adapter<SubCategoryAdapter.CategoryViewHolder> {

    private Context context;
    SubCategoryItemBinding binding;

    private List<SuccessResGetCompanies.Result> companies;

    public SubCategoryAdapter(Context context,List<SuccessResGetCompanies.Result> companies)
    {
        this.context = context;
        this.companies = companies;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding= SubCategoryItemBinding.inflate(LayoutInflater.from(context));

        //View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_item, parent, false);
        return new CategoryViewHolder(binding);

    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {

      binding.tvCompanyName.setText(companies.get(position).getName());

        Glide.with(context)
                .load(companies.get(position).getImage())
                .fitCenter()
                .into(binding.ivCompany);

        binding.cvCompany.setOnClickListener(v ->

                {

                    Bundle bundle = new Bundle();
                    bundle.putString("companyId",companies.get(position).getId());
                    Navigation.findNavController(v).navigate(R.id.action_subCategoryFragment_to_itemListFragment,bundle);
                }
                );

    }

    @Override
    public int getItemCount() {
        return companies.size();
    }

    public class CategoryViewHolder extends RecyclerView.ViewHolder
    {

        public CategoryViewHolder(SubCategoryItemBinding binding1) {
            super(binding1.getRoot());
        }
    }

}
