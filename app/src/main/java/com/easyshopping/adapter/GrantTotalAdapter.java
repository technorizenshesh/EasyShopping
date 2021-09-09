package com.easyshopping.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.easyshopping.R;
import com.easyshopping.databinding.GrantItemBinding;
import com.easyshopping.databinding.GrantItemBinding;

/**
 * Created by Ravindra Birla on 10,June,2021
 */
public class GrantTotalAdapter extends RecyclerView.Adapter<GrantTotalAdapter.CategoryViewHolder> {

    private Context context;
    GrantItemBinding binding;
  
    public GrantTotalAdapter(Context context)
    {
        this.context = context;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding= GrantItemBinding.inflate(LayoutInflater.from(context));

        //View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_item, parent, false);
        return new CategoryViewHolder(binding);

    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {

        binding.btnCheckout.setOnClickListener(v ->
                {
                    Navigation.findNavController(v).navigate(R.id.action_comparativeGrandTotalFragment_to_orderReviewFragment);
                }
                );

    }

    @Override
    public int getItemCount() {
        return 3;
    }

    public class CategoryViewHolder extends RecyclerView.ViewHolder
    {

        public CategoryViewHolder(GrantItemBinding binding1) {
            super(binding1.getRoot());
        }
    }

}
