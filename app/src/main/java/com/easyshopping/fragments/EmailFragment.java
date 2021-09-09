package com.easyshopping.fragments;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.easyshopping.R;
import com.easyshopping.databinding.FragmentEmailBinding;
import com.easyshopping.models.SuccessResGetProfile;
import com.easyshopping.models.SuccessResUpdateName;
import com.easyshopping.retrofit.ApiClient;
import com.easyshopping.retrofit.ShoppingInterface;
import com.easyshopping.util.DataManager;
import com.easyshopping.util.NetworkAvailablity;
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
 * Use the {@link EmailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EmailFragment extends Fragment {

    FragmentEmailBinding binding;
    String strEmail = "";
    ShoppingInterface apiInterface;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public EmailFragment() {
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
    public static EmailFragment newInstance(String param1, String param2) {
        EmailFragment fragment = new EmailFragment();
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
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_email, container, false);

        binding.layoutMyProfile.imgHeader.setOnClickListener(v -> {
            getActivity().onBackPressed();
        });


        binding.layoutMyProfile.tvHeader.setText(R.string.email);

        Bundle bundle = this.getArguments();

        if (bundle!=null)
        {
            String name = bundle.getString("name");
            binding.etEmail.setText(name);
        }

        apiInterface = ApiClient.getClient().create(ShoppingInterface.class);

        binding.btnLogin.setOnClickListener(v ->
                {

                    strEmail = binding.etEmail.getText().toString().trim();

                    if(isValid())
                    {

                        if (NetworkAvailablity.getInstance(getActivity()).checkNetworkStatus()) {
                            updateEmail(strEmail);
                        } else {
                            Toast.makeText(getActivity(), getResources().getString(R.string.msg_noInternet), Toast.LENGTH_SHORT).show();
                        }
                    }else {
                        Toast.makeText(getActivity(), getResources().getString(R.string.on_error), Toast.LENGTH_SHORT).show();
                    }
                }
        );

        binding.etEmail.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus)
                {
                    binding.rlMail.setBackgroundResource(R.drawable.dark_blue_border);
                }
                else
                {
                    binding.rlMail.setBackgroundResource(R.drawable.light_gray_border);

                }
            }
        });

        return binding.getRoot();
    }


    public void updateEmail(String email)
    {

        String strUserId = SharedPreferenceUtility.getInstance(getContext()).getString(USER_ID);
        DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));
        Map<String,String> map = new HashMap<>();
        map.put("user_id",strUserId);
        map.put("email",email);

        Call<SuccessResUpdateName> call = apiInterface.updateEmail(map);

        call.enqueue(new Callback<SuccessResUpdateName>() {
            @Override
            public void onResponse(Call<SuccessResUpdateName> call, Response<SuccessResUpdateName> response) {

                DataManager.getInstance().hideProgressMessage();
                try {
                    SuccessResUpdateName data = response.body();
             //       getProfile();
//                    sellerDetail = data.getResult();
//                    setSellerData();
                    Log.e("data",data.status);
                    if (data.status.equals("1")) {
                        showToast(getActivity(), data.message);
                        String dataResponse = new Gson().toJson(response.body());
                        Log.e("MapMap", "EDIT PROFILE RESPONSE" + dataResponse);
                        SuccessResUpdateName.Result userDetail  = data.getResult();
                        binding.etEmail.setText(userDetail.getEmail());
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
                        binding.etEmail.setText(userDetail.getEmail());
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
        if (strEmail.equalsIgnoreCase("")) {
            binding.etEmail.setError("Please enter email.");
            return false;
        } else if (!isValidEmail(strEmail)) {
            binding.etEmail.setError("Please enter valid email.");
            return false;
        }
        return true;
    }


}