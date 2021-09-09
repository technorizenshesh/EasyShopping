package com.easyshopping.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.easyshopping.R;
import com.easyshopping.activities.LoginAct;
import com.easyshopping.databinding.FragmentProfileBinding;
import com.easyshopping.retrofit.Constant;
import com.easyshopping.util.SharedPreferenceUtility;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {

    FragmentProfileBinding binding ;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */

    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
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
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_profile, container, false);

        binding.llAcc.setOnClickListener(v ->
                {
                    Navigation.findNavController(v).navigate(R.id.action_navigation_user_to_myProfileFragment);

                }
                );

        binding.llAddress.setOnClickListener(v ->
                {

                    Bundle bundle = new Bundle();
                    bundle.putString("from","profile");
                    Navigation.findNavController(v).navigate(R.id.action_navigation_user_to_shipToFragment,bundle);

                }
        );

        binding.llPayment.setOnClickListener(v ->
                {
                    Bundle bundle = new Bundle();
                    bundle.putString("from","profile");
                    Navigation.findNavController(v).navigate(R.id.action_navigation_user_to_chooseCardFragment,bundle);

                }
        );


        binding.llOrders.setOnClickListener(v ->
                {
                    Navigation.findNavController(v).navigate(R.id.action_navigation_user_to_myOrdersFragment);

                }
        );

        binding.llLogout.setOnClickListener(v ->
                {

                    SharedPreferenceUtility.getInstance(getActivity().getApplication()).putBoolean(Constant.IS_USER_LOGGED_IN, false);
                    Intent i = new Intent(getActivity(), LoginAct.class);
// set the new task and clear flags
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(i);

                }
        );

        return binding.getRoot();
    }
}