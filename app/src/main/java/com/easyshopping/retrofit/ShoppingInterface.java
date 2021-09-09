package com.easyshopping.retrofit;


import com.easyshopping.models.SuccessResAddAddress;
import com.easyshopping.models.SuccessResAddCard;
import com.easyshopping.models.SuccessResAddReview;
import com.easyshopping.models.SuccessResAddToCart;
import com.easyshopping.models.SuccessResBooking;
import com.easyshopping.models.SuccessResCompare;
import com.easyshopping.models.SuccessResForgotPassword;
import com.easyshopping.models.SuccessResGetAddresses;
import com.easyshopping.models.SuccessResGetBanner;
import com.easyshopping.models.SuccessResGetCards;
import com.easyshopping.models.SuccessResGetCart;
import com.easyshopping.models.SuccessResGetCategory;
import com.easyshopping.models.SuccessResGetCompanies;
import com.easyshopping.models.SuccessResGetOrders;
import com.easyshopping.models.SuccessResGetProductByCategory;
import com.easyshopping.models.SuccessResGetProductByTitle;
import com.easyshopping.models.SuccessResGetProductDetail;
import com.easyshopping.models.SuccessResGetProfile;
import com.easyshopping.models.SuccessResSignIn;
import com.easyshopping.models.SuccessResSignUp;
import com.easyshopping.models.SuccessResUpdateCart;
import com.easyshopping.models.SuccessResUpdateName;

import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface ShoppingInterface {

    @FormUrlEncoded
    @POST("signup")
    Call<SuccessResSignUp> signup(@FieldMap Map<String, String> paramHashMap);

    @FormUrlEncoded
    @POST("login")
    Call<SuccessResSignIn> login(@FieldMap Map<String, String> paramHashMap);

    @FormUrlEncoded
    @POST("forgot_password")
    Call<SuccessResForgotPassword> forgotPassword(@FieldMap Map<String, String> paramHashMap);

    @FormUrlEncoded
    @POST("get_profile")
    Call<SuccessResGetProfile> getProfile(@FieldMap Map<String, String> paramsHashMap);

    @FormUrlEncoded
    @POST("update_name")
    Call<SuccessResUpdateName> updateName(@FieldMap Map<String, String> paramsHashMap);

    @FormUrlEncoded
    @POST("update_email")
    Call<SuccessResUpdateName> updateEmail(@FieldMap Map<String, String> paramsHashMap);

    @FormUrlEncoded
    @POST("update_phone")
    Call<SuccessResUpdateName> updateNumber(@FieldMap Map<String, String> paramsHashMap);

    @FormUrlEncoded
    @POST("update_password")
    Call<SuccessResUpdateName> changePass(@FieldMap Map<String, String> paramsHashMap);

    @GET("get_banner")
    Call<SuccessResGetBanner> getBanners();

    @GET("get_category")
    Call<SuccessResGetCategory> getCategory();

    @FormUrlEncoded
    @POST("get_company")
    Call<SuccessResGetCompanies> getCompaniesByCategory(@FieldMap Map<String, String> paramsHashMap);

    @FormUrlEncoded
    @POST("get_product")
    Call<SuccessResGetProductByCategory> getProductsByCompany(@FieldMap Map<String, String> paramsHashMap);

    @FormUrlEncoded
    @POST("getProductDetailByProductID")
    Call<SuccessResGetProductDetail> getProductByProductId(@FieldMap Map<String, String> paramsHashMap);

    @FormUrlEncoded
    @POST("ProductCompare")
    Call<SuccessResCompare> getProductCompare(@FieldMap Map<String, String> paramsHashMap);

    @FormUrlEncoded
    @POST("add_to_cart")
    Call<SuccessResAddToCart> addToCart(@FieldMap Map<String, String> paramsHashMap);

    @FormUrlEncoded
    @POST("getCartByUserId")
    Call<SuccessResGetCart> getCartByUserId(@FieldMap Map<String, String> paramsHashMap);

    @FormUrlEncoded
    @POST("update_cart")
    Call<SuccessResUpdateCart> updateCart(@FieldMap Map<String, String> paramsHashMap);

    @FormUrlEncoded
    @POST("delete_cart_item")
    Call<SuccessResUpdateCart> deleteCart(@FieldMap Map<String, String> paramsHashMap);

    @FormUrlEncoded
    @POST("add_address")
    Call<SuccessResAddAddress> addAddress (@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("update_address")
    Call<SuccessResUpdateCart> updateAddress (@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("get_address")
    Call<SuccessResGetAddresses> getUserAddress(@FieldMap Map<String, String> paramsHashMap);

    @FormUrlEncoded
    @POST("delete_address")
    Call<SuccessResUpdateCart> deleteAddress(@FieldMap Map<String, String> paramsHashMap);

    @FormUrlEncoded
    @POST("add_to_card_details")
    Call<SuccessResAddCard> addCard (@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("get_card_details")
    Call<SuccessResGetCards> getCards(@FieldMap Map<String, String> paramsHashMap);


    @FormUrlEncoded
    @POST("get_product_search")
    Call<SuccessResGetProductByTitle> getProductSearch(@FieldMap Map<String, String> paramsHashMap);

    @FormUrlEncoded
    @POST("get_product_com_title")
    Call<SuccessResGetProductByTitle> getProductofVendor(@FieldMap Map<String, String> paramsHashMap);

    @FormUrlEncoded
    @POST("search_cat_byTitle")
    Call<SuccessResGetCategory> searchCatByTitle(@FieldMap Map<String, String> paramsHashMap);


    @FormUrlEncoded
    @POST("get_banner_product")
    Call<SuccessResGetProductByTitle> getBannerProduct(@FieldMap Map<String, String> paramsHashMap);


    @FormUrlEncoded
    @POST("userbooking")
    Call<SuccessResBooking> book(@FieldMap Map<String, String> paramsHashMap);


    @FormUrlEncoded
    @POST("get_orders")
    Call<SuccessResGetOrders> getOrders(@FieldMap Map<String, String> paramsHashMap);

    @FormUrlEncoded
    @POST("add_review_rating")
    Call<SuccessResAddReview> addReview(@FieldMap Map<String, String> paramHashMap);



/*

    @Multipart
    @POST(LOGIN_API)
    Call<SuccessResSignIn> login (@Part("email") RequestBody last_name,
                                   @Part("password") RequestBody email,
                                   @Part("register_id") RequestBody mobile);
*/

/*

    @FormUrlEncoded
    @POST(LOGIN_API)
    Call<SuccessResSignIn> login(@FieldMap Map<String, String> paramHashMap);


    @FormUrlEncoded
    @POST(FORGET_PASSWORD)
    Call<SuccessResForgetPassword> forgotPassword(@FieldMap Map<String, String> paramHashMap);

    @FormUrlEncoded
    @POST(CATEGORY_LIST)
    Call<SuccessResAllCategories> getAllCategories(@FieldMap Map<String, String> paramHashMap);

    @FormUrlEncoded
    @POST(GET_SUB_CATEGORIES)
    Call<SuccessResGetAllSubCategories> getAllSubCategories(@FieldMap Map<String, String> paramHashMap);

    */



/*

    @Multipart
    @POST(ADD_ITEM)
    Call<SuccessResItemAdded> addItem (@Part("user_id") RequestBody userId,
                                       @Part("category_id") RequestBody categoryId,
                                       @Part("sub_category_id") RequestBody subCategoryId,
                                       @Part("conditions") RequestBody condition,
                                       @Part("title") RequestBody title,
                                       @Part("description") RequestBody description,
                                       @Part("price") RequestBody price,
                                       @Part("address") RequestBody address,
                                       @Part("lat") RequestBody latitue,
                                       @Part("lon") RequestBody lon,
                                       @Part MultipartBody.Part file);

    @FormUrlEncoded
    @POST(GET_ITEM)
    Call<SuccessResGetMyItems> getAllMyItems(@FieldMap Map<String, String> paramHashMap);

    @GET(GET_BANNERS)
    Call<SuccessResBannersList> getBanners();

    @FormUrlEncoded
    @POST(GET_PROFILE)
    Call<SuccessResProfileData> getSellerDetails(@FieldMap Map<String, String> paramHashMap);

    @FormUrlEncoded
    @POST(GET_PRODUCT_BY_CATEGORY)
    Call<SuccessResGetProductByCategory> getProductsByCategory(@FieldMap Map<String, String> paramHashMap);

    @FormUrlEncoded
    @POST(GET_PRODUCT_DETAIL)
    Call<SuccessResProductDetail> getProductDetail(@FieldMap Map<String, String> paramHashMap);


    @FormUrlEncoded
    @POST(GET_ALL_PRODUCT)
    Call<SuccessResGetMyItems> getAllProducts(@FieldMap Map<String, String> paramHashMap);

    @Multipart
    @POST(UPDATE_PROFILE)
    Call<SuccessResUpdateProfile> updateProfile (@Part("user_id") RequestBody userId,
                                                 @Part("name") RequestBody first_name,
                                                 @Part("email") RequestBody last_name,
                                                 @Part("address") RequestBody address,
                                                 @Part("lat") RequestBody lat,
                                                 @Part("lon") RequestBody lng,
                                                 @Part MultipartBody.Part file);

    @FormUrlEncoded
    @POST(DELETE_ITEM)
    Call<SuccessResDeleteItem> deleteItem(@FieldMap Map<String, String> paramHashMap);

    @Multipart
    @POST(UPDATE_ITEM)
    Call<SuccessResUpdateItem> updateItem (@Part("item_id") RequestBody itemId,
                                           @Part("title") RequestBody title,
                                           @Part("price") RequestBody price,
                                           @Part("conditions") RequestBody conditions,
                                           @Part("address") RequestBody address,
                                           @Part("lat") RequestBody lat,
                                           @Part("lon") RequestBody lng,
                                           @Part("description") RequestBody description,
                                           @Part MultipartBody.Part file);


    @FormUrlEncoded
    @POST(ADD_FAVORITE)
    Call<SuccessResAddFavourite> addFavorite(@FieldMap Map<String, String> paramHashMap);

    @FormUrlEncoded
    @POST(CHAT_REQUEST)
    Call<SuccessResChatRequest> chatRequest(@FieldMap Map<String, String> paramHashMap);


    @FormUrlEncoded
    @POST(GET_NOTIFICATION)
    Call<SuccessResGetNotifications> getNotifications(@FieldMap Map<String, String> paramHashMap);

    @FormUrlEncoded
    @POST(UPDATE_CHAT_STATUS)
    Call<SuccessResRequestStatus> updateRequestStatus(@FieldMap Map<String, String> paramHashMap);

    @Multipart
    @POST(INSERT_CHAT)
    Call<SuccessResInsertChat> insertChat (@Part("sender_id") RequestBody senderId,
                                           @Part("receiver_id") RequestBody receiverId,
                                           @Part("chat_message") RequestBody message,
                                           @Part("item_id") RequestBody itemId,
                                           @Part MultipartBody.Part file);


    @FormUrlEncoded
    @POST(GET_CHAT)
    Call<SuccessResGetChat> getChat(@FieldMap Map<String, String> paramHashMap);


    @FormUrlEncoded
    @POST(GET_CONVERSATION)
    Call<SuccessResGetConversation> getConversation(@FieldMap Map<String, String> paramHashMap);

    @FormUrlEncoded
    @POST(GET_CHAT_REQUEST)
    Call<SuccessResGetChatRequest> getChatRequest(@FieldMap Map<String, String> paramHashMap);

    @FormUrlEncoded
    @POST(GET_AVAILABLE_CHAT_REQUEST)
    Call<SuccessResAvialableChatRequest> getAvailableChatRequest(@FieldMap Map<String, String> paramHashMap);


    @FormUrlEncoded
    @POST(ITEM_SEARCH)
    Call<SuccessResGetMyItems> searchItem(@FieldMap Map<String, String> paramHashMap);

    @FormUrlEncoded
    @POST(ADD_ITEM_OFFER)
    Call<SuccessResAddOfferItem> addItemOffer(@FieldMap Map<String, String> paramHashMap);

    @FormUrlEncoded
    @POST(PRIVACY_POLICY)
    Call<SuccessResPrivacyPolicy> getPrivacyPolicy(@FieldMap Map<String, String> paramHashMap);

    @FormUrlEncoded
    @POST(TERMS_AND_CONDITION)
    Call<SuccessResPrivacyPolicy> getTermsAndCondition(@FieldMap Map<String, String> paramHashMap);

    @FormUrlEncoded
    @POST(USER_PRODUCT_BY_CATEGORY)
    Call<SuccessResGetMyItems> userProductByCategory(@FieldMap Map<String, String> paramHashMap);


    @FormUrlEncoded
    @POST(SEARCH_ITEM_BY_CATEGORY)
    Call<SuccessResGetMyItems> searchWithCategory(@FieldMap Map<String, String> paramHashMap);

    @Multipart
    @POST(SOCIAL_LOGIN)
    Call<SuccessResSocialLogin> socialLogin (@Part("first_name") RequestBody first_name,
                                             @Part("email") RequestBody last_name,
                                             @Part("address") RequestBody address,
                                             @Part("lat") RequestBody lat,
                                             @Part("lon") RequestBody lng,
                                             @Part("register_id") RequestBody registerId,
                                             @Part("social_id") RequestBody socialId,
                                             @Part("image") RequestBody image);

    @FormUrlEncoded
    @POST(CONTACT_US)
    Call<SuccessResContactUs> contactUs(@FieldMap Map<String, String> paramHashMap);

    @FormUrlEncoded
    @POST(USER_ITEM_SEARCH)
    Call<SuccessResGetMyItems> seachSellerItem(@FieldMap Map<String, String> paramHashMap);

    @FormUrlEncoded
    @POST(UPDATE_LANGUAGE)
    Call<SuccessResRequestStatus> changeLanguage(@FieldMap Map<String, String> paramHashMap);

    @FormUrlEncoded
    @POST(CHANGE_PASSWORD)
    Call<SuccessResRequestStatus> changePassword(@FieldMap Map<String, String> paramHashMap);
*/


/*

    @POST(SIGN_UP_API)
    Call<SuccessResSignUp> signUp(@Body Map<String, String> paramHashMap);
*/

/*
    @POST(SIGN_UP_API)
    Call<SuccessResSignUp> signUp(@Body Map<String, String> paramHashMap);
*/

/*
    @FormUrlEncoded
    @POST("login")
    Call<SignupModel> userLogin (@FieldMap Map<String,String> params);

    @FormUrlEncoded
    @POST("forgot_password")
    Call<Map<String,String>> forgotPass (@FieldMap Map<String,String> params);

    @Multipart
    @POST("update_profile")
    Call<SignupModel> editprofile(
            @Part("user_id") RequestBody id,
            @Part("first_name") RequestBody first_name,
            @Part("last_name") RequestBody last_name,
            @Part("email") RequestBody email,
            @Part("mobile") RequestBody mobile,
            @Part("phone_code") RequestBody phone_code,
            @Part MultipartBody.Part file);


    @FormUrlEncoded
    @POST("change_password")
    Call<SignupModel> changePassword (@FieldMap Map<String,String> params);*/

/*

    @GET("car_list")
    Call<CarListModel> getCarList();

    @FormUrlEncoded
    @POST("brand_list")
    Call<BrandListModel> cardBrandList(@FieldMap Map<String,String> params);


    @FormUrlEncoded
    @POST("model_list")
    Call<ModListModel> modelList(@FieldMap Map<String,String> params);


    @Multipart
    @POST("driver_signup2")
    Call<SignupModel> addVehicle (@Part("user_id") RequestBody user_id,
                                    @Part("car_type_id") RequestBody car_type_id,
                                    @Part("brand") RequestBody brand,
                                    @Part("car_model") RequestBody car_model,
                                    @Part("year_of_manufacture") RequestBody year_of_manufacture,
                                    @Part("car_number") RequestBody car_number,
                                    @Part("car_color") RequestBody car_color,
                                    @Part MultipartBody.Part file);


    @FormUrlEncoded
    @POST("driver_signup3")
    Call<SignupModel> addBank(@FieldMap Map<String,String> params);

*/


}
