package com.easyshopping.fragments;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import com.easyshopping.R;
import com.easyshopping.adapter.CartAdapter;
import com.easyshopping.adapter.CartBottomItemAdapter;
import com.easyshopping.databinding.FragmentCartBinding;
import com.easyshopping.models.SuccessResGetCart;
import com.easyshopping.models.SuccessResUpdateCart;
import com.easyshopping.retrofit.ApiClient;
import com.easyshopping.retrofit.ShoppingInterface;
import com.easyshopping.util.DataManager;
import com.easyshopping.util.SharedPreferenceUtility;
import com.easyshopping.util.UpdateAndDeleteCart;
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
 * Use the {@link CartFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CartFragment extends Fragment implements UpdateAndDeleteCart {

    FragmentCartBinding binding;
    ShoppingInterface apiInterace;
    List<SuccessResGetCart.Result> cartItems  = new LinkedList<>();
    HashMap<String,Integer> cartMap = new HashMap<>();

    private String cartItemIDs = "";

    public static String deliveryType = "delivery";

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public CartFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CartFragment.
     */

    // TODO: Rename and change types and number of parameters
    public static CartFragment newInstance(String param1, String param2) {
        CartFragment fragment = new CartFragment();
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
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_cart, container, false);

        apiInterace = ApiClient.getClient().create(ShoppingInterface.class);

        getCartItems(true);

       int id =  binding.radioGroup.getCheckedRadioButtonId();

       binding.radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
           @Override
           public void onCheckedChanged(RadioGroup group, int checkedId) {

               if(checkedId==binding.radioDelivery.getId())
               {
                   deliveryType = "delivery";
               } else if(checkedId==binding.radioPickup.getId())
               {
                   deliveryType = "pickup";
               }

           }
       });


       binding.btnCheckout.setOnClickListener(v ->
                {
                    Bundle bundle = new Bundle();
                    bundle.putString("from","Cart");
                    bundle.putString("id","2");
                    Navigation.findNavController(v).navigate(R.id.action_navigation_cart_to_shipToFragment,bundle);
                }
        );

        binding.btnCompare.setOnClickListener(v ->
                {
                    Navigation.findNavController(v).navigate(R.id.action_navigation_cart_to_comparativeGrandTotalFragment);
                }
        );

        return binding.getRoot();

    }

    private void getCartItems(boolean is)
    {

        if(is)
        {
            DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));
        }

        String userId = SharedPreferenceUtility.getInstance(getActivity()).getString(USER_ID);
        Map<String,String> map = new HashMap<>();
        map.put("user_id",userId);

        Call<SuccessResGetCart> call = apiInterace.getCartByUserId(map);
        call.enqueue(new Callback<SuccessResGetCart>() {
            @Override
            public void onResponse(Call<SuccessResGetCart> call, Response<SuccessResGetCart> response) {

                DataManager.getInstance().hideProgressMessage();
                try {
                    SuccessResGetCart data = response.body();
                    Log.e("data",data.status);
                    if (data.status.equals("1")) {
                        String dataResponse = new Gson().toJson(response.body());


                        binding.tvTotalPrice.setText(data.totalPrice+"");

                        String dataSHip  = data.shippingCharges;

                        binding.tvShipping.setText(data.shippingCharges);
                        binding.tvImport.setText(data.importCharges);

                        cartItems.clear();
                        cartItems.addAll(data.getResult());

                        binding.llAvailCart.setVisibility(View.VISIBLE);
                        binding.llBottom.setVisibility(View.VISIBLE);
                        binding.ivNocart.setVisibility(View.GONE);

                        setCartList();

                        binding.rvCartItems.setHasFixedSize(true);
                        binding.rvCartItems.setLayoutManager(new LinearLayoutManager(getActivity()));
                        binding.rvCartItems.setAdapter(new CartAdapter(getActivity(),cartItems,cartMap,CartFragment.this));

                        binding.rvCartBottom.setHasFixedSize(true);
                        binding.rvCartBottom.setLayoutManager(new LinearLayoutManager(getActivity()));
                        binding.rvCartBottom.setAdapter(new CartBottomItemAdapter(getActivity(),cartItems));


                        //    manageAddCartButton();
                      //  setDetail();

//                        SessionManager.writeString(RegisterAct.this, Constant.driver_id,data.result.id);
//                        App.showToast(RegisterAct.this, data.message, Toast.LENGTH_SHORT);

                    } else if (data.status.equals("0")) {
                        showToast(getActivity(), data.message);
                        binding.tvTotalPrice.setText("");
                        binding.llAvailCart.setVisibility(View.GONE);
                        binding.llBottom.setVisibility(View.GONE);
                        binding.ivNocart.setVisibility(View.VISIBLE);
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

    @Override
    public void updateCart(String productId, String userId, String quantity) {

        String userID = SharedPreferenceUtility.getInstance(getActivity()).getString(USER_ID);

        Map<String,String> map = new HashMap<>();
        map.put("product_id",productId);
        map.put("user_id",userID);
        map.put("quntity",quantity);

        Call<SuccessResUpdateCart> call = apiInterace.updateCart(map);

        call.enqueue(new Callback<SuccessResUpdateCart>() {
            @Override
            public void onResponse(Call<SuccessResUpdateCart> call, Response<SuccessResUpdateCart> response) {

                DataManager.getInstance().hideProgressMessage();
                try {
                    SuccessResUpdateCart data = response.body();
                    Log.e("data",data.status);
                    if (data.status.equals("1")) {
                        String dataResponse = new Gson().toJson(response.body());

                            getCartItems(false);

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

    @Override
    public void deleteCart(String productId) {

        Map<String,String> map = new HashMap<>();
        map.put("id",productId);

        Call<SuccessResUpdateCart> call = apiInterace.deleteCart(map);

        call.enqueue(new Callback<SuccessResUpdateCart>() {
            @Override
            public void onResponse(Call<SuccessResUpdateCart> call, Response<SuccessResUpdateCart> response) {

                DataManager.getInstance().hideProgressMessage();
                try {
                    SuccessResUpdateCart data = response.body();
                    Log.e("data",data.status);
                    if (data.status.equals("1")) {
                        String dataResponse = new Gson().toJson(response.body());

                        getCartItems(false);

//                        SessionManager.writeString(RegisterAct.this, Constant.driver_id,data.result.id);
//                        App.showToast(RegisterAct.this, data.message, Toast.LENGTH_SHORT);

                    } else if (data.status.equals("0")) {
                        showToast(getActivity(), data.message);
                        getCartItems(false);

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

    public void setCartList()
    {

        for(SuccessResGetCart.Result cart:cartItems)
        {
            Integer quantity = Integer.parseInt(cart.getQuntity());
            cartMap.put(cart.getId(),quantity);
        }

    }

}