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
import com.easyshopping.activities.HomeActivity;
import com.easyshopping.databinding.ProductItemBinding;
import com.easyshopping.models.SuccessResCompare;
import com.easyshopping.models.SuccessResGetCart;
import com.easyshopping.models.SuccessResGetProductByCategory;
import com.easyshopping.util.MyClick;

import java.util.List;

import static com.easyshopping.activities.HomeActivity.navController;

/**
 * Created by Ravindra Birla on 10,June,2021
 */
public class ComparisonAdapter extends RecyclerView.Adapter<ComparisonAdapter.CategoryViewHolder> {

    private Context context;

    ProductItemBinding binding;

    private MyClick myClick;
    HomeActivity homeActivity;

    private String companyId="";
    String myCartCompany = "";

    private List<SuccessResCompare.Result> products;
    private List<SuccessResGetCart.Result> cartItems;

    public ComparisonAdapter(Context context,List<SuccessResCompare.Result> products,MyClick myClick,List<SuccessResGetCart.Result> cartItems)
    {
        this.context = context;
        this.products = products;
        this.myClick = myClick;
        this.cartItems = cartItems;
    }

    @NonNull
    @Override
    public ComparisonAdapter.CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding= ProductItemBinding.inflate(LayoutInflater.from(context));
        //View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_item, parent, false);
        return new ComparisonAdapter.CategoryViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {

        binding.btnCompare.setVisibility(View.GONE);

        companyId = products.get(position).getCompanyId();

        AppCompatButton button =  holder.itemView.findViewById(R.id.btnAddToCart);
        AppCompatButton buttonCart =  holder.itemView.findViewById(R.id.btnViewCart);

        button.setOnClickListener(v ->
                {
                    button.setVisibility(View.GONE);
                    buttonCart.setVisibility(View.VISIBLE);
                    myClick.getClick(products.get(position).getCompanyId(),products.get(position).getId(),products.get(position).getPrice(),"1");
                }
        );

        binding.tvTitle.setText(products.get(position).getName());

        if(products.get(position).getImage().size()>0)
        {
            Glide.with(context)
                    .load(products.get(position).getImage().get(0).getImage())
                    .fitCenter()
                    .into(binding.ivProduct);
        }

        binding.tvPrice.setText("$"+products.get(position).getPrice());

      /*  binding.btnCompare.setOnClickListener(v ->
                {
                    Bundle bundle = new Bundle();
                    bundle.putString("name",products.get(position).getName());
                    Navigation.findNavController(v).navigate(R.id.action_itemListFragment_to_comparisonFragment,bundle);
                }
        );
*/
        binding.btnViewCart.setOnClickListener(v ->
                {
                    navController.navigate(R.id.navigation_cart);
                }
        );

        binding.llProduct.setOnClickListener(v ->
                {
                    Bundle bundle = new Bundle();
                    bundle.putString("id",products.get(position).getId());
                    Navigation.findNavController(v).navigate(R.id.action_itemListFragment_to_productFragment,bundle);
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
