package com.easyshopping.util;

/**
 * Created by Ravindra Birla on 19,July,2021
 */
public interface UpdateAndDeleteCart {

    public void updateCart(String productId,String userId,String quantity);
    public void deleteCart(String productId);

}
