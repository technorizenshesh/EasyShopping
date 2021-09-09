package com.easyshopping.adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.easyshopping.R;
import com.easyshopping.activities.HomeActivity;
import com.easyshopping.databinding.ProductItemBinding;
import com.easyshopping.models.SuccessResCompare;
import com.easyshopping.models.SuccessResGetCart;
import com.easyshopping.models.SuccessResGetProductByTitle;
import com.easyshopping.util.MyClick;

import java.util.List;

import static com.easyshopping.activities.HomeActivity.navController;

/**
 * Created by Ravindra Birla on 10,June,2021
 */
public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.CategoryViewHolder> {

    private Context context;

    ProductItemBinding binding;

    private MyClick myClick;

    HomeActivity homeActivity;

    private String companyId="";

    private String from;

    String myCartCompany = "";

    private List<SuccessResGetProductByTitle.Result> products;
    private List<SuccessResGetCart.Result> cartItems;

    public SearchAdapter(Context context, List<SuccessResGetProductByTitle.Result> products, MyClick myClick, List<SuccessResGetCart.Result> cartItems,String from)
    {
        this.context = context;
        this.products = products;
        this.myClick = myClick;
        this.cartItems = cartItems;
        this.from = from;
    }

    @NonNull
    @Override
    public SearchAdapter.CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding= ProductItemBinding.inflate(LayoutInflater.from(context));
        //View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_item, parent, false);
        return new SearchAdapter.CategoryViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {

        companyId = products.get(position).getCompanyId();

        AppCompatButton button =  holder.itemView.findViewById(R.id.btnAddToCart);
        AppCompatButton buttonCart =  holder.itemView.findViewById(R.id.btnViewCart);

        LinearLayout llProduct = holder.itemView.findViewById(R.id.llProduct);

        button.setOnClickListener(v ->
                {
                    button.setVisibility(View.GONE);
                    buttonCart.setVisibility(View.VISIBLE);
                    myClick.getClick(products.get(position).getCompanyId(),products.get(position).getId(),products.get(position).getPrice(),"1");
                }
        );

        binding.tvTitle.setText(products.get(position).getName());

            Glide.with(context)
                    .load(products.get(position).getImage())
                    .fitCenter()
                    .into(binding.ivProduct);

        binding.tvPrice.setText("$"+products.get(position).getPrice());

        binding.btnCompare.setOnClickListener(v ->
                {

                    if(from.equalsIgnoreCase("search"))
                    {
                        Bundle bundle = new Bundle();
                        bundle.putString("name",products.get(position).getName());
                        Navigation.findNavController(v).navigate(R.id.action_navigation_search_to_comparisonFragment,bundle);
                    }
                    else if(from.equalsIgnoreCase("promotion"))
                    {
                        Bundle bundle = new Bundle();
                        bundle.putString("name",products.get(position).getName());
                        Navigation.findNavController(v).navigate(R.id.action_promotionFragment_to_comparisonFragment,bundle);
                    } else if(from.equalsIgnoreCase("vendor"))
                    {
                        Bundle bundle = new Bundle();
                        bundle.putString("name",products.get(position).getName());
                        Navigation.findNavController(v).navigate(R.id.action_searchVendorItemFragment_to_comparisonFragment,bundle);
                    }

                }
        );

        binding.btnViewCart.setOnClickListener(v ->
                {
                    navController.navigate(R.id.navigation_cart);
                }
        );

        llProduct.setOnClickListener(v ->
                {

                    if(from.equalsIgnoreCase("search"))
                    {
                        Bundle bundle = new Bundle();
                        bundle.putString("id",products.get(position).getId());
                        Navigation.findNavController(v).navigate(R.id.action_navigation_search_to_productFragment,bundle);
                    }
                    else if(from.equalsIgnoreCase("promotion"))
                    {
                        Bundle bundle = new Bundle();
                        bundle.putString("id",products.get(position).getId());
                        Navigation.findNavController(v).navigate(R.id.action_promotionFragment_to_productFragment,bundle);
                    } else if(from.equalsIgnoreCase("vendor"))
                    {
                        Bundle bundle = new Bundle();
                        bundle.putString("id",products.get(position).getId());
                        Navigation.findNavController(v).navigate(R.id.action_searchVendorItemFragment_to_productFragment,bundle);
                    }


                }
        );

       /* binding.btnAddToCart.setOnClickListener(v ->
                {

                    binding.btnAddToCart.setVisibility(View.GONE);
                    binding.btnViewCart.setVisibility(View.VISIBLE);
                    myClick.getClick(products.get(position).getId(),products.get(position).getPrice(),"1");

                }
                );*/

        if(hasItemInCart(products.get(position).getId()))
        {
            binding.btnAddToCart.setVisibility(View.GONE);
            binding.btnViewCart.setVisibility(View.VISIBLE);
        }else
        {
            binding.btnAddToCart.setVisibility(View.VISIBLE);
            binding.btnViewCart.setVisibility(View.GONE);
        }

    }


    @Override
    public int getItemCount() {
        return products.size();
    }

    public class CategoryViewHolder extends RecyclerView.ViewHolder
    {

        public CategoryViewHolder(ProductItemBinding binding1) {
            super(binding1.getRoot());
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

}
