package com.easyshopping.fragments;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.easyshopping.R;
import com.easyshopping.adapter.ComparisonAdapter;
import com.easyshopping.adapter.ProductsAdapter;
import com.easyshopping.databinding.FragmentComparisonBinding;
import com.easyshopping.models.SuccessResAddToCart;
import com.easyshopping.models.SuccessResCompare;
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
 * Use the {@link ComparisonFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ComparisonFragment extends Fragment implements MyClick {

    FragmentComparisonBinding binding;
    String strProductName = "";
    private ShoppingInterface apiInterface;
    private List<SuccessResCompare.Result> products = new LinkedList<>();

    private ComparisonAdapter comparisonAdapter;
    List<SuccessResGetCart.Result> cartItems  = new LinkedList<>();

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ComparisonFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ComparisonFragment.
     */


    // TODO: Rename and change types and number of parameters
    public static ComparisonFragment newInstance(String param1, String param2) {
        ComparisonFragment fragment = new ComparisonFragment();
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
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_comparison, container, false);

        apiInterface = ApiClient.getClient().create(ShoppingInterface.class);

        Bundle bundle = this.getArguments();

        if (bundle!=null)
        {
            strProductName = bundle.getString("name");
        }


        getProducts();


        binding.layoutMyProfile.imgHeader.setOnClickListener(v -> {
            getActivity().onBackPressed();
        });

        binding.layoutMyProfile.tvHeader.setText(R.string.comparison);

        return binding.getRoot();
    }

    private void getProducts() {

        DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));
        Map<String,String> map = new HashMap<>();
        map.put("p_name",strProductName);

        Call<SuccessResCompare> call = apiInterface.getProductCompare(map);

        call.enqueue(new Callback<SuccessResCompare>() {
            @Override
            public void onResponse(Call<SuccessResCompare> call, Response<SuccessResCompare> response) {

                DataManager.getInstance().hideProgressMessage();
                try {
                    SuccessResCompare data = response.body();
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
            public void onFailure(Call<SuccessResCompare> call, Throwable t) {
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
                        comparisonAdapter = new ComparisonAdapter(getActivity(),products,ComparisonFragment.this::getClick,cartItems);
                        binding.rvProducts.setHasFixedSize(true);
                        binding.rvProducts.setLayoutManager(new GridLayoutManager(getActivity(),2));
                        binding.rvProducts.setAdapter(comparisonAdapter);

//                        SessionManager.writeString(RegisterAct.this, Constant.driver_id,data.result.id);
//                        App.showToast(RegisterAct.this, data.message, Toast.LENGTH_SHORT);

                    } else if (data.status.equals("0")) {
                        showToast(getActivity(), data.message);

                        comparisonAdapter = new ComparisonAdapter(getActivity(),products,ComparisonFragment.this::getClick,cartItems);
                        binding.rvProducts.setHasFixedSize(true);
                        binding.rvProducts.setLayoutManager(new GridLayoutManager(getActivity(),2));
                        binding.rvProducts.setAdapter(comparisonAdapter);

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