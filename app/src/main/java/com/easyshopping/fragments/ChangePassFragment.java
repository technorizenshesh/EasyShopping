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
import com.easyshopping.databinding.FragmentChangePassBinding;
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
import static com.easyshopping.retrofit.Constant.showToast;


public class ChangePassFragment extends Fragment {

    FragmentChangePassBinding binding;
    com.easyshopping.retrofit.ShoppingInterface shoppingInterface;

    String oldPass = "",newConfirmPass = "" ,newPass = "", pass = "";

    public ChangePassFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_change_pass, container, false);

        shoppingInterface = ApiClient.getClient().create(ShoppingInterface.class);
        binding.layoutMyProfile.imgHeader.setOnClickListener(v -> {
            getActivity().onBackPressed();
        });
        binding.layoutMyProfile.tvHeader.setText(R.string.change_pass);

        Bundle bundle = this.getArguments();

        if (bundle!=null)
        {
            pass = bundle.getString("pass");
        }

        binding.btnLogin.setOnClickListener(v ->
                {

                    oldPass = binding.etPass.getText().toString().trim();
                    newPass = binding.etNewPass.getText().toString().trim();
                    newConfirmPass = binding.etNewConPass.getText().toString().trim();

                    if(isValid())
                    {

                        if (NetworkAvailablity.getInstance(getActivity()).checkNetworkStatus()) {

                            changePassword();

                        } else {
                            Toast.makeText(getActivity(), getResources().getString(R.string.msg_noInternet), Toast.LENGTH_SHORT).show();
                        }
                    }else {
                        Toast.makeText(getActivity(), getResources().getString(R.string.on_error), Toast.LENGTH_SHORT).show();
                    }

                }
        );

        return binding.getRoot();
    }


    private boolean isValid() {
        if (oldPass.equalsIgnoreCase("")) {
            binding.etPass.setError(getString(R.string.please_enter_old_pass));
            return false;
        }else if (newPass.equalsIgnoreCase("")) {
            binding.etNewPass.setError(getString(R.string.enter_new_password));
            return false;
        } else if (newConfirmPass.equalsIgnoreCase("")) {
            binding.etPass.setError(getString(R.string.please_enter_confirm_password));
            return false;
        }else if (!newConfirmPass.equalsIgnoreCase(newPass)) {
            binding.etNewConPass.setError(getString(R.string.password_mismatched));
            return false;
        }else if (!oldPass.equalsIgnoreCase(pass)) {
            binding.etPass.setError(getString(R.string.old_password_mismatched));
            return false;
        }
        return true;
    }


    public void changePassword()

    {

        String userId = SharedPreferenceUtility.getInstance(getActivity()).getString(USER_ID);
        DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));
        Map<String,String> map = new HashMap<>();

        map.put("user_id",userId);
        map.put("password",newPass);

        Call<SuccessResUpdateName> call = shoppingInterface.changePass(map);

        call.enqueue(new Callback<SuccessResUpdateName>() {
            @Override
            public void onResponse(Call<SuccessResUpdateName> call, Response<SuccessResUpdateName> response) {

                DataManager.getInstance().hideProgressMessage();
                try {
                    SuccessResUpdateName data = response.body();
                    Log.e("data",data.status);
                    if (data.status.equals("1")) {
                        showToast(getActivity(), data.message);
                        String dataResponse = new Gson().toJson(response.body());

                        binding.etNewPass.setText("");
                        binding.etPass.setText("");
                        binding.etNewConPass.setText("");


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

}