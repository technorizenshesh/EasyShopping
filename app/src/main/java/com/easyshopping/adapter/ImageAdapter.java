package com.easyshopping.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.easyshopping.R;
import com.easyshopping.models.SuccessResGetProductDetail;

import java.util.List;

/**
 * Created by Ravindra Birla on 21,June,2021
 */
public class ImageAdapter extends PagerAdapter {
    Context mContext;

    private List<SuccessResGetProductDetail.Image> images;
    public ImageAdapter(Context context,List<SuccessResGetProductDetail.Image> images) {
        this.mContext = context;
        this.images = images;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((ImageView) object);
    }


    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ImageView imageView = new ImageView(mContext);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        Glide.with(mContext)
                .load(images.get(position).getImage())
                .fitCenter()
                .into(imageView);
        ((ViewPager) container).addView(imageView, 0);
        return imageView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager) container).removeView((ImageView) object);
    }

    @Override
    public int getCount() {
        return images.size();
    }
}