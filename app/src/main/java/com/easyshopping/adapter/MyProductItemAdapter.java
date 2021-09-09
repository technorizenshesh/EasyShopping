package com.easyshopping.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.easyshopping.MainActivity;
import com.easyshopping.R;
import com.easyshopping.databinding.MyItemDetailBinding;
import com.easyshopping.databinding.MyItemDetailBinding;
import com.easyshopping.databinding.ProductItemBinding;
import com.easyshopping.models.SuccessResGetOrders;
import com.easyshopping.util.RatingBarClick;

import java.util.List;

/**
 * Created by Ravindra Birla on 10,June,2021
 */
public class MyProductItemAdapter extends RecyclerView.Adapter<MyProductItemAdapter.CategoryViewHolder> {

    private Context context;
    MyItemDetailBinding binding;

    private List<SuccessResGetOrders.ItemDatum> addressList;

    private RatingBarClick ratingBarClick;


    public MyProductItemAdapter(Context context, List<SuccessResGetOrders.ItemDatum> addressList,RatingBarClick ratingBarClick)
    {
        this.context = context;
        this.addressList= addressList;
        this.ratingBarClick = ratingBarClick;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding= MyItemDetailBinding.inflate(LayoutInflater.from(context));

        //View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_item, parent, false);
        return new CategoryViewHolder(binding);

    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {

        TextView tvProductName,tvQuantity;
        TextView btnDetail;
        RatingBar ratingBar;
        ImageView ivCompany;

        tvProductName = holder.itemView.findViewById(R.id.tvProductName);
        tvQuantity = holder.itemView.findViewById(R.id.tvQuantity);
        ivCompany = holder.itemView.findViewById(R.id.ivProductImage);
        ratingBar = holder.itemView.findViewById(R.id.ratingBar);
        tvProductName.setText(addressList.get(position).getProductName());
        tvQuantity.setText(addressList.get(position).getPrice()+" x "+addressList.get(position).getQuntity());

        if(addressList.get(position).getRating()!=null)
        {
            ratingBar.setRating(Float.parseFloat(String.valueOf(addressList.get(position).getRating().getRating())));
        }

        ratingBar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    float touchPositionX = event.getX();
                    float width = ratingBar.getWidth();
                    float starsf = (touchPositionX / width) * 5.0f;
                    int stars = (int)starsf + 1;
                    ratingBar.setRating(stars);
                    ratingBarClick.getClick(addressList.get(position).getProductId(),stars+"");

                    v.setPressed(false);
                }
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    v.setPressed(true);
                }

                if (event.getAction() == MotionEvent.ACTION_CANCEL) {
                    v.setPressed(false);
                }




                return true;
            }});


        Glide.with(context)
                .load(addressList.get(position).getItemImage())
                .fitCenter()
                .into(ivCompany);


    }

    @Override
    public int getItemCount() {
        return addressList.size();
    }

    public class CategoryViewHolder extends RecyclerView.ViewHolder
    {

        public CategoryViewHolder(MyItemDetailBinding binding1) {
            super(binding1.getRoot());
        }
    }

}
