package com.easyshopping.fragments;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.braintreepayments.cardform.OnCardFormSubmitListener;
import com.braintreepayments.cardform.view.CardForm;
import com.easyshopping.R;
import com.easyshopping.activities.HomeActivity;
import com.easyshopping.adapter.AddressAdapter;
import com.easyshopping.adapter.CardAdapter;
import com.easyshopping.adapter.CartAdapter;
import com.easyshopping.adapter.CartBottomItemAdapter;
import com.easyshopping.databinding.FragmentChooseCardBinding;
import com.easyshopping.models.SuccessResAddAddress;
import com.easyshopping.models.SuccessResAddCard;
import com.easyshopping.models.SuccessResBooking;
import com.easyshopping.models.SuccessResGetAddresses;
import com.easyshopping.models.SuccessResGetCards;
import com.easyshopping.models.SuccessResGetCart;
import com.easyshopping.retrofit.ApiClient;
import com.easyshopping.retrofit.ShoppingInterface;
import com.easyshopping.util.DataManager;
import com.easyshopping.util.SharedPreferenceUtility;
import com.easyshopping.util.UpdateAndDeleteAddress;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.ContentValues.TAG;
import static com.easyshopping.fragments.CartFragment.deliveryType;
import static com.easyshopping.retrofit.Constant.USER_ID;
import static com.easyshopping.retrofit.Constant.showToast;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ChooseCardFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChooseCardFragment extends Fragment implements UpdateAndDeleteAddress {

    FragmentChooseCardBinding binding;
    Dialog dialog;
    ShoppingInterface apiInterface;
    String addressID = "";
    List<SuccessResGetCart.Result> cartItems  = new LinkedList<>();

    String cartItemsIDs = "";

    String cardID = "";

    private boolean fromWhere;

    private int selectedPosition = -1;

    private boolean selected = false;

    private List<SuccessResGetCards.Result> cardList = new LinkedList<>();

    String cardNo ="",expirationDate="",cvv = "",cardType = "",holderName="";

    String cartID = "",totalAmount = "",companyID = "";

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ChooseCardFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @param param1 Parameter 1
     * @param param2 Parameter 2.
     * @return A new instance of fragment ChooseCardFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ChooseCardFragment newInstance(String param1, String param2) {
        ChooseCardFragment fragment = new ChooseCardFragment();
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

        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_choose_card, container, false);

        apiInterface = ApiClient.getClient().create(ShoppingInterface.class);

        binding.imgAdd.setOnClickListener(v ->
                {
                  fullScreenDialog();
                }
                );

        binding.imgHeader.setOnClickListener(v -> getActivity().onBackPressed());

        Bundle bundle = this.getArguments();

        if (bundle!=null)
        {
            String from = bundle.getString("from");
            if(from.equalsIgnoreCase("profile"))
            {
                binding.btnNext.setVisibility(View.GONE);
                fromWhere = false;
                getCards();
            }
            else
            {

                fromWhere = true;
                addressID = bundle.getString("id");
                cartID = bundle.getString("cartID");
                binding.btnNext.setVisibility(View.VISIBLE);
                getCartItems();
                getCards();
            }

            binding.btnNext.setOnClickListener(v ->
                    {

                        if (selected)
                        {

                            cardID = cardList.get(selectedPosition).getId();
                            addBooking();

                        } else
                        {
                            showToast(getActivity(),"Please select card.");
                        }

                    }
                    );


        }
        return binding.getRoot();
    }


    private void getCartItems()
    {


        String userId = SharedPreferenceUtility.getInstance(getActivity()).getString(USER_ID);
        Map<String,String> map = new HashMap<>();
        map.put("user_id",userId);

        Call<SuccessResGetCart> call = apiInterface.getCartByUserId(map);
        call.enqueue(new Callback<SuccessResGetCart>() {
            @Override
            public void onResponse(Call<SuccessResGetCart> call, Response<SuccessResGetCart> response) {

                DataManager.getInstance().hideProgressMessage();
                try {
                    SuccessResGetCart data = response.body();
                    Log.e("data",data.status);
                    if (data.status.equals("1")) {
                        String dataResponse = new Gson().toJson(response.body());

                        cartItems.clear();
                        cartItems.addAll(data.getResult());

                        totalAmount = data.totalPrice+"";
                        companyID = cartItems.get(0).getCompanyId();

                        setCartList();

                        cartItemsIDs = cartItemsIDs.substring(0, cartItemsIDs.length() -1);
                        String cartItems = cartItemsIDs;

                        //    manageAddCartButton();
                        //  setDetail();

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
            public void onFailure(Call<SuccessResGetCart> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }
        });

    }

    public void setCartList()
    {

        for(SuccessResGetCart.Result cart:cartItems)
        {

            cartItemsIDs = cartItemsIDs+""+cart.getId()+",";

        }

    }


    private void fullScreenDialog() {

        dialog = new Dialog(getActivity(), WindowManager.LayoutParams.MATCH_PARENT);

        dialog.setContentView(R.layout.fragment_add_card);

        AppCompatButton btnAdd =  dialog.findViewById(R.id.btnAdd);

        ImageView ivBack;
        ivBack = dialog.findViewById(R.id.img_header);

        CardForm cardForm = dialog.findViewById(R.id.card_form);

        cardForm.cardRequired(true)
                .maskCardNumber(true)
                .maskCvv(true)
                .expirationRequired(true)
                .cvvRequired(true)
                .postalCodeRequired(false)
                .mobileNumberRequired(false)
                .saveCardCheckBoxChecked(false)
                .saveCardCheckBoxVisible(false)
                .cardholderName(CardForm.FIELD_REQUIRED)
                .mobileNumberExplanation("Make sure SMS is enabled for this mobile number")
                .actionLabel("Purchase")
                .setup((AppCompatActivity) getActivity());

        cardForm.setOnCardFormSubmitListener(new OnCardFormSubmitListener() {
            @Override
            public void onCardFormSubmit() {

                cardNo = cardForm.getCardNumber();
                expirationDate = cardForm.getExpirationMonth()+"/"+cardForm.getExpirationYear();
                cvv = cardForm.getCvv();
                cardType = "";
                holderName = cardForm.getCardholderName();

                if(cardForm.isValid())
                {

                    addCardDetails();

                }else
                {
                    cardForm.validate();
                }

            }
        });

        btnAdd.setOnClickListener(v ->
                {

                    cardNo = cardForm.getCardNumber();
                    expirationDate = cardForm.getExpirationDateEditText().getText().toString();
                    cvv = cardForm.getCvv();
                    cardType = "";
                    holderName = cardForm.getCardholderName();

                    if(cardForm.isValid())
                    {
                        addCardDetails();

                    }else
                    {
                        cardForm.validate();
                    }

                }
                );

        ivBack.setOnClickListener(v ->
                {
                    dialog.dismiss();
                }
                );

        dialog.show();

    }

    private void addCardDetails()
    {

        String userId = SharedPreferenceUtility.getInstance(getActivity()).getString(USER_ID);

        DataManager.getInstance().showProgressMessage(getActivity(),getString(R.string.please_wait));

        Map<String,String> map = new HashMap<>();
        map.put("user_id",userId);
        map.put("card_no",cardNo);
        map.put("exp_date",expirationDate);
        map.put("cvv",cvv);
        map.put("cart_type",cardType);
        map.put("holder_name",holderName);

        Log.e(TAG,"Test Request "+map);
        Call<SuccessResAddCard> loginCall = apiInterface.addCard(map);
        loginCall.enqueue(new Callback<SuccessResAddCard>() {
            @Override
            public void onResponse(Call<SuccessResAddCard> call, Response<SuccessResAddCard> response) {
                DataManager.getInstance().hideProgressMessage();
                try {

                    SuccessResAddCard data = response.body();
                    String responseString = new Gson().toJson(response.body());

                 //   getActivity().onBackPressed();
                    dialog.dismiss();

                    Log.e(TAG,"Test Response :"+responseString);
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e(TAG,"Test Response :"+response.body());
                }
            }

            @Override
            public void onFailure(Call<SuccessResAddCard> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }
        });
    }


    private void getCards()
    {

        String userId = SharedPreferenceUtility.getInstance(getActivity()).getString(USER_ID);
        DataManager.getInstance().showProgressMessage(getActivity(),getString(R.string.please_wait));

        Map<String,String> map = new HashMap<>();
        map.put("user_id",userId);

        Call<SuccessResGetCards> call = apiInterface.getCards(map);

        call.enqueue(new Callback<SuccessResGetCards>() {
            @Override
            public void onResponse(Call<SuccessResGetCards> call, Response<SuccessResGetCards> response) {

                DataManager.getInstance().hideProgressMessage();
                try {
                    SuccessResGetCards data = response.body();
                    Log.e("data",data.status);
                    if (data.status.equals("1")) {
                        String dataResponse = new Gson().toJson(response.body());

                        cardList.clear();
                        cardList.addAll(data.getResult());

                        binding.rvCards.setHasFixedSize(true);
                        binding.rvCards.setLayoutManager(new LinearLayoutManager(getActivity()));
                        binding.rvCards.setAdapter(new CardAdapter(getActivity(),cardList,ChooseCardFragment.this,fromWhere));


//                        SessionManager.writeString(RegisterAct.this, Constant.driver_id,data.result.id);
//                        App.showToast(RegisterAct.this, data.message, Toast.LENGTH_SHORT);

                    } else if (data.status.equals("0")) {
                        showToast(getActivity(), data.message);
                        cardList.clear();
                        binding.rvCards.setHasFixedSize(true);
                        binding.rvCards.setLayoutManager(new LinearLayoutManager(getActivity()));
                        binding.rvCards.setAdapter(new CardAdapter(getActivity(),cardList,ChooseCardFragment.this,fromWhere));

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<SuccessResGetCards> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }
        });

    }


    @Override
    public void updateCart(View v, String name, String address, String id, String phone, String countrycode) {

    }

    @Override
    public void deleteCart(String productId) {

    }

    @Override
    public void isSelected(boolean isSelected,int position) {

        selected = isSelected;
        selectedPosition = position;

    }

    private void addBooking()
    {


        String userId = SharedPreferenceUtility.getInstance(getActivity()).getString(USER_ID);
        DataManager.getInstance().showProgressMessage(getActivity(),getString(R.string.please_wait));

        Map<String,String> map = new HashMap<>();
        map.put("cart_id",cartItemsIDs);
        map.put("address_id",addressID);
        map.put("deliverytype",deliveryType);
        map.put("payment_type","Credit/Debit");
        map.put("user_id",userId);
        map.put("company_id",companyID);
        map.put("total_amount",totalAmount);
        map.put("card_id",cardID);

        Call<SuccessResBooking> call = apiInterface.book(map);

        call.enqueue(new Callback<SuccessResBooking>() {
            @Override
            public void onResponse(Call<SuccessResBooking> call, Response<SuccessResBooking> response) {

                DataManager.getInstance().hideProgressMessage();
                try {
                    SuccessResBooking data = response.body();
                    Log.e("data",data.status);
                    if (data.status.equals("1")) {
                        String dataResponse = new Gson().toJson(response.body());
                        showToast(getActivity(), data.message);

                        startActivity(new Intent(getActivity(), HomeActivity.class));
                        getActivity().finish();

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
            public void onFailure(Call<SuccessResBooking> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }
        });

    }


}