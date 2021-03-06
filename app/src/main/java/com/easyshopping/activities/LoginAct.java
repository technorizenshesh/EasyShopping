package com.easyshopping.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.easyshopping.R;
import com.easyshopping.databinding.ActivityLoginBinding;
import com.easyshopping.fragments.HomeFragment;
import com.easyshopping.models.SuccessResSignIn;
import com.easyshopping.retrofit.ApiClient;
import com.easyshopping.retrofit.Constant;
import com.easyshopping.retrofit.ShoppingInterface;
import com.easyshopping.util.DataManager;
import com.easyshopping.util.NetworkAvailablity;
import com.easyshopping.util.SharedPreferenceUtility;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.easyshopping.retrofit.Constant.isValidEmail;
import static com.easyshopping.retrofit.Constant.showToast;

public class LoginAct extends AppCompatActivity {

    ShoppingInterface apiInterface;
    public static String TAG = "LoginActivity";

    private ActivityLoginBinding binding;
    private String strEmail ="",strPassword= "",deviceToken = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_login);

        apiInterface = ApiClient.getClient().create(ShoppingInterface.class);

        getToken();

        binding.tvForgot.setOnClickListener(v ->
                {
                    startActivity(new Intent(LoginAct.this,ForgotActivity.class));
                }
                );

        binding.btnLogin.setOnClickListener(v -> {

            strEmail = binding.etEmail.getText().toString().trim();
            strPassword = binding.etPass.getText().toString().trim();

            if(isValid())
            {

                if (NetworkAvailablity.getInstance(this).checkNetworkStatus()) {

                    login();

                } else {
                    Toast.makeText(this, getResources().getString(R.string.msg_noInternet), Toast.LENGTH_SHORT).show();
                }
            }else {
                Toast.makeText(this,  getResources().getString(R.string.on_error), Toast.LENGTH_SHORT).show();
            }

        });

        binding.tvDontHv.setOnClickListener(v ->
                {
                    startActivity(new Intent(LoginAct.this,SignupAct.class));
                }
        );

    }

    private void login() {

        DataManager.getInstance().showProgressMessage(LoginAct.this, getString(R.string.please_wait));
        Map<String,String> map = new HashMap<>();
        map.put("email",strEmail);
        map.put("password",strPassword);
        map.put("register_id",deviceToken);
      /*  RequestBody email = RequestBody.create(MediaType.parse("text/plain"),strEmail);
        RequestBody password = RequestBody.create(MediaType.parse("text/plain"), strPassword);
        RequestBody registerID = RequestBody.create(MediaType.parse("text/plain"),deviceToken);
*/
//        Call<SuccessResSignIn> call = apiInterface.login(email,password,registerID);
        Call<SuccessResSignIn> call = apiInterface.login(map);

        call.enqueue(new Callback<SuccessResSignIn>() {
            @Override
            public void onResponse(Call<SuccessResSignIn> call, Response<SuccessResSignIn> response) {

                DataManager.getInstance().hideProgressMessage();
                try {
                    SuccessResSignIn data = response.body();
                    Log.e("data",data.status);
                    if (data.status.equals("1")) {
                        String dataResponse = new Gson().toJson(response.body());
                        Log.e("MapMap", "EDIT PROFILE RESPONSE" + dataResponse);
                        SharedPreferenceUtility.getInstance(getApplication()).putBoolean(Constant.IS_USER_LOGGED_IN, true);
                        SharedPreferenceUtility.getInstance(LoginAct.this).putString(Constant.USER_ID,data.getResult().getId());
                        Toast.makeText(LoginAct.this,"Logged in successfully",Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(LoginAct.this, HomeActivity.class));
//                        SessionManager.writeString(RegisterAct.this, Constant.driver_id,data.result.id);
//                        App.showToast(RegisterAct.this, data.message, Toast.LENGTH_SHORT);
                        finish();
                    } else if (data.status.equals("0")) {
                        showToast(LoginAct.this, data.message);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<SuccessResSignIn> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }
        });
    }

    private boolean isValid() {
        if (strEmail.equalsIgnoreCase("")) {
            binding.etEmail.setError(getString(R.string.enter_email));
            return false;
        } else if (!isValidEmail(strEmail)) {
            binding.etEmail.setError(getString(R.string.enter_valid_email));
            return false;
        }else if (strPassword.equalsIgnoreCase("")) {
            binding.etPass.setError(getString(R.string.please_enter_pass));
            return false;
        }
        return true;
    }

    /**
     * This method is used to get fcm token
     */

    private void getToken() {
        try {
            FirebaseMessaging.getInstance().getToken()
                    .addOnCompleteListener(new OnCompleteListener<String>() {
                        @Override
                        public void onComplete(@NonNull Task<String> task) {
                            if (!task.isSuccessful()) {
                                Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                                return;
                            }
                            // Get new FCM registration token
                            String token = task.getResult();
                            deviceToken = token;
                        }
                    });
        } catch (Exception e) {
            Toast.makeText(LoginAct.this, "Error=>" + e, Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }
}