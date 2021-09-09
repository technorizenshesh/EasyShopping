package com.easyshopping.util;

import android.view.View;

/**
 * Created by Ravindra Birla on 19,July,2021
 */
public interface UpdateAndDeleteAddress {

    public void updateCart(View v, String name, String address, String id, String phone, String countrycode);
    public void deleteCart(String productId);
    public void isSelected(boolean isSelected,int position);

}
