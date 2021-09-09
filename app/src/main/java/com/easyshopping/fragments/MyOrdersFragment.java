package com.easyshopping.fragments;

import android.app.Dialog;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.braintreepayments.cardform.OnCardFormSubmitListener;
import com.braintreepayments.cardform.view.CardForm;
import com.easyshopping.R;
import com.easyshopping.activities.ForgotActivity;
import com.easyshopping.adapter.AddressAdapter;
import com.easyshopping.adapter.MyOrdersAdapter;
import com.easyshopping.adapter.MyProductItemAdapter;
import com.easyshopping.databinding.FragmentMyOrdersBinding;
import com.easyshopping.models.SuccessResAddReview;
import com.easyshopping.models.SuccessResForgotPassword;
import com.easyshopping.models.SuccessResGetAddresses;
import com.easyshopping.models.SuccessResGetOrders;
import com.easyshopping.retrofit.ApiClient;
import com.easyshopping.retrofit.ShoppingInterface;
import com.easyshopping.util.DataManager;
import com.easyshopping.util.ItemDetail;
import com.easyshopping.util.RatingBarClick;
import com.easyshopping.util.SharedPreferenceUtility;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import io.perfmark.Link;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.easyshopping.retrofit.Constant.USER_ID;
import static com.easyshopping.retrofit.Constant.showToast;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MyOrdersFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyOrdersFragment extends Fragment implements ItemDetail {

    FragmentMyOrdersBinding binding;

    ShoppingInterface apiInterface ;

    SuccessResGetOrders.Result orderDetail = null;

    private List<SuccessResGetOrders.Result> ordersList = new LinkedList<>();
    Dialog dialog;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MyOrdersFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MyOrdersFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MyOrdersFragment newInstance(String param1, String param2) {
        MyOrdersFragment fragment = new MyOrdersFragment();
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

        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_my_orders, container, false);

        binding.header.imgHeader.setOnClickListener(v -> getActivity().onBackPressed());
        binding.header.tvHeader.setText(R.string.orders);

        apiInterface = ApiClient.getClient().create(ShoppingInterface.class);

        getOrders();

        return binding.getRoot();
    }

    private void getOrders()
    {

        String userId = SharedPreferenceUtility.getInstance(getActivity()).getString(USER_ID);
        DataManager.getInstance().showProgressMessage(getActivity(),getString(R.string.please_wait));

        Map<String,String> map = new HashMap<>();
        map.put("user_id",userId);

        Call<SuccessResGetOrders> call = apiInterface.getOrders(map);

        call.enqueue(new Callback<SuccessResGetOrders>() {
            @Override
            public void onResponse(Call<SuccessResGetOrders> call, Response<SuccessResGetOrders> response) {

                DataManager.getInstance().hideProgressMessage();
                try {
                    SuccessResGetOrders data = response.body();
                    Log.e("data",data.status);
                    if (data.status.equals("1")) {
                        String dataResponse = new Gson().toJson(response.body());

                        ordersList.clear();
                        ordersList.addAll(data.getResult());

                        binding.rvOrdered.setHasFixedSize(true);
                        binding.rvOrdered.setLayoutManager(new LinearLayoutManager(getActivity()));
                        binding.rvOrdered.setAdapter(new MyOrdersAdapter(getActivity(),ordersList,MyOrdersFragment.this::getClick));

//                        SessionManager.writeString(RegisterAct.this, Constant.driver_id,data.result.id);
//                        App.showToast(RegisterAct.this, data.message, Toast.LENGTH_SHORT);

                    } else if (data.status.equals("0")) {
                        showToast(getActivity(), data.message);
                        ordersList.clear();
                        binding.rvOrdered.setHasFixedSize(true);
                        binding.rvOrdered.setLayoutManager(new LinearLayoutManager(getActivity()));
                        binding.rvOrdered.setAdapter(new MyOrdersAdapter(getActivity(),ordersList,MyOrdersFragment.this::getClick));

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<SuccessResGetOrders> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }
        });

    }


    private void fullScreenDialog() {

        dialog = new Dialog(getActivity(), WindowManager.LayoutParams.MATCH_PARENT);

        dialog.setContentView(R.layout.fragment_order_detail);

        TextView tvOrderId,tvOrderAMount ,tvtype ;
        RecyclerView rvOrderItem = dialog.findViewById(R.id.rvProducts);

        tvOrderId = dialog.findViewById(R.id.tvID);
        tvOrderAMount = dialog.findViewById(R.id.tvTotalAmount);
        tvtype = dialog.findViewById(R.id.tvPayment);


        tvOrderId.setText(orderDetail.getId());
        tvOrderAMount.setText(orderDetail.getTotalAmount());
        tvtype.setText(orderDetail.getPaymentType());

        rvOrderItem.setHasFixedSize(true);
        rvOrderItem.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvOrderItem.setAdapter(new MyProductItemAdapter(getActivity(), orderDetail.getItemData(), new RatingBarClick() {
            @Override
            public void getClick(String productId, String rating) {
                addRating(productId,rating);
            }
        }));


        ImageView ivBack;
        ivBack = dialog.findViewById(R.id.img_header);

        ivBack.setOnClickListener(v ->
                {
                    dialog.dismiss();
                    getOrders();
                }
        );


        dialog.show();

    }


    @Override
    public void getClick(int position) {

        orderDetail = ordersList.get(position);
        fullScreenDialog();
    }


    public void addRating(String productId,String rating)
    {

        String userId = SharedPreferenceUtility.getInstance(getActivity()).getString(USER_ID);
        Map<String,String> map = new HashMap<>();
        map.put("user_id",userId);
        map.put("product_id",productId);
        map.put("rating",rating);
        map.put("comment","test");

        Call<SuccessResAddReview> call = apiInterface.addReview(map);
        call.enqueue(new Callback<SuccessResAddReview>() {
            @Override
            public void onResponse(Call<SuccessResAddReview> call, Response<SuccessResAddReview> response) {

                DataManager.getInstance().hideProgressMessage();
                try {
                    SuccessResAddReview data = response.body();
                    showToast(getActivity(), data.result);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<SuccessResAddReview> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }
        });
    }



}