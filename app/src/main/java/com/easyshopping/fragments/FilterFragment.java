package com.easyshopping.fragments;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.easyshopping.R;
import com.easyshopping.adapter.FilterAdapter;
import com.easyshopping.adapter.SubCategoryAdapter;
import com.easyshopping.databinding.FragmentFilterBinding;

import java.util.LinkedList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FilterFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FilterFragment extends Fragment {


    FragmentFilterBinding binding;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    LinkedList<String> condition = new LinkedList<>();
    LinkedList<String> buying = new LinkedList<>();
    LinkedList<String> item = new LinkedList<>();
    LinkedList<String> show = new LinkedList<>();

    public FilterFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FilterFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FilterFragment newInstance(String param1, String param2) {
        FilterFragment fragment = new FilterFragment();
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
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_filter, container, false);

        setData();
        binding.layoutMyProfile.imgHeader.setOnClickListener(v -> {
            getActivity().onBackPressed();
        });


        binding.layoutMyProfile.tvHeader.setText(R.string.profile);


        binding.rvCondition.setHasFixedSize(true);
        binding.rvCondition.setLayoutManager(new GridLayoutManager(getActivity(),3));
        binding.rvCondition.setAdapter(new FilterAdapter(getActivity(),condition));

        binding.rvBuying.setHasFixedSize(true);
        binding.rvBuying.setLayoutManager(new GridLayoutManager(getActivity(),3));
        binding.rvBuying.setAdapter(new FilterAdapter(getActivity(),buying));

        binding.rvItemList.setHasFixedSize(true);
        binding.rvItemList.setLayoutManager(new GridLayoutManager(getActivity(),3));
        binding.rvItemList.setAdapter(new FilterAdapter(getActivity(),item));

        binding.rvSHow.setHasFixedSize(true);
        binding.rvSHow.setLayoutManager(new GridLayoutManager(getActivity(),2));
        binding.rvSHow.setAdapter(new FilterAdapter(getActivity(),show));


        return binding.getRoot();    }

    private void setData() {

        condition.add("New");
        condition.add("Used");
        condition.add("Not Specified");

        buying.add("All Listings");
        buying.add("Aceepts Offers");
        buying.add("Auction");
        buying.add("Buy it now");
        buying.add("Classified ads");

        item.add("US");
        item.add("North America");
        item.add("Europe");
        item.add("Asia");

        show.add("Free Return");
        show.add("Return Accepted");
        show.add("Authorised seller");
        show.add("Completed items");
        show.add("Sold Items");
        show.add("Data and Savings");
        show.add("Sale Items");
        show.add("List and Lots");
        show.add("Search in Description");
        show.add("Benefits Charity");
        show.add("Autheticity Verified");


    }
}