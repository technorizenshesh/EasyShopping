package com.easyshopping.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.easyshopping.R;
import com.easyshopping.databinding.FragmentEmailBinding;
import com.easyshopping.databinding.FragmentPhoneBinding;
import com.easyshopping.models.SuccessResGetProfile;
import com.easyshopping.models.SuccessResUpdateName;
import com.easyshopping.retrofit.ApiClient;
import com.easyshopping.retrofit.ShoppingInterface;
import com.easyshopping.util.DataManager;
import com.easyshopping.util.SharedPreferenceUtility;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.easyshopping.retrofit.Constant.USER_ID;
import static com.easyshopping.retrofit.Constant.isValidEmail;
import static com.easyshopping.retrofit.Constant.showToast;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PhoneFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PhoneFragment extends Fragment {

    FragmentPhoneBinding binding;

    private String strPhone = "";
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    ShoppingInterface apiInterface;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public PhoneFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EmailFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PhoneFragment newInstance(String param1, String param2) {
        PhoneFragment fragment = new PhoneFragment();
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
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_phone, container, false);

        binding.layoutMyProfile.imgHeader.setOnClickListener(v -> {
            getActivity().onBackPressed();
        });

        binding.layoutMyProfile.tvHeader.setText(R.string.ph_num);

        apiInterface = ApiClient.getClient().create(ShoppingInterface.class);

        Bundle bundle = this.getArguments();

        if (bundle!=null)
        {
            String mobile = bundle.getString("mobile");
            String cc = bundle.getString("cc");
            binding.etNumber.setText(mobile);
            binding.ccp.setCountryForPhoneCode(Integer.parseInt(cc));

        }

        binding.btnLogin.setOnClickListener(v ->

                {
                    if(!binding.etNumber.getText().toString().equalsIgnoreCase(""))
                    {
                        updateNumber(binding.etNumber.getText().toString());
                    }
                    else
                    {
                        binding.etNumber.setError(getString(R.string.please_enter_pass));
                    }
                }
                );

        return binding.getRoot();
    }

    public void updateNumber(String number)
    {

        String strUserId = SharedPreferenceUtility.getInstance(getContext()).getString(USER_ID);
        DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));
        Map<String,String> map = new HashMap<>();
        map.put("user_id",strUserId);
        map.put("mobile",number);
        map.put("country_code",binding.ccp.getSelectedCountryCode());

        Call<SuccessResUpdateName> call = apiInterface.updateNumber(map);

        call.enqueue(new Callback<SuccessResUpdateName>() {
            @Override
            public void onResponse(Call<SuccessResUpdateName> call, Response<SuccessResUpdateName> response) {

                DataManager.getInstance().hideProgressMessage();
                try {
                    SuccessResUpdateName data = response.body();
                 //   getProfile();
//                    sellerDetail = data.getResult();
//                    setSellerData();
                    Log.e("data",data.status);
                    if (data.status.equals("1")) {
                        showToast(getActivity(), data.message);
                        String dataResponse = new Gson().toJson(response.body());
                        Log.e("MapMap", "EDIT PROFILE RESPONSE" + dataResponse);

                        SuccessResUpdateName.Result userDetail  = data.getResult();
                        binding.etNumber.setText(userDetail.getMobile());
                        binding.ccp.setCountryForPhoneCode(Integer.parseInt(userDetail.getCountryCode()));

                        //   getProfile();
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
            public void onFailure(Call<SuccessResUpdateName> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }
        });
    }


    private void getProfile()

    {

        String userId = SharedPreferenceUtility.getInstance(getActivity()).getString(USER_ID);

        DataManager.getInstance().showProgressMessage(getActivity(),getString(R.string.please_wait));
        Map<String,String> map=new HashMap<>();
        map.put("user_id",userId);

        Call<SuccessResGetProfile> call = apiInterface.getProfile(map);
        call.enqueue(new Callback<SuccessResGetProfile>() {
            @Override
            public void onResponse(Call<SuccessResGetProfile> call, Response<SuccessResGetProfile> response) {

                DataManager.getInstance().hideProgressMessage();

                try {


                    SuccessResGetProfile data= response.body();
                    if(data.status.equals("1"))
                    {
                        SuccessResGetProfile.Result userDetail  = data.getResult();
                        binding.etNumber.setText(userDetail.getEmail());
                        binding.ccp.setCountryForPhoneCode(Integer.parseInt(userDetail.getCountryCode()));
                    }

                    else
                    {
                        showToast(getActivity(), data.message);
                    }

                }catch (Exception e)
                {
                    e.printStackTrace();
                }


            }

            @Override
            public void onFailure(Call<SuccessResGetProfile> call, Throwable t) {

                call.cancel();
                DataManager.getInstance().hideProgressMessage();

            }
        });

    }



    private boolean isValid() {
        if (strPhone.equalsIgnoreCase("")) {
            binding.etNumber.setError(getString(R.string.enter_number));
            return false;
        }
        return true;
    }



}