package com.easyshopping.fragments;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.easyshopping.R;
import com.easyshopping.databinding.FragmentMyProfileBinding;
import com.easyshopping.models.SuccessResGetProfile;
import com.easyshopping.retrofit.ApiClient;
import com.easyshopping.retrofit.ShoppingInterface;
import com.easyshopping.util.DataManager;
import com.easyshopping.util.SharedPreferenceUtility;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.easyshopping.retrofit.Constant.USER_ID;
import static com.easyshopping.retrofit.Constant.showToast;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MyProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyProfileFragment extends Fragment {

    FragmentMyProfileBinding binding;

    ShoppingInterface apiInterface;


    SuccessResGetProfile.Result userDetail = null;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MyProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MyProfileFragment.
     */

    // TODO: Rename and change types and number of parameters
    public static MyProfileFragment newInstance(String param1, String param2) {
        MyProfileFragment fragment = new MyProfileFragment();
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

        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_my_profile, container, false);

        binding.layoutMyProfile.imgHeader.setOnClickListener(v -> {
            getActivity().onBackPressed();
        });

        binding.layoutMyProfile.tvHeader.setText(R.string.profile);
        apiInterface = ApiClient.getClient().create(ShoppingInterface.class);

        getProfile();

        Bundle bundle = new Bundle();

        binding.rlName.setOnClickListener(v ->
                {

                    bundle.putString("name",userDetail.getFirstName());
                    Navigation.findNavController(v).navigate(R.id.action_myProfileFragment_to_nameFragment,bundle);

                }
        );

        binding.rlEMail.setOnClickListener(v ->
                {

                    bundle.putString("name",userDetail.getEmail());
                    Navigation.findNavController(v).navigate(R.id.action_myProfileFragment_to_emailFragment,bundle);

                }
        );

        binding.rlPass.setOnClickListener(v ->
                {
                    bundle.putString("pass",userDetail.getPassword());
                    Navigation.findNavController(v).navigate(R.id.action_myProfileFragment_to_changePassFragment,bundle);

                }
        );

        binding.rlPhone.setOnClickListener(v ->
                {
                    bundle.putString("mobile",userDetail.getMobile());
                    bundle.putString("cc",userDetail.getCountryCode());
                    Navigation.findNavController(v).navigate(R.id.action_myProfileFragment_to_phoneFragment,bundle);

                }
        );

        return binding.getRoot();
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
                        userDetail  = data.getResult();
                        setUserDetail();
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

    private void setUserDetail()
    {

        binding.tvName.setText(userDetail.getFirstName());
        binding.tvName1.setText(userDetail.getFirstName());
        binding.tvEmail.setText(userDetail.getEmail());
        binding.tvPhone.setText(userDetail.getCountryCode()+" "+userDetail.getMobile());
        binding.etPass.setText(userDetail.getPassword());

    }

}