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
import android.view.animation.AnticipateOvershootInterpolator;

import com.easyshopping.R;
import com.easyshopping.adapter.AddressAdapter;
import com.easyshopping.databinding.FragmentShipToBinding;
import com.easyshopping.models.SuccessResGetAddresses;
import com.easyshopping.models.SuccessResUpdateCart;
import com.easyshopping.retrofit.ApiClient;
import com.easyshopping.retrofit.ShoppingInterface;
import com.easyshopping.util.DataManager;
import com.easyshopping.util.SharedPreferenceUtility;
import com.easyshopping.util.UpdateAndDeleteAddress;
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
 * Use the {@link ShipToFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ShipToFragment extends Fragment implements UpdateAndDeleteAddress {

    FragmentShipToBinding binding;

    ShoppingInterface apiInterface;

    private boolean fromWhere;

    String addressID = "";

    private int selectedAddress = -1;

    private boolean selected;

    String cartID = "";

    private String  id="",name = "",address="",phone = "",cc ="";

    List<SuccessResGetAddresses.Result> addressList = new LinkedList<>();

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ShipToFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ShipToFragment.
     */

    // TODO: Rename and change types and number of parameters

    public static ShipToFragment newInstance(String param1, String param2) {
        ShipToFragment fragment = new ShipToFragment();
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
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_ship_to, container, false);

        apiInterface = ApiClient.getClient().create(ShoppingInterface.class);
        selected = false;

        binding.layoutMyProfile.imgHeader.setOnClickListener(v -> {
            getActivity().onBackPressed();
        });

        binding.layoutMyProfile.tvHeader.setText(R.string.ship);

        Bundle bundle = this.getArguments();

        if (bundle!=null)
        {
            String from = bundle.getString("from");
            if(from.equalsIgnoreCase("profile"))
            {
                binding.btnNext.setVisibility(View.GONE);
                fromWhere = false;
                getAddress();
            }
            else
            {
                fromWhere = true;
                binding.btnNext.setVisibility(View.VISIBLE);
                cartID = bundle.getString("id",id);
                getAddress();

            }

        }

        binding.btnNext.setOnClickListener(v ->
                {
                    if (selected)
                    {
                        addressID = addressList.get(selectedAddress).getId();
                        Bundle bundle1 = new Bundle();
                        bundle1.putString("from","ShipTo");
                        bundle1.putString("id",addressID);
                        bundle1.putString("name",name);
                        bundle1.putString("address",address);
                        bundle1.putString("cc",cc);
                        bundle1.putString("phone",phone);
                        bundle1.putString("cartID",cartID);
                        Navigation.findNavController(v).navigate(R.id.action_shipToFragment_to_chooseCardFragment,bundle1);

                    }
                    else
                    {
                        showToast(getActivity(),"Please select address");
                    }
                }
                );

        binding.ivAdd.setOnClickListener(v ->
                {
                    Bundle bundle1 = new Bundle();
                    bundle1.putString("from","ShipToAdd");
                    Navigation.findNavController(v).navigate(R.id.action_shipToFragment_to_addAddressFragment,bundle1);
                }
                );

        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    private void getAddress()
    {

        String userId = SharedPreferenceUtility.getInstance(getActivity()).getString(USER_ID);
        DataManager.getInstance().showProgressMessage(getActivity(),getString(R.string.please_wait));

        Map<String,String> map = new HashMap<>();
        map.put("user_id",userId);

        Call<SuccessResGetAddresses> call = apiInterface.getUserAddress(map);

        call.enqueue(new Callback<SuccessResGetAddresses>() {
            @Override
            public void onResponse(Call<SuccessResGetAddresses> call, Response<SuccessResGetAddresses> response) {

                DataManager.getInstance().hideProgressMessage();
                try {
                    SuccessResGetAddresses data = response.body();
                    Log.e("data",data.status);
                    if (data.status.equals("1")) {
                        String dataResponse = new Gson().toJson(response.body());

                        addressList.clear();
                        addressList.addAll(data.getResult());

                        binding.rvAddress.setHasFixedSize(true);
                        binding.rvAddress.setLayoutManager(new LinearLayoutManager(getActivity()));
                        binding.rvAddress.setAdapter(new AddressAdapter(getActivity(),addressList,ShipToFragment.this,fromWhere));

//                        SessionManager.writeString(RegisterAct.this, Constant.driver_id,data.result.id);
//                        App.showToast(RegisterAct.this, data.message, Toast.LENGTH_SHORT);

                    } else if (data.status.equals("0")) {
                        showToast(getActivity(), data.message);
                        addressList.clear();
                        binding.rvAddress.setHasFixedSize(true);
                        binding.rvAddress.setLayoutManager(new LinearLayoutManager(getActivity()));
                        binding.rvAddress.setAdapter(new AddressAdapter(getActivity(),addressList,ShipToFragment.this,fromWhere));

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<SuccessResGetAddresses> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }
        });

    }


    @Override
    public void updateCart(View v,String name, String address, String id, String phone, String countrycode) {
        Bundle bundle = new Bundle();

        bundle.putString("from","ShipToEdit");
        bundle.putString("id",id);
        bundle.putString("name",name);
        bundle.putString("address",address);
        bundle.putString("cc",countrycode);
        bundle.putString("phone",phone);

        Navigation.findNavController(v).navigate(R.id.action_shipToFragment_to_addAddressFragment,bundle);
    }

    @Override
    public void deleteCart(String productId) {

        DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));
        Map<String,String> map = new HashMap<>();
        map.put("id",productId);

        Call<SuccessResUpdateCart> call = apiInterface.deleteAddress(map);

        call.enqueue(new Callback<SuccessResUpdateCart>() {
            @Override
            public void onResponse(Call<SuccessResUpdateCart> call, Response<SuccessResUpdateCart> response) {

                DataManager.getInstance().hideProgressMessage();
                try {
                    SuccessResUpdateCart data = response.body();
                    Log.e("data",data.status);
                    if (data.status.equals("1")) {
                        String dataResponse = new Gson().toJson(response.body());

                        getAddress();

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
    public void isSelected(boolean isSelected,int position) {

        selected = isSelected;
         selectedAddress = position;

    }
}