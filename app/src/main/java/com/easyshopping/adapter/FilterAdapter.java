package com.easyshopping.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.easyshopping.databinding.FilterItemBinding;
import com.easyshopping.databinding.FilterItemBinding;

import java.util.LinkedList;

/**
 * Created by Ravindra Birla on 10,June,2021
 */

public class FilterAdapter extends RecyclerView.Adapter<FilterAdapter.CategoryViewHolder> {

    private Context context;
    FilterItemBinding binding;
    private LinkedList<String> data;
 
    public FilterAdapter(Context context,LinkedList<String> data)
    {
        this.context = context;
        this.data = data;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding= FilterItemBinding.inflate(LayoutInflater.from(context));

        //View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_item, parent, false);
        return new CategoryViewHolder(binding);

    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {

        binding.tvFilter.setText(data.get(position));

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class CategoryViewHolder extends RecyclerView.ViewHolder
    {

        public CategoryViewHolder(FilterItemBinding binding1) {
            super(binding1.getRoot());
        }
    }

}
