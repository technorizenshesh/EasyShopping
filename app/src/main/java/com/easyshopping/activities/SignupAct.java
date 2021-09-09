package com.easyshopping.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.easyshopping.R;
import com.easyshopping.databinding.ActivitySignupBinding;
import com.easyshopping.models.SuccessResSignUp;
import com.easyshopping.retrofit.ApiClient;
import com.easyshopping.retrofit.ShoppingInterface;
import com.easyshopping.util.DataManager;
import com.easyshopping.util.NetworkAvailablity;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.easyshopping.retrofit.Constant.isValidEmail;
import static com.easyshopping.retrofit.Constant.showToast;

public class SignupAct extends AppCompatActivity {

    ActivitySignupBinding binding;
    ShoppingInterface apiInterface;
    String strName = "", strEmail = "", strPass = "" ,strConfirmPass="",strPhone = "",strcc = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_signup);

        binding.btnSignup.setOnClickListener(v ->
                {
                    startActivity(new Intent(SignupAct.this,LoginAct.class));
                }
        );

        binding.tvAlreayHave.setOnClickListener(v ->
                {
                    startActivity(new Intent(SignupAct.this,LoginAct.class));
                }
        );

        apiInterface = ApiClient.getClient().create(ShoppingInterface.class);
/*
        binding.tvAlreadyHave.setOnClickListener(v ->
                {
                    startActivity(new Intent(SignupAct.this,LoginActivity.class));
                }
                );*/

       /* binding.rlHaveAcc.setOnClickListener(v ->
                 {
                     Intent i = new Intent(SignupAct.this, LoginActivity.class);
                     SignupAct.this.startActivity(i);
                 }
                 );*/

        binding.btnSignup.setOnClickListener(v ->
                {
                    strName = binding.etName.getText().toString().trim();
                    strEmail = binding.etEmail.getText().toString().trim();
                    strPass = binding.etPass.getText().toString().trim();
                    strcc = binding.ccp.getSelectedCountryCode();
                    strPhone = binding.etNumber.getText().toString().trim();
                    strConfirmPass = binding.etConPass.getText().toString().trim();

                    if (isValid()) {

                        if (NetworkAvailablity.getInstance(this).checkNetworkStatus()) {

                            signup();

                        } else {
                            Toast.makeText(this, getResources().getString(R.string.msg_noInternet), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(this, getResources().getString(R.string.on_error), Toast.LENGTH_SHORT).show();

                    }

                }
        );

    }


    public void signup() {

//        strLat ="22.698986";
//        strLng = "75.867851";

        DataManager.getInstance().showProgressMessage(SignupAct.this, getString(R.string.please_wait));
        Map<String,String> map = new HashMap<>();
        map.put("first_name",strName);
        map.put("email",strEmail);
        map.put("lat","");
        map.put("lon","");
        map.put("password",strPass);
        map.put("mobile",strPhone);
        map.put("country_code",strcc);
        map.put("register_id","");
        map.put("type","USER");


        Call<SuccessResSignUp> signupCall = apiInterface.signup(map);
        signupCall.enqueue(new Callback<SuccessResSignUp>() {
            @Override
            public void onResponse(Call<SuccessResSignUp> call, Response<SuccessResSignUp> response) {
                DataManager.getInstance().hideProgressMessage();
                try {
                    SuccessResSignUp data = response.body();
                    if (data.status.equals("1")) {
                        showToast(SignupAct.this, data.message);
                        String dataResponse = new Gson().toJson(response.body());
                        Log.e("MapMap", "EDIT PROFILE RESPONSE" + dataResponse);

                        //   mobileVerify();
//                        SessionManager.writeString(RegisterAct.this, Constant.driver_id,data.result.id);
//                        App.showToast(RegisterAct.this, data.message, Toast.LENGTH_SHORT);
                        startActivity(new Intent(SignupAct.this, LoginAct.class));
                        // finish();
                    } else if (data.status.equals("0")) {
                        showToast(SignupAct.this, data.message);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<SuccessResSignUp> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }

        });
    }
/*

    public void mobileVerify()
    {

        DataManager.getInstance().showProgressMessage(SignupAct.this, getString(R.string.please_wait));
        Map<String,String> map = new HashMap<>();
        map.put("mobile",strPhone);

        Call<SuccessResMobileVerify> signupCall = apiInterface.mobileVerify(map);
        signupCall.enqueue(new Callback<SuccessResMobileVerify>() {
            @Override
            public void onResponse(Call<SuccessResMobileVerify> call, Response<SuccessResMobileVerify> response) {
                DataManager.getInstance().hideProgressMessage();
                try {
                    SuccessResMobileVerify data = response.body();
                    if (data.status.equals("1")) {
                        String dataResponse = new Gson().toJson(response.body());
                        Log.e("MapMap", "EDIT PROFILE RESPONSE" + dataResponse);

//                        mobileVerify();
//                        SessionManager.writeString(RegisterAct.this, Constant.driver_id,data.result.id);
//                        App.showToast(RegisterAct.this, data.message, Toast.LENGTH_SHORT);
                        startActivity(new Intent(SignupAct.this,OTPActivity.class));
                      //  finish();
                    } else if (data.status.equals("0")) {
                        showToast(SignupAct.this, data.message);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<SuccessResMobileVerify> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }

        });

    }
*/

    private boolean isValid() {
        if (strName.equalsIgnoreCase("")) {
            binding.etName.setError(getString(R.string.enter_name));
            return false;
        } else if (strEmail.equalsIgnoreCase("")) {
            binding.etEmail.setError(getString(R.string.enter_email));
            return false;
        }  else if (!isValidEmail(strEmail)) {
            binding.etEmail.setError(getString(R.string.enter_valid_email));
            return false;
        }else if (strPhone.equalsIgnoreCase("")) {
            binding.etNumber.setError(getString(R.string.please_enter_pass));
            return false;
        }else if (strPass.equalsIgnoreCase("")) {
            binding.etPass.setError(getString(R.string.please_enter_pass));
            return false;
        } else if (strConfirmPass.equalsIgnoreCase("")) {
            binding.etConPass.setError(getString(R.string.please_enter_con_pass));
            return false;
        } else if (!strPass.equalsIgnoreCase(strConfirmPass)) {
            binding.etConPass.setError(getString(R.string.pass_and_confirm_pass_not_macthed));
            return false;
        }
        return true;
    }


}