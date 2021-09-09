package com.easyshopping.fragments;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.easyshopping.R;
import com.easyshopping.databinding.FragmentAddAddressBinding;
import com.easyshopping.models.SuccessResAddAddress;
import com.easyshopping.models.SuccessResUpdateCart;
import com.easyshopping.retrofit.ApiClient;
import com.easyshopping.retrofit.Constant;
import com.easyshopping.retrofit.ShoppingInterface;
import com.easyshopping.util.DataManager;
import com.easyshopping.util.GPSTracker;
import com.easyshopping.util.NetworkAvailablity;
import com.easyshopping.util.SharedPreferenceUtility;
import com.google.android.gms.maps.GoogleMap;
import com.google.gson.Gson;

import java.io.IOException;
import java.net.InterfaceAddress;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;
import static com.easyshopping.retrofit.Constant.USER_ID;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddAddressFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddAddressFragment extends Fragment {

    FragmentAddAddressBinding binding;
    private GoogleMap mMap;
    GPSTracker gpsTracker;
    String strLat, strLng;
    public static final int LOCATION_REQUEST = 1000;
    Geocoder geocoder;
    List<Address> addresses;

    String name = "",address = "",phone = "",cc = "",id = "";

    private ShoppingInterface apiInterface;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AddAddressFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddAddressFragment.
     */

    // TODO: Rename and change types and number of parameters
    public static AddAddressFragment newInstance(String param1, String param2) {
        AddAddressFragment fragment = new AddAddressFragment();
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
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_address, container, false);
        binding.layoutMyProfile.imgHeader.setOnClickListener(v -> {
            getActivity().onBackPressed();
        });

        apiInterface = ApiClient.getClient().create(ShoppingInterface.class);

        binding.layoutMyProfile.tvHeader.setText(R.string.address);

        Bundle bundle = this.getArguments();

        String from = bundle.getString("from");

        if (from.equalsIgnoreCase("ShipToEdit"))
        {

            String name1 = bundle.getString("name");
            String address1 = bundle.getString("address");
            String phone1 = bundle.getString("phone");
            String ccode = bundle.getString("cc");
            id = bundle.getString("id");

            if(CurrentLocationFragment.myAddress.equalsIgnoreCase(""))
            {
                CurrentLocationFragment.myAddress  = address1;
            }

            binding.ccp.setCountryForPhoneCode(Integer.parseInt(ccode));

            binding.etName.setText(name1);
            binding.etAddress.setText(address1);
            binding.etNumber.setText(phone1);

            binding.etAddress.setOnClickListener(v ->
                    {
                        Navigation.findNavController(v).navigate(R.id.action_addAddressFragment_to_currentLocationFragment);
                    }
            );

            binding.btnLogin.setOnClickListener(v ->
                    {

                        name = binding.etName.getText().toString().trim();
                        address = binding.etAddress.getText().toString();
                        cc = binding.ccp.getSelectedCountryCode();
                        phone = binding.etNumber.getText().toString().trim();

                        if(isValid())
                        {

                            if (NetworkAvailablity.getInstance(getActivity()).checkNetworkStatus()) {

                                editAddress();

                            } else {
                                Toast.makeText(getActivity(), getResources().getString(R.string.msg_noInternet), Toast.LENGTH_SHORT).show();
                            }
                        }else {
                            Toast.makeText(getActivity(),  getResources().getString(R.string.on_error), Toast.LENGTH_SHORT).show();
                        }
                    }
            );
        }


        //For add address

        else if(from.equalsIgnoreCase("ShipToAdd"))
        {
            binding.etAddress.setText(CurrentLocationFragment.myAddress);

            binding.etAddress.setOnClickListener(v ->
                    {
                        Navigation.findNavController(v).navigate(R.id.action_addAddressFragment_to_currentLocationFragment);
                    }
            );

            binding.btnLogin.setOnClickListener(v ->
                    {

                        name = binding.etName.getText().toString().trim();
                        address = binding.etAddress.getText().toString();
                        cc = binding.ccp.getSelectedCountryCode();
                        phone = binding.etNumber.getText().toString().trim();

                        if(isValid())
                        {

                            if (NetworkAvailablity.getInstance(getActivity()).checkNetworkStatus()) {

                                addAddress();

                            } else {
                                Toast.makeText(getActivity(), getResources().getString(R.string.msg_noInternet), Toast.LENGTH_SHORT).show();
                            }
                        }else {
                            Toast.makeText(getActivity(),  getResources().getString(R.string.on_error), Toast.LENGTH_SHORT).show();
                        }
                    }
            );
        } else if(from.equalsIgnoreCase("profile"))
        {
            binding.etAddress.setText(CurrentLocationFragment.myAddress);

            binding.etAddress.setOnClickListener(v ->
                    {
                        Navigation.findNavController(v).navigate(R.id.action_addAddressFragment_to_currentLocationFragment);
                    }
            );

            binding.btnLogin.setOnClickListener(v ->
                    {

                        name = binding.etName.getText().toString().trim();
                        address = binding.etAddress.getText().toString();
                        cc = binding.ccp.getSelectedCountryCode();
                        phone = binding.etNumber.getText().toString().trim();

                        if(isValid())
                        {

                            if (NetworkAvailablity.getInstance(getActivity()).checkNetworkStatus()) {

                                addAddress();

                            } else {
                                Toast.makeText(getActivity(), getResources().getString(R.string.msg_noInternet), Toast.LENGTH_SHORT).show();
                            }
                        }else {
                            Toast.makeText(getActivity(),  getResources().getString(R.string.on_error), Toast.LENGTH_SHORT).show();
                        }
                    }
            );
        }


        return binding.getRoot();
    }


    private boolean isValid() {
        if (name.equalsIgnoreCase("")) {
            binding.etName.setError(getString(R.string.enter_name));
            return false;
        }else if (address.equalsIgnoreCase("")) {
            Toast.makeText(getActivity(),""+ R.string.please_add_address,Toast.LENGTH_SHORT).show();
            return false;
        }else if (phone.equalsIgnoreCase("")) {
            binding.etNumber.setError(getString(R.string.please_enter_mobile_number));
            return false;
        }
        return true;
    }

    public void addAddress()
    {

        String userId = SharedPreferenceUtility.getInstance(getActivity()).getString(USER_ID);

        DataManager.getInstance().showProgressMessage(getActivity(),getString(R.string.please_wait));

        Map<String,String> map = new HashMap<>();
        map.put("user_id",userId);
        map.put("name",name);
        map.put("phone",phone);
        map.put("address",address);
        map.put("country_code",cc);

        Log.e(TAG,"Test Request "+map);
        Call<SuccessResAddAddress> loginCall = apiInterface.addAddress(map);
        loginCall.enqueue(new Callback<SuccessResAddAddress>() {
            @Override
            public void onResponse(Call<SuccessResAddAddress> call, Response<SuccessResAddAddress> response) {
                DataManager.getInstance().hideProgressMessage();
                try {

                    SuccessResAddAddress data = response.body();
                    String responseString = new Gson().toJson(response.body());
                    binding.etName.setText("");
                    binding.etNumber.setText("");
                    binding.etAddress.setText("");

                    getActivity().onBackPressed();

                    Log.e(TAG,"Test Response :"+responseString);
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e(TAG,"Test Response :"+response.body());
                }
            }

            @Override
            public void onFailure(Call<SuccessResAddAddress> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }
        });

    }

    public void editAddress()
    {

        String userId = SharedPreferenceUtility.getInstance(getActivity()).getString(USER_ID);
        DataManager.getInstance().showProgressMessage(getActivity(),getString(R.string.please_wait));

        Map<String,String> map = new HashMap<>();
        map.put("user_id",userId);
        map.put("id",id);
        map.put("name",name);
        map.put("phone",phone);
        map.put("address",address);
        map.put("country_code",cc);

        Log.e(TAG,"Test Request "+map);
        Call<SuccessResUpdateCart> loginCall = apiInterface.updateAddress(map);
        loginCall.enqueue(new Callback<SuccessResUpdateCart>() {
            @Override
            public void onResponse(Call<SuccessResUpdateCart> call, Response<SuccessResUpdateCart> response) {
                DataManager.getInstance().hideProgressMessage();
                try {
                    SuccessResUpdateCart data = response.body();
                    String responseString = new Gson().toJson(response.body());
                    binding.etName.setText("");
                    binding.etNumber.setText("");
                    binding.etAddress.setText("");

                    getActivity().onBackPressed();

                    Log.e(TAG,"Test Response :"+responseString);
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e(TAG,"Test Response :"+response.body());
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
    public void onResume() {
        super.onResume();

        binding.etAddress.setText(CurrentLocationFragment.myAddress);

    }

    public void setAddress()
    {

        geocoder = new Geocoder(getActivity(), Locale.getDefault());

        try {
            addresses = geocoder.getFromLocation(gpsTracker.getLatitude(), gpsTracker.getLongitude(), 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            String city = addresses.get(0).getLocality();
            String state = addresses.get(0).getAdminArea();
            String country = addresses.get(0).getCountryName();
            String postalCode = addresses.get(0).getPostalCode();
            String knownName = addresses.get(0).getFeatureName(); //

            binding.etAddress.setText(address);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    Constant.LOCATION_REQUEST);
        } else {
            Log.e("Latittude====",gpsTracker.getLatitude()+"");
            strLat = Double.toString(gpsTracker.getLatitude()) ;
            strLng = Double.toString(gpsTracker.getLongitude()) ;
            setAddress();
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
//            if (requestCode == Constant.GPS_REQUEST) {
//                isGPS = true; // flag maintain before get location
//            }
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {

            case 1000: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    Log.e("Latittude====", gpsTracker.getLatitude() + "");

                    strLat = Double.toString(gpsTracker.getLatitude()) ;
                    strLng = Double.toString(gpsTracker.getLongitude()) ;
                    setAddress();


//
//                    if (isContinue) {
//                        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//                            // TODO: Consider calling
//                            //    ActivityCompat#requestPermissions
//                            // here to request the missing permissions, and then overriding
//                            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//                            //                                          int[] grantResults)
//                            // to handle the case where the user grants the permission. See the documentation
//                            // for ActivityCompat#requestPermissions for more details.
//                            return;
//                        }
//                        mFusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null);
//                    } else {
//                        Log.e("Latittude====", gpsTracker.getLatitude() + "");
//
//                        strLat = Double.toString(gpsTracker.getLatitude()) ;
//                        strLng = Double.toString(gpsTracker.getLongitude()) ;
//
//                    }
                } else {
                    Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.permisson_denied), Toast.LENGTH_SHORT).show();
                }
                break;
            }


        }
    }

}