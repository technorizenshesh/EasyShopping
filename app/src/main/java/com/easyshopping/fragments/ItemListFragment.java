package com.easyshopping.fragments;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;

import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.easyshopping.R;
import com.easyshopping.activities.HomeActivity;
import com.easyshopping.adapter.ProductsAdapter;
import com.easyshopping.databinding.FragmentItemListBinding;
import com.easyshopping.models.SuccessResAddToCart;
import com.easyshopping.models.SuccessResGetCart;
import com.easyshopping.models.SuccessResGetProductByCategory;
import com.easyshopping.retrofit.ApiClient;
import com.easyshopping.retrofit.ShoppingInterface;
import com.easyshopping.util.DataManager;
import com.easyshopping.util.MyClick;
import com.easyshopping.util.SharedPreferenceUtility;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.easyshopping.retrofit.Constant.USER_ID;
import static com.easyshopping.retrofit.Constant.showToast;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ItemListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ItemListFragment extends Fragment implements MyClick {


    FragmentItemListBinding binding;
    ShoppingInterface apiInterface;
    String categoryId="";
    ProductsAdapter productsAdapter;
    List<SuccessResGetProductByCategory.Result> products  = new LinkedList<>();
    List<SuccessResGetCart.Result> cartItems  = new LinkedList<>();

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ItemListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ItemListFragment.
     */

    // TODO: Rename and change types and number of parameters
    public static ItemListFragment newInstance(String param1, String param2) {
        ItemListFragment fragment = new ItemListFragment();
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

        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_item_list, container, false);
        apiInterface = ApiClient.getClient().create(ShoppingInterface.class);

        Bundle bundle = this.getArguments();

        if (bundle!=null)
        {
            categoryId = bundle.getString("companyId");
        }

        getProducts();

        binding.etSearch.setInputType(InputType.TYPE_NULL);
        binding.etSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // showMyDialog();

                Bundle bundle = new Bundle();
                bundle.putString("companyId",categoryId);
                Navigation.findNavController(v).navigate(R.id.action_itemListFragment_to_searchVendorItemFragment,bundle);

            }
        });
        binding.etSearch.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    // showMyDialog();

                    Bundle bundle = new Bundle();
                    bundle.putString("companyId",categoryId);
                    Navigation.findNavController(v).navigate(R.id.action_itemListFragment_to_searchVendorItemFragment,bundle);

                }
            }
        });

        return binding.getRoot();

    }

    private void getProducts() {

        DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));
        Map<String,String> map = new HashMap<>();
        map.put("company_id",categoryId);

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
            public void onFailure(Call<SuccessResGetProductByCategory> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }
        });
    }

    @Override
    public void getClick(String companyId,String productId, String price, String quantitty) {

        String userId = SharedPreferenceUtility.getInstance(getActivity()).getString(USER_ID);

        DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));
        Map<String,String> map = new HashMap<>();
        map.put("product_id",productId);
        map.put("company_id",companyId);
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

    private void getCartItems()
    {

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
                        binding.rvProducts.setHasFixedSize(true);
                        binding.rvProducts.setLayoutManager(new GridLayoutManager(getActivity(),2));
                        productsAdapter = new ProductsAdapter(getActivity(),products,ItemListFragment.this::getClick,cartItems);
                        binding.rvProducts.setAdapter(productsAdapter);


//                        SessionManager.writeString(RegisterAct.this, Constant.driver_id,data.result.id);
//                        App.showToast(RegisterAct.this, data.message, Toast.LENGTH_SHORT);

                    } else if (data.status.equals("0")) {
                        showToast(getActivity(), data.message);

                        binding.rvProducts.setHasFixedSize(true);
                        binding.rvProducts.setLayoutManager(new GridLayoutManager(getActivity(),2));
                        productsAdapter = new ProductsAdapter(getActivity(),products,ItemListFragment.this::getClick,cartItems);
                        binding.rvProducts.setAdapter(productsAdapter);

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

}