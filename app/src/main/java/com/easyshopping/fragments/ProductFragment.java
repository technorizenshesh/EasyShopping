package com.easyshopping.fragments;

import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.easyshopping.R;
import com.easyshopping.adapter.ImageAdapter;
import com.easyshopping.adapter.ProductsAdapter;
import com.easyshopping.adapter.RelatedProductAdapter;
import com.easyshopping.databinding.FragmentProductBinding;
import com.easyshopping.models.SuccessResAddToCart;
import com.easyshopping.models.SuccessResGetCart;
import com.easyshopping.models.SuccessResGetCompanies;
import com.easyshopping.models.SuccessResGetProductByCategory;
import com.easyshopping.models.SuccessResGetProductDetail;
import com.easyshopping.models.SuccessResUpdateCart;
import com.easyshopping.retrofit.ApiClient;
import com.easyshopping.retrofit.ShoppingInterface;
import com.easyshopping.util.DataManager;
import com.easyshopping.util.SharedPreferenceUtility;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.easyshopping.activities.HomeActivity.navController;
import static com.easyshopping.retrofit.Constant.USER_ID;
import static com.easyshopping.retrofit.Constant.showToast;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProductFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProductFragment extends Fragment {

    FragmentProductBinding binding;
    ShoppingInterface apiInterface;
    String strProductId= "";
    SuccessResGetProductDetail.Result productDetail;
    List<SuccessResGetCart.Result> cartItems  = new LinkedList<>();
    String quantity = "0";
    int productQunatity = 0;

    RelatedProductAdapter productsAdapter;
    List<SuccessResGetProductByCategory.Result> products  = new LinkedList<>();

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private int dotscount;
    private ImageView[] dots;
    private ImageAdapter imageAdapter;
    private List<SuccessResGetProductDetail.Image> images = new LinkedList<>();

    String getStrProductName = "",strCompanyId = "";

    public ProductFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProductFragment.
     */

    // TODO: Rename and change types and number of parameters
    public static ProductFragment newInstance(String param1, String param2) {
        ProductFragment fragment = new ProductFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_product, container, false);


        apiInterface = ApiClient.getClient().create(ShoppingInterface.class);
        Bundle bundle = this.getArguments();

        if (bundle!=null)
        {
            strProductId = bundle.getString("id");
        }


        getProduct();


        binding.layoutMyProfile.imgHeader.setOnClickListener(v -> {
            getActivity().onBackPressed();
        });

        binding.layoutMyProfile.tvHeader.setVisibility(View.GONE);

        apiInterface = ApiClient.getClient().create(ShoppingInterface.class);

        productsAdapter = new RelatedProductAdapter(getActivity(),products);

        binding.rvRelatedProducts.setHasFixedSize(true);

        binding.rvRelatedProducts.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));

        binding.rvRelatedProducts.setAdapter(productsAdapter);

        binding.btnCompare.setOnClickListener(v ->
                {
                    Bundle bundle1 = new Bundle();
                    bundle1.putString("name",getStrProductName);
                    Navigation.findNavController(v).navigate(R.id.action_productFragment_to_comparisonFragment,bundle1);
                }
                );
        return binding.getRoot();
    }

    private void getProduct() {
        DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));
        Map<String,String> map = new HashMap<>();
        map.put("id",strProductId);
        Call<SuccessResGetProductDetail> call = apiInterface.getProductByProductId(map);
        call.enqueue(new Callback<SuccessResGetProductDetail>() {
            @Override
            public void onResponse(Call<SuccessResGetProductDetail> call, Response<SuccessResGetProductDetail> response) {

                DataManager.getInstance().hideProgressMessage();
                try {
                    SuccessResGetProductDetail data = response.body();
                    Log.e("data",data.status);
                    if (data.status.equals("1")) {
                        String dataResponse = new Gson().toJson(response.body());
                        productDetail = data.getResult().get(0);
                        strCompanyId = productDetail.getCompanyId();

                        getCartItems();

//                        SessionManager.writeString(RegisterAct.this, Constant.driver_id,data.result.id);
//                        App.showToast(RegisterAct.this, data.message, Toast.LENGTH_SHORT);
                    } else if (data.status.equals("0")) {
                        showToast(getActivity(), data.message);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<SuccessResGetProductDetail> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }
        });
    }


    private void setDetail()
    {
        getStrProductName = productDetail.getName();

        binding.tvProduct.setText(productDetail.getName());
        binding.tvStoreName.setText(productDetail.getComapny());
        binding.tvPrice.setText("$"+productDetail.getPrice());
        binding.listitemrating.setRating(Float.parseFloat(productDetail.getRatting()));
        binding.tvRatting.setText(productDetail.getRatting());

        images.addAll(productDetail.getImage());
        if(images.size()>0)
        {

            imageAdapter = new ImageAdapter(getActivity(),images);
            binding.viewPage.setAdapter(imageAdapter);
            dotscount = imageAdapter.getCount();
            dots = new ImageView[dotscount];
            for(int i = 0; i < dotscount; i++){

                dots[i] = new ImageView(getActivity());
                dots[i].setImageDrawable(ContextCompat.getDrawable(getActivity().getApplicationContext(), R.drawable.non_active_dot));

                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

                params.setMargins(8, 0, 8, 0);

                binding.layoutBars.addView(dots[i], params);

            }

            dots[0].setImageDrawable(ContextCompat.getDrawable(getActivity().getApplicationContext(), R.drawable.active_dot));

            binding.viewPage.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {

                    for(int i = 0; i< dotscount; i++){
                        dots[i].setImageDrawable(ContextCompat.getDrawable(getActivity().getApplicationContext(), R.drawable.non_active_dot));
                    }

                    dots[position].setImageDrawable(ContextCompat.getDrawable(getActivity().getApplicationContext(), R.drawable.active_dot));

                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });
        }

        getRelatedProduct();

        binding.btnAddToCart.setOnClickListener(v ->
                {
                    binding.btnViewCart.setVisibility(View.VISIBLE);
                    binding.btnAddToCart.setVisibility(View.GONE);
                    binding.tvQuantity.setText("1");
                    addToCart(productDetail.getId(),productDetail.getPrice(),binding.tvQuantity.getText().toString());
                }
        );

        binding.btnViewCart.setOnClickListener(v ->
                {
                    navController.navigate(R.id.navigation_cart);
                }
        );


    }


    private void getRelatedProduct() {

        Map<String,String> map = new HashMap<>();
        map.put("company_id",strCompanyId);

        Call<SuccessResGetProductByCategory> call = apiInterface.getProductsByCompany(map);

        call.enqueue(new Callback<SuccessResGetProductByCategory>() {
            @Override
            public void onResponse(Call<SuccessResGetProductByCategory> call, Response<SuccessResGetProductByCategory> response) {

                DataManager.getInstance().hideProgressMessage();
                try {
                    SuccessResGetProductByCategory data = response.body();
                    Log.e("data",data.status);
                    if (data.status.equals("1")) {
                        String dataResponse = new Gson().toJson(response.body());
                        products.clear();
                        products.addAll(data.getResult());
                        productsAdapter.notifyDataSetChanged();

//                        SessionManager.writeString(RegisterAct.this, Constant.driver_id,data.result.id);
//                        App.showToast(RegisterAct.this, data.message, Toast.LENGTH_SHORT);

                    } else if (data.status.equals("0")) {
                        showToast(getActivity(), data.message);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<SuccessResGetProductByCategory> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }
        });
    }

    private void getCartItems()
    {
        DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));
        String userId = SharedPreferenceUtility.getInstance(getActivity()).getString(USER_ID);
        Map<String,String> map = new HashMap<>();
        map.put("user_id",userId);

        Call<SuccessResGetCart> call = apiInterface.getCartByUserId(map);

        call.enqueue(new Callback<SuccessResGetCart>() {
            @Override
            public void onResponse(Call<SuccessResGetCart> call, Response<SuccessResGetCart> response) {

                DataManager.getInstance().hideProgressMessage();
                try {
                    SuccessResGetCart data = response.body();
                    Log.e("data",data.status);
                    if (data.status.equals("1")) {
                        String dataResponse = new Gson().toJson(response.body());
                        cartItems.clear();
                        cartItems.addAll(data.getResult());
                        manageAddCartButton();
                        setDetail();

//                        SessionManager.writeString(RegisterAct.this, Constant.driver_id,data.result.id);
//                        App.showToast(RegisterAct.this, data.message, Toast.LENGTH_SHORT);

                    } else if (data.status.equals("0")) {
                        showToast(getActivity(), data.message);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<SuccessResGetCart> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }
        });

    }

    public void manageAddCartButton()
    {

        if(hasItemInCart(productDetail.getId()))

        {

            binding.btnAddToCart.setVisibility(View.GONE);
            binding.btnViewCart.setVisibility(View.VISIBLE);

        }

        binding.ivInc.setOnClickListener(v ->
                {
                    productQunatity++;

                    updateCart(productDetail.getId(),productDetail.getPrice(),""+productQunatity);

                    binding.tvQuantity.setText(""+productQunatity);
                }
                );

        binding.ivDec.setOnClickListener(v ->
                {

                    if(productQunatity==1 || productQunatity==0)
                    {
                        return;
                    }
                    productQunatity--;
                    updateCart(productDetail.getId(),productDetail.getPrice(),""+productQunatity);
                    binding.tvQuantity.setText(""+productQunatity);
                }
        );
    }

    private boolean hasItemInCart(String id)
    {
        for (SuccessResGetCart.Result item:cartItems)
        {

            if(item.getProductDetails().getId().equalsIgnoreCase(id))
            {
                binding.tvQuantity.setText(item.getQuntity());
                quantity = item.getQuntity();
                productQunatity= Integer.parseInt(quantity);
                return true;
            }

        }
        return false;

    }

    public void addToCart(String productId, String price, String quantitty) {

        String userId = SharedPreferenceUtility.getInstance(getActivity()).getString(USER_ID);

        DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));
        Map<String,String> map = new HashMap<>();
        map.put("product_id",productId);
        map.put("user_id",userId);
        map.put("price",price);
        map.put("quntity","1");

        Call<SuccessResAddToCart> call = apiInterface.addToCart(map);

        call.enqueue(new Callback<SuccessResAddToCart>() {
            @Override
            public void onResponse(Call<SuccessResAddToCart> call, Response<SuccessResAddToCart> response) {

                DataManager.getInstance().hideProgressMessage();
                try {
                    SuccessResAddToCart data = response.body();
                    Log.e("data",data.status);
                    if (data.status.equals("1")) {
                        String dataResponse = new Gson().toJson(response.body());

//                        SessionManager.writeString(RegisterAct.this, Constant.driver_id,data.result.id);
//                        App.showToast(RegisterAct.this, data.message, Toast.LENGTH_SHORT);

                    } else if (data.status.equals("0")) {
                        showToast(getActivity(), data.message);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<SuccessResAddToCart> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }
        });

    }



    public void updateCart(String productId, String price, String quantitty) {

        String userId = SharedPreferenceUtility.getInstance(getActivity()).getString(USER_ID);

        Map<String,String> map = new HashMap<>();
        map.put("product_id",productId);
        map.put("user_id",userId);
        map.put("quntity",quantitty);

        Call<SuccessResUpdateCart> call = apiInterface.updateCart(map);

        call.enqueue(new Callback<SuccessResUpdateCart>() {
            @Override
            public void onResponse(Call<SuccessResUpdateCart> call, Response<SuccessResUpdateCart> response) {

                DataManager.getInstance().hideProgressMessage();
                try {
                    SuccessResUpdateCart data = response.body();
                    Log.e("data",data.status);
                    if (data.status.equals("1")) {
                        String dataResponse = new Gson().toJson(response.body());

                    //    getCartItems();

//                        SessionManager.writeString(RegisterAct.this, Constant.driver_id,data.result.id);
//                        App.showToast(RegisterAct.this, data.message, Toast.LENGTH_SHORT);

                    } else if (data.status.equals("0")) {
                        showToast(getActivity(), data.message);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<SuccessResUpdateCart> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }
        });

    }

}