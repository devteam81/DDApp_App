package com.dd.app.ui.activity;

import static com.dd.app.network.APIConstants.APIs.GET_PAYMENT_GATEWAY;
import static com.dd.app.network.APIConstants.APIs.MAKE_AGREEPAY_PAYMENT;
import static com.dd.app.network.APIConstants.APIs.MAKE_PAYTM_PAYMENT;
import static com.dd.app.network.APIConstants.APIs.PAYTM_CHECKSUM;
import static com.dd.app.network.APIConstants.APIs.UPDATE_AGE;
import static com.dd.app.network.APIConstants.Params.APPVERSION;
import static com.dd.app.network.APIConstants.Params.AP_API_KEY;
import static com.dd.app.network.APIConstants.Params.AP_COUNTRY;
import static com.dd.app.network.APIConstants.Params.AP_CURRENCY;
import static com.dd.app.network.APIConstants.Params.AP_ERROR_MSG;
import static com.dd.app.network.APIConstants.Params.AP_HOST_NAME;
import static com.dd.app.network.APIConstants.Params.AP_MODE;
import static com.dd.app.network.APIConstants.Params.AP_ORDER_ID;
import static com.dd.app.network.APIConstants.Params.AP_RESP_MSG;
import static com.dd.app.network.APIConstants.Params.AP_RETURN_URL;
import static com.dd.app.network.APIConstants.Params.BEBUEXT;
import static com.dd.app.network.APIConstants.Params.BRAND;
import static com.dd.app.network.APIConstants.Params.CHANNEL_ID;
import static com.dd.app.network.APIConstants.Params.CHECKSUMHASH;
import static com.dd.app.network.APIConstants.Params.CUST_ID;
import static com.dd.app.network.APIConstants.Params.DEVICE;
import static com.dd.app.network.APIConstants.Params.DEVICE_CODE;
import static com.dd.app.network.APIConstants.Params.DISTRIBUTOR;
import static com.dd.app.network.APIConstants.Params.ERROR_MESSAGE;
import static com.dd.app.network.APIConstants.Params.ID;
import static com.dd.app.network.APIConstants.Params.INDUSTRY_TYPE_ID;
import static com.dd.app.network.APIConstants.Params.MID;
import static com.dd.app.network.APIConstants.Params.MODEL;
import static com.dd.app.network.APIConstants.Params.ORDERID;
import static com.dd.app.network.APIConstants.Params.PARAM_CHANNEL_ID;
import static com.dd.app.network.APIConstants.Params.PARAM_CUST_ID;
import static com.dd.app.network.APIConstants.Params.PARAM_INDUSTRY_TYPE_ID;
import static com.dd.app.network.APIConstants.Params.PARAM_ORDER_ID;
import static com.dd.app.network.APIConstants.Params.PARAM_TXN_AMOUNT;
import static com.dd.app.network.APIConstants.Params.PAYMENT_GATEWAY_NAME;
import static com.dd.app.network.APIConstants.Params.PAYMENT_GATEWAY_TYPE;
import static com.dd.app.network.APIConstants.Params.PAYTM_CALLBACK_URL;
import static com.dd.app.network.APIConstants.Params.PLAT;
import static com.dd.app.network.APIConstants.Params.REFERRER;
import static com.dd.app.network.APIConstants.Params.REF_ID;
import static com.dd.app.network.APIConstants.Params.SHARE_TYPE_SUBSCRIBE;
import static com.dd.app.network.APIConstants.Params.SUCCESS;
import static com.dd.app.network.APIConstants.Params.TEST_CALLBACK_URL;
import static com.dd.app.network.APIConstants.Params.TEST_CHANNEL_ID_KEY;
import static com.dd.app.network.APIConstants.Params.TEST_INDUSTRY_TYPE_KEY;
import static com.dd.app.network.APIConstants.Params.TEST_MID_KEY;
import static com.dd.app.network.APIConstants.Params.TEST_WEBSITE_KEY;
import static com.dd.app.network.APIConstants.Params.TXN_AMOUNT;
import static com.dd.app.network.APIConstants.Params.TXN_TOKEN;
import static com.dd.app.network.APIConstants.Params.TYPE_AGREE_PAY_KEY;
import static com.dd.app.network.APIConstants.Params.TYPE_CARD_KEY;
import static com.dd.app.network.APIConstants.Params.TYPE_GOOGLE_IN_APP_KEY;
import static com.dd.app.network.APIConstants.Params.TYPE_PAYTM_KEY;
import static com.dd.app.network.APIConstants.Params.TYPE_UPI_KEY;
import static com.dd.app.network.APIConstants.Params.VERSION;
import static com.dd.app.network.APIConstants.Params.VERSION_CODE;
import static com.dd.app.network.APIConstants.Params.WEBSITE;
import static com.dd.app.ui.activity.MainActivity.CURRENT_COUNTRY;
import static com.dd.app.ui.activity.MainActivity.CURRENT_IP;
import static com.dd.app.ui.activity.MainActivity.CURRENT_REGION;
import static com.dd.app.ui.activity.MainActivity.STATES_PAYMENT;
import static com.dd.app.util.UiUtils.animationObject;
import static com.dd.app.util.UiUtils.checkString;
import static com.dd.app.util.sharedpref.Utils.getSecureId;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dd.app.R;
import com.dd.app.dd.model.LoginUser;
import com.dd.app.dd.model.ResponseBody;
import com.dd.app.dd.model.ResponseBodyArray;
import com.dd.app.dd.model.ResponseLogin;
import com.dd.app.dd.network.RetrofitClient;
import com.dd.app.dd.utils.sharedPreference.PrefKeysDD;
import com.dd.app.dd.utils.sharedPreference.SharedPreferenceHelper;
import com.dd.app.model.AgeItem;
import com.dd.app.model.Invoice;
import com.dd.app.model.Video;
import com.dd.app.network.APIClient;
import com.dd.app.network.APIConstants;
import com.dd.app.network.APIInterface;
import com.dd.app.ui.adapter.AgeAdapter;
import com.dd.app.ui.fragment.bottomsheet.PaymentBottomSheet;
import com.dd.app.util.NetworkUtils;
import com.dd.app.util.ResponsivenessUtils;
import com.dd.app.util.UiUtils;
import com.dd.app.util.dialog.LoadingDialog;
import com.dd.app.util.sharedpref.PrefKeys;
import com.dd.app.util.sharedpref.PrefUtils;
import com.dd.app.util.sharedpref.Utils;
import com.bumptech.glide.GenericTransitionOptions;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.signature.ObjectKey;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paytm.pgsdk.PaytmOrder;
import com.paytm.pgsdk.PaytmPaymentTransactionCallback;
import com.paytm.pgsdk.TransactionManager;
import com.test.pg.secure.pgsdkv4.PGConstants;
import com.test.pg.secure.pgsdkv4.PaymentGatewayPaymentInitializer;
import com.test.pg.secure.pgsdkv4.PaymentParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PayPerViewPlansActivity extends BaseActivity implements
        PaymentBottomSheet.PaymentsInterface{

    private static final String TAG = PayPerViewPlansActivity.class.getSimpleName();
    private final int PAYTM = 100;

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.video_thumbnail)
    ImageView video_thumbnail;
    @BindView(R.id.txt_name)
    TextView txt_name;
    @BindView(R.id.txt_listed_price)
    TextView txt_listed_price;
    @BindView(R.id.txt_discounted_price)
    TextView txt_discounted_price;
    @BindView(R.id.txt_actual_price)
    TextView txt_actual_price;
    @BindView(R.id.txt_total_coins)
    TextView txt_total_coins;

    @BindView(R.id.txt_final_coins)
    TextView txt_final_coins;
    @BindView(R.id.txt_plus)
    TextView txt_plus;
    @BindView(R.id.txt_final_price)
    TextView txt_final_price;

    @BindView(R.id.rl_pay_per_video)
    RelativeLayout rl_pay_per_video;


    APIInterface apiInterface;
    public PrefUtils prefUtils;

    Invoice invoice;
    Video video;
    AgeAdapter ageAdapter;
    Dialog dialog,paymentDialog;
    String selectedAge="";

    String selectedState="";

    PaymentBottomSheet.PaymentsInterface paymentsInterface;
    HashMap<String, String> paytmParamMap =null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_per_view_plans);
        ButterKnife.bind(this);

        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        apiInterface = APIClient.getClient().create(APIInterface.class);
        prefUtils = PrefUtils.getInstance(this);
        paymentsInterface = this;

        video = (Video) getIntent().getSerializableExtra("Video");
        setInvoice();

        //video.setListedPrice(1000.0);
        if(video.getListedPrice() < 1){
            Toast.makeText(this,getString(R.string.no_amount),Toast.LENGTH_SHORT).show();
            finish();
        }

        video_thumbnail.setLayoutParams(ResponsivenessUtils.getLayoutParamsForFullWidthSeasonView(this));
        Glide.with(getApplicationContext())
                .load(video.getIsMain()==1?video.getTrailerMobileImage():video.getThumbNailUrl())
                .transition(GenericTransitionOptions.with(animationObject))
                .apply(new RequestOptions().placeholder(R.drawable.bebu_placeholder_horizontal).error(R.drawable.bebu_placeholder_horizontal).diskCacheStrategy(DiskCacheStrategy.ALL).signature(new ObjectKey(0)))
                .thumbnail(0.5f)
                .into(video_thumbnail);

        UiUtils.log(TAG,"Name -> "+ video.getTitle());
        UiUtils.log(TAG,"Code -> "+ prefUtils.getStringValue(PrefKeys.REFERENCE_URL_CODE,"No Code"));
        UiUtils.log(TAG,"Ref -> "+ prefUtils.getStringValue(PrefKeys.REFERENCE_URL_ID,"No Ref"));

        txt_name.setText(video.getIsMain()==1?video.getTitle():video.getTitle()+" - "+video.getEpTitle());
        txt_listed_price.setText(video.getCurrency() + video.getBasePrice());
        txt_listed_price.setPaintFlags(txt_listed_price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        double discountedPrice = video.getBasePrice() - video.getListedPrice();
        txt_discounted_price.setText(video.getCurrency() + discountedPrice);
        txt_actual_price.setText(video.getCurrency() +  video.getListedPrice());

        setCoinsTotal();

        rl_pay_per_video.setOnClickListener(v-> showAgePicker()/*paymentCoinPicker()*//*sendPaytmPaymentToBackend(video.getAdminVideoId(),null)*/);
        /*rl_pay_per_video.setOnClickListener(v->{
            SharedPreferenceHelper sharedPreferenceHelper = new SharedPreferenceHelper(PayPerViewPlansActivity.this);
            int nowCoins = sharedPreferenceHelper.getIntValue(PrefKeysDD.Companion.getUSER_COINS(),0) - video.getListedPrice().intValue();
            sharedPreferenceHelper.setValue(PrefKeysDD.Companion.getUSER_COINS(), nowCoins);
            LoginUser.INSTANCE.setCoins(nowCoins);
        });*/

    }

    @Override
    public void onResume() {
        super.onResume();
        getUserDetails();
    }

    private void setInvoice(){
        invoice = new Invoice();
        if(video.getIsMain() == 1)
            invoice.setTitle(video.getTitle());
        else
            invoice.setTitle(video.getTitle()+" - "+video.getEpTitle());
        invoice.setTotalAmount(video.getBasePrice());
        invoice.setPaidAmount(video.getListedPrice());
        invoice.setCouponAmount(0.0);
        invoice.setCouponApplied(false);
        invoice.setCouponCode("");
        invoice.setCurrency(video.getCurrency());
        invoice.setCurrencySymbol(video.getCurrency());
    }

    private void setCoinsTotal(){
        txt_total_coins.setText(String.valueOf(LoginUser.INSTANCE.getCoins()));

        if(video.getListedPrice() > LoginUser.INSTANCE.getCoins()){
            txt_final_coins.setText(String.valueOf(LoginUser.INSTANCE.getCoins()));
            txt_final_price.setText(video.getCurrency() + (video.getListedPrice() - LoginUser.INSTANCE.getCoins()));
        }else{
            txt_final_coins.setText(String.valueOf(video.getListedPrice()));
            txt_plus.setVisibility(View.GONE);
            txt_final_price.setVisibility(View.GONE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String paymentId;
        if (requestCode == PGConstants.REQUEST_CODE) {
            if(resultCode == Activity.RESULT_OK){
                try{
                    String sdkResponse=data.getStringExtra(PGConstants.SDK_PAYMENT_RESPONSE);
                    if(sdkResponse==null){
                        UiUtils.log(TAG,"Transaction Error!");
                        //transactionIdView.setText("Transaction ID: NIL");
                        //transactionStatusView.setText("Transaction Status: Transaction Error!");
                        onPaymentFailed(getString(R.string.invalid_payment));
                    }else{
                        JSONObject response = new JSONObject(sdkResponse);
                        if(response.has("payment_response")&&response.getString("status").equals("success")){
                            JSONObject paymentResponse = new JSONObject(response.getString("payment_response"));
                            UiUtils.log(TAG, "paymentResponse: "+paymentResponse.toString());
                            sendAgreePayPaymentToBackend(/*invoice.isPayingForPlan(),
                                    invoice.isPayingForPlan() ? 1*//*plan.getId()*//* : */video.getAdminVideoId(), paymentResponse);
                            //transactionIdView.setText("Transaction ID: "+paymentResponse.getString("transaction_id"));
                            //transactionStatusView.setText("Transaction Status: "+paymentResponse.getString("response_message"));
                        }else{
                            UiUtils.log(TAG, "paymentResponse: Something went wrong");
                            onPaymentFailed(getString(R.string.invalid_payment));
                            //transactionIdView.setText("Transaction Status: "+response.getString("status"));
                            //transactionStatusView.setText("Transaction Message: "+response.getString("error_message"));
                        }
                    }

                }catch (Exception ep) {
                    UiUtils.log(TAG, Log.getStackTraceString(ep));
                }
            }
        }
        /*else if (requestCode == IN_APP_GATEWAY)
        {
            if(data!=null) {
                if (data.getStringExtra(PAYMENT_RESP).equalsIgnoreCase(SUCCESS))
                    onPaymentSucceeded(invoice);
                else
                    onPaymentFailed(getString(R.string.invalid_payment));
            }else
            {
                UiUtils.log(TAG,"Back button pressed");
                onPaymentFailed(getString(R.string.invalid_payment));
            }
        }*/
        else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
            onPaymentFailed(getString(R.string.invalid_payment));
        }
    }

    private void getUserDetails()
    {
        LoadingDialog.getInstance().show(PayPerViewPlansActivity.this);
        SharedPreferenceHelper sharedPreferenceHelper = new SharedPreferenceHelper(PayPerViewPlansActivity.this);
        try {
            JSONObject jsonObject = new JSONObject();
            //jsonObject.put("email", "demotest@81tech.app")
            //jsonObject.put("email", "ashish@gmail.com")
            //jsonObject.put("password", "123456")
            jsonObject.put("ott_id", sharedPreferenceHelper.getStringValue(PrefKeysDD.Companion.getOTT_ID(),""));

            UiUtils.log("jsonObject :", jsonObject.toString());

            /*Gson gson = new Gson();
            String data = gson.toJson(jsonObject);
            UiUtils.log("jsonObject :", data);*/
            RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), jsonObject.toString());

            Call<Object> response = RetrofitClient.INSTANCE.getApiInterface().getUserDetails(body);
            response.enqueue( new Callback<Object>() {
                @Override
                public void onResponse(Call<Object> call, Response<Object> response) {
                    LoadingDialog.getInstance().dismiss();
                    UiUtils.log(TAG, "Response-> " +response);
                    if(response.body() != null) {

                        UiUtils.log(TAG, "Data-> " +response.body());
                        String mJsonString = new Gson().toJson(response.body());
                        UiUtils.log(TAG, "String-> " +mJsonString);

                        try {
                            JSONObject mJsonObject = new JSONObject(mJsonString);

                            if (mJsonObject.getBoolean("success")) {

                                JSONObject data = mJsonObject.getJSONObject("data");
                                UiUtils.log(TAG, "Data-> " + data.getDouble("coins"));
                                int coins = (int) data.getDouble("coins");
                                sharedPreferenceHelper.setValue(PrefKeysDD.Companion.getUSER_COINS(), coins);
                                LoginUser.INSTANCE.setCoins(coins);
                                setCoinsTotal();
                            }else
                            {
                                UiUtils.log(TAG, "Message-> ");
                                Toast.makeText(PayPerViewPlansActivity.this,mJsonObject.getString("message"), Toast.LENGTH_LONG).show();
                            }

                        } catch (Exception e) {
                            UiUtils.log(TAG,Log.getStackTraceString(e));
                        }
                    }
                }

                @Override
                public void onFailure(Call<Object> call, Throwable t) {
                    //NetworkUtils.onApiError(PayPerViewPlansActivity.this);
                    LoadingDialog.getInstance().dismiss();
                    UiUtils.log(TAG,"Error->"+ t.getMessage());
                }
            });

        }catch (Exception e){
            LoadingDialog.getInstance().dismiss();
            UiUtils.log(TAG, Log.getStackTraceString(e));
        }

    }

    public void paymentCoinPicker()
    {

        Dialog dialog = new Dialog(PayPerViewPlansActivity.this);

        dialog.setContentView(R.layout.dialog_logout_popup);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        TextView txtYes = dialog.findViewById(R.id.btnYes);
        TextView txtNo = dialog.findViewById(R.id.btnNo);
        TextView txtMessage = dialog.findViewById(R.id.txt_message);
        txtMessage.setText("You are buying this video for "+video.getListedPrice() + " coins");

        txtNo.setOnClickListener((View v) -> dialog.dismiss());

        txtYes.setOnClickListener((View v) -> {
            dialog.dismiss();
            //Toast.makeText(PayPerViewPlansActivity.this,"Payment Done", Toast.LENGTH_SHORT).show();
            sendPaytmPaymentToBackend(video.getAdminVideoId(),null);
            //logOutConfirm();
        });

        dialog.show();
        Window window = dialog.getWindow();
        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        //window.setGravity(Gravity.CENTER);
    }

    public void showAgePicker()
    {

        //Now we need an AlertDialog.Builder object
        dialog = new Dialog(PayPerViewPlansActivity.this);

        dialog.setContentView(R.layout.dialog_age_selection);
        dialog.setCancelable(false);
        WindowManager.LayoutParams params = dialog.getWindow().getAttributes(); // change this to your dialog.

        params.y = 10; // Here is the param to set your dialog position. Same with params.x
        dialog.getWindow().setAttributes(params);
        dialog.getWindow().setGravity(Gravity.BOTTOM);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        LinearLayout ll_subscription = dialog.findViewById(R.id.ll_subscription);
        ll_subscription.setVisibility(View.GONE);

        TextView txt_state = dialog.findViewById(R.id.txt_state);
        ImageView img_state = dialog.findViewById(R.id.img_state);
        if(CURRENT_COUNTRY.equalsIgnoreCase("India")) {
            txt_state.setText(CURRENT_REGION);

            LinearLayout ll_state = dialog.findViewById(R.id.ll_state);
            if (STATES_PAYMENT != null && STATES_PAYMENT.length > 1) {
                img_state.setVisibility(View.VISIBLE);
                ll_state.setOnClickListener(v -> showStatesPopup(txt_state));
            }else
            {
                ll_state.setVisibility(View.GONE);
                UiUtils.log(TAG,"STATES_PAYMENT is NULL");
            }
        }
        else {
            txt_state.setText(CURRENT_COUNTRY);
            img_state.setVisibility(View.GONE);
        }

        CheckBox checkBox =dialog.findViewById(R.id.termsConditionCheck);
        TextView termsCondition = dialog.findViewById(R.id.termsCondition);
        String termsAndConditions = getResources().getString(R.string.terms_conditions);
        String privacyPolicy = getResources().getString(R.string.privacy_policy);
        String refundPolicy = getResources().getString(R.string.refund_policy);

        String policy = getResources().getString(R.string.agree_text) + " "+ termsAndConditions +" and " + privacyPolicy +" and " + refundPolicy;
        SpannableString ss = new SpannableString(policy);
        ClickableSpan span1 = new ClickableSpan() {
            @Override
            public void onClick(View textView) {
                startActivity(new Intent(PayPerViewPlansActivity.this, WebViewActivity.class)
                        .putExtra(WebViewActivity.PAGE_TYPE, WebViewActivity.PageTypes.TERMS));
            }
        };

        ClickableSpan span2 = new ClickableSpan() {
            @Override
            public void onClick(View textView) {
                startActivity(new Intent(PayPerViewPlansActivity.this, WebViewActivity.class)
                        .putExtra(WebViewActivity.PAGE_TYPE, WebViewActivity.PageTypes.PRIVACY));
            }
        };

        ClickableSpan span3 = new ClickableSpan() {
            @Override
            public void onClick(View textView) {
                startActivity(new Intent(PayPerViewPlansActivity.this, WebViewActivity.class)
                        .putExtra(WebViewActivity.PAGE_TYPE, WebViewActivity.PageTypes.REFUND));
            }
        };

        ss.setSpan(span1, 13, 31, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss.setSpan(span2, 36, 50, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss.setSpan(span3, 55, 68, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        termsCondition.setText(ss);
        termsCondition.setMovementMethod(LinkMovementMethod.getInstance());

        RecyclerView rv_age = dialog.findViewById(R.id.rv_age);
        setAgeListAdapter(rv_age,checkBox);

        dialog.findViewById(R.id.cancel_btn).setOnClickListener((View v) -> { selectedAge=""; dialog.dismiss(); });

        checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            UiUtils.log(TAG,"sgfr: "+ isChecked );
            if(isChecked)
            {
                if(selectedAge.equals(""))
                    Toast.makeText(PayPerViewPlansActivity.this,"Please select age",Toast.LENGTH_SHORT).show();
                else
                    sendAgeToBackend(selectedAge);
            }
        });

        dialog.show();
    }

    private void setAgeListAdapter(RecyclerView rv_age, CheckBox checkBox) {

        List<String> ageArray = Arrays.asList(getResources().getStringArray(R.array.ageList));
        ArrayList<AgeItem> ageList = new ArrayList<>();
        for(int i=0;i<ageArray.size();i++) {
            //sendAgeToBackend();
            ageList.add(new AgeItem(ageArray.get(i), v -> {
                int itemPosition = rv_age.getChildLayoutPosition(v);
                AgeItem item = ageList.get(itemPosition);
                selectedAge = ((TextView) v.findViewById(R.id.ageName)).getText().toString();
                UiUtils.log(TAG,"sgfr: "+ item.getAgeName());
                if(checkBox.isChecked())
                    sendAgeToBackend(selectedAge);
                else
                    Toast.makeText(PayPerViewPlansActivity.this,"Please accept terms and condition",Toast.LENGTH_SHORT).show();

                clearSelection(ageList);
                item.setSelected(true);
                ageAdapter.notifyDataSetChanged();

                //showPaymentPicker(dialog)/*sendAgeToBackend(((TextView)v.findViewById(R.id.ageName)).getText().toString())*/
            }));
        }

        ageAdapter = new AgeAdapter(PayPerViewPlansActivity.this, ageList);
        rv_age.setLayoutManager(new LinearLayoutManager(PayPerViewPlansActivity.this));
        rv_age.setItemAnimator(new DefaultItemAnimator());
        rv_age.setAdapter(ageAdapter);
    }

    private void clearSelection(List<AgeItem> ageList)
    {
        for (AgeItem age : ageList) {
            // do something with object
            age.setSelected(false);
        }
    }

    private void sendAgeToBackend(String age) {
        UiUtils.showLoadingDialog(PayPerViewPlansActivity.this);

        Map<String, Object> params = new HashMap<>();
        params.put(ID, prefUtils.getIntValue(PrefKeys.USER_ID, -1));
        params.put(APIConstants.Params.TOKEN, prefUtils.getStringValue(PrefKeys.SESSION_TOKEN, ""));
        params.put(APIConstants.Params.SUB_PROFILE_ID, prefUtils.getIntValue(PrefKeys.ACTIVE_SUB_PROFILE, 0));
        params.put(APIConstants.Params.AGE, age);
        params.put(APIConstants.Params.REGION_NAME, selectedState);
        params.put(APIConstants.Params.LANGUAGE, prefUtils.getStringValue(PrefKeys.APP_LANGUAGE_STRING, ""));

        Call<String> call = apiInterface.updateAge(UPDATE_AGE, params);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                UiUtils.hideLoadingDialog();

                JSONObject homeDynamicResponse = null;
                try {
                    homeDynamicResponse = new JSONObject(response.body());
                }  catch (Exception e) {
                    UiUtils.log(TAG, Log.getStackTraceString(e));
                }
                if (homeDynamicResponse != null) {
                    if (homeDynamicResponse.optString(SUCCESS).equals(APIConstants.Constants.TRUE)) {
                        //JSONArray dynamicVideoArr = homeDynamicResponse.optJSONArray(Params.DATA);
                        //dialog.dismiss();
                        showPaymentPicker(dialog);
                        prefUtils.setValue(PrefKeys.USER_AGE, age);

                    } else {
                        UiUtils.showShortToast(PayPerViewPlansActivity.this, homeDynamicResponse.optString(ERROR_MESSAGE));
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                NetworkUtils.onApiError(PayPerViewPlansActivity.this);
            }
        });
    }

    public void showStatesPopup(TextView txt_state)
    {
        Dialog statesDialog = new Dialog(PayPerViewPlansActivity.this);

        statesDialog.setContentView(R.layout.dialog_states_selection);
        WindowManager.LayoutParams params = dialog.getWindow().getAttributes(); // change this to your dialog.

        //params.y = 100; // Here is the param to set your dialog position. Same with params.x
        //statesDialog.getWindow().setAttributes(params);
        statesDialog.getWindow().setGravity(Gravity.CENTER);
        statesDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        statesDialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        ListView simpleListView = statesDialog.findViewById(R.id.simpleListView);

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this,
                R.layout.item_state, /*R.id.stateName,*/ STATES_PAYMENT);
        simpleListView.setAdapter(arrayAdapter);

        simpleListView.setOnItemClickListener((parent, view, position, id1) ->
        {
            String value = arrayAdapter.getItem(position);
            UiUtils.log(TAG, "ITEM1: " + value);
            txt_state.setText(value);
            selectedState = value;
            statesDialog.dismiss();
        });

        /*simpleListView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                UiUtils.log(TAG,"ITEM: TEST");
                UiUtils.log(TAG,"ITEM: "+ arrayAdapter.getItem(position));
                txt_state.setText(arrayAdapter.getItem(position));
                statesDialog.dismiss();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });*/

        statesDialog.show();

    }

    public void showPaymentPicker(Dialog dialogold) {
        if (dialogold != null)
            dialogold.dismiss();
        //Now we need an AlertDialog.Builder object
        paymentDialog = new Dialog(PayPerViewPlansActivity.this);

        paymentDialog.setContentView(R.layout.dialog_payment_selection);
        WindowManager.LayoutParams params = paymentDialog.getWindow().getAttributes(); // change this to your dialog.

        params.y = 10; // Here is the param to set your dialog position. Same with params.x
        paymentDialog.getWindow().setAttributes(params);
        paymentDialog.getWindow().setGravity(Gravity.BOTTOM);
        paymentDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        paymentDialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        LinearLayout llCoins = paymentDialog.findViewById(R.id.ll_coins);
        TextView txtTotalCoins = paymentDialog.findViewById(R.id.txt_total_coins);
        TextView txtPlus = paymentDialog.findViewById(R.id.txt_plus);

        LinearLayout ll_price = paymentDialog.findViewById(R.id.ll_price);
        TextView txt_currency = paymentDialog.findViewById(R.id.txt_currency);
        TextView txt_price = paymentDialog.findViewById(R.id.txt_price);
        TextView txt_price_currency = paymentDialog.findViewById(R.id.txt_price_currency);


        if(video.getListedPrice() > LoginUser.INSTANCE.getCoins()){
            txtTotalCoins.setText(String.valueOf(LoginUser.INSTANCE.getCoins()));

            txt_currency.setText(getString(R.string.currency)/*plan.getSymbol()*/);
            txt_price.setText(String.valueOf(video.getListedPrice() - LoginUser.INSTANCE.getCoins()));
            txt_price_currency.setText("*Price in " + getString(R.string.currency)/*plan.getCode()*/);
        }else{
            txtPlus.setVisibility(View.GONE);
            ll_price.setVisibility(View.GONE);
            txtTotalCoins.setText(String.valueOf(video.getListedPrice()));
            //txt_currency.setText(getString(R.string.currency)/*plan.getSymbol()*/);
            //txt_price.setText(String.valueOf(video.getListedPrice()));
            //txt_price_currency.setText("*Price in " + getString(R.string.currency)/*plan.getCode()*/);
        }


        /*if(plan.getCode().equalsIgnoreCase(""))
            txt_price_currency.setText(getResources().getString(R.string.inr));

        if(plan.getSymbol().equalsIgnoreCase(""))
            txt_currency.setText(getResources().getString(R.string.currency));*/

        TextView txt_months = paymentDialog.findViewById(R.id.txt_months);
        String[] plans = "6 months".split("\\s");/*plan.getMonthFormatted().split("\\s");*/
        txt_months.setText(""/*String.format(" / %s %s", plans[0], plans[1])*/);

        TextView txt_plan = paymentDialog.findViewById(R.id.txt_plan);
        TextView txt_plan_txt = paymentDialog.findViewById(R.id.txt_plan_txt);
        txt_plan.setText("6 months"/*plan.getMonthFormatted()*/);


        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;

        int dp = (int) (width / Resources.getSystem().getDisplayMetrics().density);

        if(dp<400)
        {
            txt_plan.setTextSize(10);
            txt_plan_txt.setTextSize(10);
        }

        //dialog.findViewById(R.id.btnCancel).setOnClickListener((View v) -> { dialog.dismiss(); });
        TelephonyManager tm = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
        String countryCodeValue = tm.getSimCountryIso();

        /*TextView txt_state = paymentDialog.findViewById(R.id.txt_state);
        txt_state.setText(CURRENT_REGION);

        ImageView img_state = paymentDialog.findViewById(R.id.img_state);

        LinearLayout ll_state = paymentDialog.findViewById(R.id.ll_state);
        if (STATES_PAYMENT != null && STATES_PAYMENT.length > 1) {
            img_state.setVisibility(View.VISIBLE);
            ll_state.setOnClickListener(v -> {
                showStatesPopup(txt_state);
            });
        }else
        {
            ll_state.setVisibility(View.GONE);
            UiUtils.log(TAG,"STATES_PAYMENT is NULL");
        }*/

        if(video.getListedPrice() > LoginUser.INSTANCE.getCoins()) {

            LinearLayout ll_payment_with_coin = paymentDialog.findViewById(R.id.ll_payment_with_coin);
            LinearLayout ll_payment_gateway_available = paymentDialog.findViewById(R.id.ll_payment_gateway_available);
            LinearLayout ll_no_payment_gateway_available = paymentDialog.findViewById(R.id.ll_no_payment_gateway_available);
            ProgressBar loadingPaymentGateway = paymentDialog.findViewById(R.id.loadingPaymentGateway);

            ll_payment_with_coin.setVisibility(View.GONE);
            ll_payment_gateway_available.setVisibility(View.VISIBLE);
            ll_no_payment_gateway_available.setVisibility(View.GONE);
            loadingPaymentGateway.setVisibility(View.VISIBLE);

            //api calling
            Call paymentCall = apiInterface.getPaymentGatewayCountryWise(GET_PAYMENT_GATEWAY + CURRENT_IP);
            paymentCall.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    UiUtils.hideLoadingDialog();
                    JSONArray plansArray = null;
                    try {
                        plansArray = new JSONArray(response.body());
                    } catch (Exception e) {
                        UiUtils.log(TAG, Log.getStackTraceString(e));
                    }

                    try {
                        if (plansArray != null) {

                            ll_payment_gateway_available.setVisibility(View.VISIBLE);
                            loadingPaymentGateway.setVisibility(View.VISIBLE);

                            if (plansArray.getJSONObject(0) == null) {
                                ll_no_payment_gateway_available.setVisibility(View.VISIBLE);
                                ll_payment_gateway_available.setVisibility(View.GONE);
                            } else {
                                UiUtils.log(TAG, "length: " + plansArray.length());
                                for (int i = 0; i < plansArray.length(); i++) {

                                    try {
                                        View singlePaymentType = getLayoutInflater().inflate(R.layout.payment_gateway_row, null);
                                        TextView gatewayName = (TextView) singlePaymentType.findViewById(R.id.payment_gateway_name);
                                        gatewayName.setText(plansArray.getJSONObject(i).getString(PAYMENT_GATEWAY_NAME));

                                        String paymentType = plansArray.getJSONObject(i).getString(PAYMENT_GATEWAY_TYPE);
                                        UiUtils.log(TAG, "paymentType: " + paymentType);

                                        boolean matchFound = setPaymentTitleAndIcon(paymentType);

                                        //IMPORTANT !!! ADD MORE IF ELSE CONDITIONS TO ADD MORE GATEWAYS...DONT CHANGE LOGIC HERE
                                        singlePaymentType.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {

                                                if (paymentType.equalsIgnoreCase(TYPE_PAYTM_KEY)) {

                                                    UiUtils.log(TAG, "paymentType: " + "onStartTransaction");

                                                    onStartTransaction(paymentType);

                                                } else if (paymentType.equalsIgnoreCase(TYPE_CARD_KEY)) {

                                                    UiUtils.log(TAG, "paymentType: " + "onStartTransaction");
                                                    onStartTransaction(paymentType);

                                                } else if (paymentType.equalsIgnoreCase(TYPE_UPI_KEY)) {

                                                    UiUtils.log(TAG, "paymentType: " + "onStartTransaction");
                                                    onStartTransaction(paymentType);
                                                /*Intent upiIntent = new Intent(PlansActivity.this,PaytmUPIActivity.class);
                                                Bundle b = new Bundle();
                                                b.putSerializable(PLAN,plan);
                                                upiIntent.putExtras(b);
                                                startActivityForResult(upiIntent, UPI_INTENT);*/

                                                } else if (paymentType.equalsIgnoreCase(TYPE_AGREE_PAY_KEY)) {

                                                    UiUtils.log(TAG, "paymentType: " + "onStartAgreePayTransaction");
                                                    onStartAgreePayTransaction();
                                                /*Intent intent = new Intent(PlansActivity.this, GoogleInAppPurchaseActivity.class);
                                                intent.putExtra("subscriptionDetails", plan);
                                                startActivityForResult(intent, IN_APP_GATEWAY);*/

                                                } else if (paymentType.equalsIgnoreCase(TYPE_GOOGLE_IN_APP_KEY)) {

                                                    //onStartGooglePlayBillingTransaction();
                                                /*Intent intent = new Intent(PlansActivity.this, GoogleInAppPurchaseActivity.class);
                                                intent.putExtra("subscriptionDetails", plan);
                                                startActivityForResult(intent, IN_APP_GATEWAY);*/

                                                } else if (paymentType.equalsIgnoreCase("RAZORPAY")) {

                                        /*bottomSheetDialog.dismiss();

                                        getOrderId(subscriptionPackage);*/

                                                } else if (paymentType.equalsIgnoreCase("PAYPAL")) {

                                        /*bottomSheetDialog.dismiss();
                                        Intent intent = new Intent(getActivity(), PaypalActivity.class);
                                        intent.putExtra("subscriptionDetails", new Gson().toJson(subscriptionPackage));
                                        startActivityForResult(intent, PAYPAL_GATEWAY);
*/
                                                } else if (paymentType.equalsIgnoreCase("GOOGLE_INAPP_V2")) {

                                        /*bottomSheetDialog.dismiss();
                                        Intent intent = new Intent(getActivity(), GoogleInAppPurchaseActivity.class);
                                        intent.putExtra("subscriptionDetails", new Gson().toJson(subscriptionPackage));
                                        startActivityForResult(intent, IN_APP_GETWAY);*/

                                                } else if (paymentType.equalsIgnoreCase("PAYTM_UPI")) {

                                        /*bottomSheetDialog.dismiss();
                                        Intent intent = new Intent(getActivity(), PaytmUPIActivity.class);
                                        intent.putExtra("subscriptionDetails", new Gson().toJson(subscriptionPackage));
                                        startActivityForResult(intent, IN_APP_GETWAY);*/

                                                } else if (paymentType.equalsIgnoreCase("STRIPE")) {

                                       /* bottomSheetDialog.dismiss();
                                        Intent intent = new Intent(getActivity(), StripePaymentActivity.class);
                                        intent.putExtra("subscriptionDetails", new Gson().toJson(subscriptionPackage));
                                        startActivityForResult(intent, STRIPE_GETWAY);*/

                                                } else if (paymentType.equalsIgnoreCase("PAYPAL_INDIA")) {

                                        /*bottomSheetDialog.dismiss();
                                        Intent intent = new Intent(getActivity(), PaypalIndiaActivity.class);
                                        intent.putExtra("subscriptionDetails", new Gson().toJson(subscriptionPackage));
                                        startActivityForResult(intent, PAYPAL_INDIA_GETWAY);*/

                                                } else if (paymentType.equalsIgnoreCase("PAYPAL_AUTO_RENEW")) {

                                        /*bottomSheetDialog.dismiss();
                                        Intent intent = new Intent(getActivity(), PaypalAutoRenewActivity.class);
                                        intent.putExtra("subscriptionDetails", new Gson().toJson(subscriptionPackage));
                                        startActivityForResult(intent, PAYPAL_AUTO_RENEW_GETWAY);*/

                                                } else if (paymentType.equalsIgnoreCase("STRIP_RECURRING")) {

                                        /*bottomSheetDialog.dismiss();
                                        Intent intent = new Intent(getActivity(), StripRecurringPaymentActivity.class);
                                        intent.putExtra("subscriptionDetails", new Gson().toJson(subscriptionPackage));
                                        startActivityForResult(intent, STRIP_AUTO_RENUE_GETWAY);*/
                                                }

                                            }
                                        });

                                        if (matchFound)
                                            ll_payment_gateway_available.addView(singlePaymentType);

                                    } catch (Exception e) {
                                        UiUtils.log(TAG, Log.getStackTraceString(e));
                                    }
                                }

                                ll_payment_gateway_available.setVisibility(View.VISIBLE);
                                loadingPaymentGateway.setVisibility(View.GONE);
                            }

                        } else {
                            ll_no_payment_gateway_available.setVisibility(View.VISIBLE);
                            ll_payment_gateway_available.setVisibility(View.GONE);
                        }
                    } catch (Exception e) {
                        UiUtils.log(TAG, Log.getStackTraceString(e));
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    loadingPaymentGateway.setVisibility(View.GONE);
                    NetworkUtils.onApiError(PayPerViewPlansActivity.this);
                }
            });

       /* paymentDialog.findViewById(R.id.paytm).setOnClickListener((View v) -> {  onStartTransaction(v); });
        paymentDialog.findViewById(R.id.card).setOnClickListener((View v) -> {  onStartTransaction(v); });
        paymentDialog.findViewById(R.id.upi).setOnClickListener((View v) -> {
            Intent upiIntent = new Intent(this,PaytmUPIActivity.class);
            Bundle b = new Bundle();
            b.putSerializable(PLAN,plan);
            upiIntent.putExtras(b);
            startActivityForResult(upiIntent, UPI_INTENT);
            //startPayment();
        });
        paymentDialog.findViewById(R.id.inApp).setOnClickListener((View v) -> {
            Toast.makeText(PlansActivity.this,"Coming Soon!!",Toast.LENGTH_SHORT).show(); });
        paymentDialog.findViewById(R.id.paypal).setOnClickListener((View v) -> {   getPayPalPayment(); });
        paymentDialog.findViewById(R.id.google_pay).setOnClickListener((View v) -> {

            try {
                if (!countryCodeValue.equalsIgnoreCase("in")) {
                    requestPayment(v);
                } else {
                    Uri uri =
                            new Uri.Builder()
                                    .scheme("upi")
                                    .authority("pay")
                                    .appendQueryParameter("pa", "test@axisbank")
                                    .appendQueryParameter("pn", "Test Merchant")
                                    .appendQueryParameter("mc", "1234")
                                    .appendQueryParameter("tr", "123456789")
                                    .appendQueryParameter("tn", invoice.getTitle())
                                    .appendQueryParameter("am", String.valueOf(invoice.getTotalAmount()))
                                    .appendQueryParameter("cu", "INR")
                                    .appendQueryParameter("url", "https://test.merchant.website")
                                    .build();
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(uri);
                    intent.setPackage(GOOGLE_TEZ_PACKAGE_NAME);
                    startActivityForResult(intent, TEZ_REQUEST_CODE);
                }
            } catch (Exception e) {
                UiUtils.log(TAG, Log.getStackTraceString(e));
            }
        });
        paymentDialog.findViewById(R.id.stripe).setOnClickListener(v -> new AlertDialog.Builder(PlansActivity.this)
                .setTitle(getString(R.string.are_you_sure_you_want_to_pay) + invoice.getCurrency() + invoice.getPaidAmount() + " ?")
                .setPositiveButton(getResources().getString(R.string.yes), (dialogInterface, as) ->
                        sendStripePaymentToBackend(invoice.isPayingForPlan(),
                                invoice.isPayingForPlan() ? plan.getId() : video.getAdminVideoId()))
                .setNegativeButton(getResources().getString(R.string.no), (dialogInterface, as) -> dialogInterface.cancel()).create().show());*/

        }else{

            TextView button_pay_with_coin = paymentDialog.findViewById(R.id.button_pay_with_coin);

            button_pay_with_coin.setOnClickListener(v-> {
                sendPaytmPaymentToBackend(video.getAdminVideoId(),null);
                //Toast.makeText(PayPerViewPlansActivity.this,"Payment Done", Toast.LENGTH_SHORT).show();
            });
        }

        paymentDialog.show();
    }

    private boolean setPaymentTitleAndIcon(String paymentType) {

        if (paymentType.equalsIgnoreCase(TYPE_PAYTM_KEY)) {
            return true;
        }else if (paymentType.equalsIgnoreCase(TYPE_CARD_KEY)) {
            return true;
        } else if (paymentType.equalsIgnoreCase(TYPE_UPI_KEY)) {
            return true;
        } else if (paymentType.equalsIgnoreCase(TYPE_AGREE_PAY_KEY)) {
            return true;
        }/*else if (paymentType.equalsIgnoreCase(getTypeGoogleInAppKeys())) {
            if(!checkString(plan.getGooglePlayProductId()))
                return true;
        }*//*else if (paymentType.equalsIgnoreCase("RAZORPAY")) {

            tvPaymentTypeText.setText("Cards / UPI / Netbanking");
            //Glide.with(getActivity()).load(getResources().getIdentifier("img_razorpay", "drawable", getActivity().getPackageName())).into(ivPaymentOptionIcon);

            matchFound = true;
        } else if (paymentType.equalsIgnoreCase("PAYPAL")) {

            tvPaymentTypeText.setText("PAYPAL");
            //Glide.with(getActivity()).load(getResources().getIdentifier("img_paypal", "drawable", getActivity().getPackageName())).into(ivPaymentOptionIcon);


            matchFound = true;
        } else if(paymentType.equalsIgnoreCase("GOOGLE_INAPP_V2")){

            tvPaymentTypeText.setText("Google In-App Purchase");
            //Glide.with(getActivity()).load(getResources().getIdentifier("img_google_play", "drawable", getActivity().getPackageName())).into(ivPaymentOptionIcon);
            matchFound = true;
        }else if(paymentType.equalsIgnoreCase("PAYTM_UPI")){

            tvPaymentTypeText.setText("Google Pay / UPI / WhatsApp UPI");
            //Glide.with(getActivity()).load(getResources().getIdentifier("img_upi_gateway", "drawable", getActivity().getPackageName())).into(ivPaymentOptionIcon);
            matchFound = true;
        }else if(paymentType.equalsIgnoreCase("STRIPE")){

            tvPaymentTypeText.setText("International Cards");
            //Glide.with(getActivity()).load(getResources().getIdentifier("image_stripe", "drawable", getActivity().getPackageName())).into(ivPaymentOptionIcon);
            matchFound = true;
        }else if (paymentType.equalsIgnoreCase("PAYTM_CARDS")) {

            tvPaymentTypeText.setText("Cards / UPI / Netbanking");
            //Glide.with(getActivity()).load(getResources().getIdentifier("img_paytm", "drawable", getActivity().getPackageName())).into(ivPaymentOptionIcon);
            matchFound = true;
        }else if (paymentType.equalsIgnoreCase("PAYPAL_INDIA")){

            tvPaymentTypeText.setText("PayPal India / Credit and Debit cards");
            //Glide.with(getActivity()).load(getResources().getIdentifier("img_paypal", "drawable", getActivity().getPackageName())).into(ivPaymentOptionIcon);
            matchFound = true;
        }else if (paymentType.equalsIgnoreCase("PAYPAL_AUTO_RENEW")){

            tvPaymentTypeText.setText("PayPal Auto renew");
            //Glide.with(getActivity()).load(getResources().getIdentifier("img_paypal", "drawable", getActivity().getPackageName())).into(ivPaymentOptionIcon);
            matchFound = true;
        }else if (paymentType.equalsIgnoreCase("STRIP_RECURRING")){

            tvPaymentTypeText.setText("Strip Auto renew");
            // Glide.with(getActivity()).load(getResources().getIdentifier("img_paypal", "drawable", getActivity().getPackageName())).into(ivPaymentOptionIcon);
            matchFound = true;
        }*/

        return false;

    }

    //Paytm
    private void onStartTransaction(String paymentType) {

        //PaytmPGService Service = PaytmPGService.getStagingService();

        //Kindly create complete Map and checksum on your server side and then put it here in paramMap.

        paytmParamMap = new HashMap<>();
        String id = initOrderId();

        //Staging
        paytmParamMap.put( MID , TEST_MID_KEY);
        // Key in your staging and production MID available in your dashboard
        paytmParamMap.put( PARAM_ORDER_ID , id);
        paytmParamMap.put( PARAM_CUST_ID , String.valueOf(prefUtils.getIntValue(PrefKeys.USER_ID, -1)));
        //paytmParamMap.put( "MOBILE_NO" , "7777777777");
        //paytmParamMap.put( "EMAIL" , "username@emailprovider.com");
        paytmParamMap.put( PARAM_CHANNEL_ID , TEST_CHANNEL_ID_KEY);
        paytmParamMap.put( PARAM_TXN_AMOUNT , String.valueOf(video.getListedPrice() - LoginUser.INSTANCE.getCoins()));
        paytmParamMap.put( WEBSITE , TEST_WEBSITE_KEY);
        // This is the staging value. Production value is available in your dashboard
        paytmParamMap.put( PARAM_INDUSTRY_TYPE_ID , TEST_INDUSTRY_TYPE_KEY);
        // This is the staging value. Production value is available in your dashboard
        paytmParamMap.put( PAYTM_CALLBACK_URL, TEST_CALLBACK_URL+id);


        //Production
        /*paytmParamMap.put( MID , LIVE_MID_KEY);
        // Key in your staging and production MID available in your dashboard
        paytmParamMap.put( PARAM_ORDER_ID , id);
        paytmParamMap.put( PARAM_CUST_ID , String.valueOf(prefUtils.getIntValue(PrefKeys.USER_ID, -1)));
        //paytmParamMap.put( "MOBILE_NO" , "7777777777");
        //paytmParamMap.put( "EMAIL" , "username@emailprovider.com");
        paytmParamMap.put( PARAM_CHANNEL_ID , LIVE_CHANNEL_ID_KEY);
        paytmParamMap.put( PARAM_TXN_AMOUNT , String.valueOf(invoice.getPaidAmount()));
        paytmParamMap.put( WEBSITE , LIVE_WEBSITE_KEY);
        // This is the staging value. Production value is available in your dashboard
        paytmParamMap.put( PARAM_INDUSTRY_TYPE_ID , LIVE_INDUSTRY_TYPE_KEY);
        //paytmParamMap.put( "paymentMode" , "UPI_INTENT");
        // This is the staging value. Production value is available in your dashboard
        paytmParamMap.put( PAYTM_CALLBACK_URL, LIVE_CALLBACK_URL+id);*/
        paytmParamMap.put(PAYMENT_GATEWAY_TYPE,paymentType);

        getChecksum(id,paytmParamMap);
    }

    private String initOrderId() {
        Random r = new Random(System.currentTimeMillis());
        String orderId =  String.format(Locale.ENGLISH,"ORDS_%05d_%d_%d", r.nextInt(100000),1/*plan.getId()*/, id);
        /*String orderId = "ORDER" + (1 + r.nextInt(2)) * 10000
                + r.nextInt(10000) + "_" + plan.getId();*/
        return orderId;
    }

    private void getChecksum(final String orderId , final HashMap<String,String> paramMap) {

        Map<String, Object> params = new HashMap<>();
        params.put(APIConstants.Params.ID, id);
        params.put(APIConstants.Params.TOKEN, token);
        params.put(APIConstants.Params.ORDER_ID, paytmParamMap.get(PARAM_ORDER_ID));
        params.put(CUST_ID, paytmParamMap.get(PARAM_CUST_ID));
        params.put(INDUSTRY_TYPE_ID, paytmParamMap.get( PARAM_INDUSTRY_TYPE_ID));
        params.put(CHANNEL_ID, paytmParamMap.get( PARAM_CHANNEL_ID));
        params.put(TXN_AMOUNT, paytmParamMap.get( PARAM_TXN_AMOUNT));
        params.put(APIConstants.Params.CALLBACK_URL, paytmParamMap.get( PAYTM_CALLBACK_URL));
        params.put(PAYMENT_GATEWAY_TYPE,paytmParamMap.get(PAYMENT_GATEWAY_TYPE));

        Call<String> call = apiInterface.getPaytmChecksum(PAYTM_CHECKSUM, params);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                UiUtils.hideLoadingDialog();
                JSONObject checksumObj = null;
                try {
                    checksumObj = new JSONObject(response.body());
                } catch (Exception e) {
                    UiUtils.log(TAG, Log.getStackTraceString(e));
                }
                try {
                    if (checksumObj != null && checksumObj.getBoolean(SUCCESS)) {
                        String checksum = checksumObj.optString(APIConstants.Params.DATA);
                        UiUtils.log("Checksum","Value: "+ checksum);
                        paramMap.put( CHECKSUMHASH , checksum);
                        paramMap.put( TXN_TOKEN , checksum);

                        startPaytmService(paramMap);

                        //UiUtils.showShortToast(getApplicationContext(), availablePlansObj.optString(Params.ERROR_MESSAGE));


                    }
                } catch (JSONException e) {
                    UiUtils.log(TAG, Log.getStackTraceString(e));
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                UiUtils.hideLoadingDialog();
                //noResultLayout.setVisibility(View.VISIBLE);
                NetworkUtils.onApiError(PayPerViewPlansActivity.this);
            }
        });

    }

    private void startPaytmService(HashMap<String, String> paramMap)
    {
        PaytmOrder paytmOrder = new PaytmOrder(paytmParamMap.get(PARAM_ORDER_ID),paytmParamMap.get(MID),paytmParamMap.get(TXN_TOKEN),
                paytmParamMap.get(TXN_AMOUNT),paytmParamMap.get(PAYTM_CALLBACK_URL));
        TransactionManager transactionManager = new TransactionManager(paytmOrder, new PaytmPaymentTransactionCallback() {
            @Override
            public void onTransactionResponse(@Nullable Bundle inResponse) {

                if(inResponse!=null) {
                    UiUtils.log("onTransactionResponse", "Payment Transaction : " + inResponse);
                    //UiUtils.log("onTransactionResponse", "Payment Transaction : " + inResponse.getString(Params.PAYTM_STATUS));
                    if (inResponse.getString(APIConstants.Params.PAYTM_STATUS).equalsIgnoreCase("TXN_SUCCESS")) {
                        sendPaytmPaymentToBackend(video.getAdminVideoId(), inResponse);
                    } else {
                        paymentsInterface.onPaymentFailed("Something");
                    }
                }else
                {
                    paymentsInterface.onPaymentFailed("Something");
                }
            }

            @Override
            public void networkNotAvailable() {
                UiUtils.log("LOG", "Payment Transaction : Network error");
                Toast.makeText(getApplicationContext(), "Network connection error: Check your internet connectivity", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onErrorProceed(String inErrorMessage) {
                UiUtils.log("LOG", "onErrorProceed : "+ inErrorMessage);
            }

            @Override
            public void clientAuthenticationFailed(String authFailedMessage) {
                UiUtils.log("clientAuthenticationFailed", "Payment Transaction : " + authFailedMessage);
                Toast.makeText(getApplicationContext(), "Authentication failed: Server error" + authFailedMessage, Toast.LENGTH_LONG).show();
                paymentsInterface.onPaymentFailed(authFailedMessage);
            }

            @Override
            public void someUIErrorOccurred(String uiErrorMessages) {
                try {
                    UiUtils.log("someUIErrorOccurred", "Payment Transaction : " + uiErrorMessages);
                    PayPerViewPlansActivity.this.finalize();
                } catch (Throwable throwable) {
                    UiUtils.log(TAG, Log.getStackTraceString(throwable));
                }
                paymentsInterface.onPaymentFailed(uiErrorMessages);
            }

            @Override
            public void onErrorLoadingWebPage(int iniErrorCode,
                                              String inErrorMessage, String inFailingUrl) {
                UiUtils.log("onErrorLoadingWebPage", "Payment Transaction : " + inErrorMessage);
                Toast.makeText(getApplicationContext(), "Unable to load webpage " + inErrorMessage, Toast.LENGTH_LONG).show();
                paymentsInterface.onPaymentFailed(inErrorMessage);
            }

            @Override
            public void onBackPressedCancelTransaction() {
                UiUtils.log("onBackPressedCancelTransaction", "Payment Transaction : " + "Transaction cancelled");
                Toast.makeText(getApplicationContext(), "Transaction cancelled" , Toast.LENGTH_LONG).show();
            }

            @Override
            public void onTransactionCancel(String inErrorMessage, Bundle inResponse) {
                UiUtils.log("onTransactionCancel", "Payment Transaction Failed " + inErrorMessage);
                Toast.makeText(getApplicationContext(), "Transaction Cancelled" + inResponse.toString(), Toast.LENGTH_LONG).show();
                //Toast.makeText(getApplicationContext(), "Transaction Cancelled", Toast.LENGTH_LONG).show();
                paymentsInterface.onPaymentFailed("Something");
            }
        });

        transactionManager.setShowPaymentUrl("https://securegw-stage.paytm.in/theia/api/v1/showPaymentPage");
        transactionManager.setAppInvokeEnabled(false);
        transactionManager.setEmiSubventionEnabled(true);
        //transactionManager.setEnableAssist(true);
        transactionManager.startTransaction(this, PAYTM);


        //hideKeyboard(PlansActivity.this);
        //transactionManager.startTransactionAfterCheckingLoginStatus(this, clientId, 100);
    }

    private void sendPaytmPaymentToBackend(int id, Bundle inResponse) {
        PrefUtils prefUtils = PrefUtils.getInstance(this);
        HashMap<String,String> deviceDetails =  Utils.getDeviceDetails(this);
        UiUtils.showLoadingDialog(this);

        Map<String, Object> params = new HashMap<>();
        params.put(APIConstants.Params.ID, prefUtils.getIntValue(PrefKeys.USER_ID, -1));
        params.put(APIConstants.Params.TOKEN, prefUtils.getStringValue(PrefKeys.SESSION_TOKEN, ""));
        params.put(APIConstants.Params.SUB_PROFILE_ID, prefUtils.getIntValue(PrefKeys.ACTIVE_SUB_PROFILE, -1));
        params.put(APIConstants.Params.ADMIN_VIDEO_ID, id);
        params.put(APIConstants.Params.SUBSCRIPTION_ID, 1/*plan.getId()*/);
        //params.put(APIConstants.Params.COUPON_CODE, invoice.getCouponCode());
        params.put(APIConstants.Params.LANGUAGE, prefUtils.getStringValue(PrefKeys.APP_LANGUAGE_STRING, ""));
        params.put(APIConstants.Params.TOTAL_AMT, video.getListedPrice());
        if(inResponse== null){
            //params.put(APIConstants.Params.PAYTM_STATUS, "");
            params.put(CHECKSUMHASH, "");
            params.put(ORDERID, initOrderId());
            params.put(APIConstants.Params.TXNAMOUNT, video.getListedPrice());
            params.put(APIConstants.Params.TXNDATE, "1");
            params.put(APIConstants.Params.MID, "1");
            params.put(APIConstants.Params.TXNID, "1");
            params.put(APIConstants.Params.RESPCODE, "1");
            params.put(APIConstants.Params.PAYMENTMODE, "1");
            params.put(APIConstants.Params.BANKTXNID, "1");
            params.put(APIConstants.Params.CURRENCY, "1");
            params.put(APIConstants.Params.GATEWAYNAME, "1");
            params.put(APIConstants.Params.RESPMSG, "1");
            params.put(APIConstants.Params.BANK_NAME, "1");
            params.put(APIConstants.Params.PAYTM_STATUS, "TXN_SUCCESS");
        }else{
            params.put(CHECKSUMHASH, inResponse.getString(CHECKSUMHASH));
            params.put(ORDERID, inResponse.getString(ORDERID));
            params.put(APIConstants.Params.PAYTM_STATUS, inResponse.getString(APIConstants.Params.PAYTM_STATUS));
            params.put(APIConstants.Params.TXNAMOUNT, inResponse.getString(APIConstants.Params.TXNAMOUNT));
            params.put(APIConstants.Params.TXNDATE, inResponse.getString(APIConstants.Params.TXNDATE));
            params.put(APIConstants.Params.MID, inResponse.getString(APIConstants.Params.MID));
            params.put(APIConstants.Params.TXNID, inResponse.getString(APIConstants.Params.TXNID));
            params.put(APIConstants.Params.RESPCODE, inResponse.getString(APIConstants.Params.RESPCODE));
            params.put(APIConstants.Params.PAYMENTMODE, inResponse.getString(APIConstants.Params.PAYMENTMODE));
            params.put(APIConstants.Params.BANKTXNID, inResponse.getString(APIConstants.Params.BANKTXNID));
            params.put(APIConstants.Params.CURRENCY, inResponse.getString(APIConstants.Params.CURRENCY.toUpperCase()));
            params.put(APIConstants.Params.GATEWAYNAME, inResponse.getString(APIConstants.Params.GATEWAYNAME));
            params.put(APIConstants.Params.RESPMSG, inResponse.getString(APIConstants.Params.RESPMSG));
            params.put(APIConstants.Params.BANK_NAME, checkString(inResponse.getString(APIConstants.Params.BANK_NAME))?"":inResponse.getString(APIConstants.Params.BANK_NAME));
        }

        params.put(APIConstants.Params.PHONEDETAILS, new JSONObject(deviceDetails).toString());
        params.put(APIConstants.Params.APPVERSION, deviceDetails.get(APPVERSION));
        params.put(VERSION_CODE, deviceDetails.get(VERSION_CODE));
        params.put(APIConstants.Params.DEVICE, deviceDetails.get(DEVICE));
        params.put(APIConstants.Params.BEBUEXT, deviceDetails.get(BEBUEXT));
        params.put(APIConstants.Params.BRAND, deviceDetails.get(BRAND));
        params.put(APIConstants.Params.MODEL, deviceDetails.get(MODEL));
        params.put(APIConstants.Params.VERSION, deviceDetails.get(VERSION));
        params.put(APIConstants.Params.PLAT, deviceDetails.get(PLAT));
        params.put(APIConstants.Params.IP, CURRENT_IP);
        params.put(DISTRIBUTOR, APIConstants.Constants.DISTRIBUTOR);
        params.put(REFERRER, checkString(prefUtils.getStringValue(PrefKeys.REFERENCE_URL_CODE,""))? getResources().getString(R.string.app_name) : prefUtils.getStringValue(PrefKeys.REFERENCE_URL_CODE,""));
        params.put(REF_ID, prefUtils.getStringValue(PrefKeys.REFERENCE_URL_ID,""));
        params.put("ott_id", prefUtils.getIntValue(PrefKeys.USER_ID, -1));
        params.put(DEVICE_CODE, getSecureId(PayPerViewPlansActivity.this));

        Call<String> call = apiInterface.makePaytmPayment(MAKE_PAYTM_PAYMENT, params);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                UiUtils.hideLoadingDialog();
                JSONObject paymentObject = null;
                try {
                    paymentObject = new JSONObject(response.body());
                } catch (Exception e) {
                    UiUtils.log(TAG, Log.getStackTraceString(e));
                }
                if (paymentObject != null) {
                    if (paymentObject.optString(SUCCESS).equals(APIConstants.Constants.TRUE)) {
                        prefUtils.setValue(PrefKeys.REFERENCE_URL_CODE,"");
                        prefUtils.setValue(PrefKeys.REFERENCE_URL_ID,"");

                        try {
                            SharedPreferenceHelper sharedPreferenceHelper = new SharedPreferenceHelper(PayPerViewPlansActivity.this);
                            int nowCoins = sharedPreferenceHelper.getIntValue(PrefKeysDD.Companion.getUSER_COINS(),0) - video.getListedPrice().intValue();
                            sharedPreferenceHelper.setValue(PrefKeysDD.Companion.getUSER_COINS(), nowCoins);
                            LoginUser.INSTANCE.setCoins(nowCoins);
                        }catch (Exception e)
                        {
                            UiUtils.log(TAG, Log.getStackTraceString(e));
                        }


                        UiUtils.showShortToast(PayPerViewPlansActivity.this, paymentObject.optString(APIConstants.Params.MESSAGE)/* +
                                (!invoice.isPayingForPlan() ? "\n" + getString(R.string.play_automatically) : "")*/);
                        JSONObject payObject = paymentObject.optJSONObject(APIConstants.Params.DATA);
                        String paymentId = payObject.optString(APIConstants.Params.PAYMENT_ID);

                        invoice.setPaymentId(paymentId);
                        //finish();
                        paymentsInterface.onPaymentSucceeded(invoice);
                    } else {
                        UiUtils.hideLoadingDialog();
                        UiUtils.showShortToast(PayPerViewPlansActivity.this, paymentObject.optString(ERROR_MESSAGE));
                        paymentsInterface.onPaymentFailed(paymentObject.optString(ERROR_MESSAGE));
                        /*switch (paymentObject.optInt(Params.ERROR_CODE)) {
                            case APIConstants.ErrorCodes.NEED_TO_SUBSCRIBE_TO_MAKE_PAYMENT:
                                Intent i = new Intent(PlansActivity.this, PaymentsActivity.class);
                                startActivity(i);
                                break;
                            case APIConstants.ErrorCodes.NO_DEFAULT_CARD_FOUND:
                            default:
                                Intent toAddCard = new Intent(getApplicationContext(), AddCardActivity.class);
                                startActivity(toAddCard);
                                break;
                        }*/
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                NetworkUtils.onApiError(PayPerViewPlansActivity.this);
            }
        });
    }

    //Agreepay
    private void onStartAgreePayTransaction()
    {
        try {
            String id = initOrderId();

            String phone = prefUtils.getStringValue(PrefKeys.USER_MOBILE, "9988776655");
            //UiUtils.log(TAG, "phone: " + prefUtils.getStringValue(PrefKeys.USER_MOBILE, "9988776655"));
            if(checkString(phone))
                phone = "9988776655";

            String email = prefUtils.getStringValue(PrefKeys.USER_EMAIL, "devteam@81tech.app");
            if(checkString(email))
                email = "devteam@81tech.app";

            PaymentParams pgPaymentParams = new PaymentParams();
            pgPaymentParams.setAPiKey(AP_API_KEY);
            pgPaymentParams.setAmount(String.valueOf(video.getListedPrice() - LoginUser.INSTANCE.getCoins()));
            pgPaymentParams.setEmail(email);
            pgPaymentParams.setName(prefUtils.getStringValue(PrefKeys.USER_NAME, ""));
            pgPaymentParams.setPhone(phone);
            pgPaymentParams.setOrderId(id);
            pgPaymentParams.setCurrency(AP_CURRENCY);
            pgPaymentParams.setDescription(id);
            pgPaymentParams.setCity("Mumbai");
            pgPaymentParams.setState("Maharashtra");
            pgPaymentParams.setAddressLine1("");
            pgPaymentParams.setAddressLine2("");
            pgPaymentParams.setZipCode("400053");
            pgPaymentParams.setCountry(AP_COUNTRY);
            pgPaymentParams.setReturnUrl(AP_RETURN_URL);
            pgPaymentParams.setMode(AP_MODE);
            pgPaymentParams.setUdf1("1");
            pgPaymentParams.setUdf2("2");
            pgPaymentParams.setUdf3("3");
            pgPaymentParams.setUdf4("4");
            pgPaymentParams.setUdf5("5");
            pgPaymentParams.setEnableAutoRefund("n");
            pgPaymentParams.setOfferCode("");
            pgPaymentParams.setPaymentHostname(AP_HOST_NAME);
            //pgPaymentParams.setSplitInfo("{\"vendors\":[{\"vendor_code\":\"24VEN985\",\"split_amount_percentage\":\"20\"}]}");

            PaymentGatewayPaymentInitializer pgPaymentInitialzer = new PaymentGatewayPaymentInitializer(pgPaymentParams, PayPerViewPlansActivity.this);
            pgPaymentInitialzer.initiatePaymentProcess();
        }catch (Exception e)
        {
            e.printStackTrace();
            UiUtils.log(TAG,"ERROR: "+ e.getMessage());
        }
    }

    private void sendAgreePayPaymentToBackend( int id, JSONObject inResponse) {

        try {

            PrefUtils prefUtils = PrefUtils.getInstance(this);
            HashMap<String, String> deviceDetails = Utils.getDeviceDetails(this);
            UiUtils.showLoadingDialog(this);

            Map<String, Object> params = new HashMap<>();
            params.put(APIConstants.Params.ID, prefUtils.getIntValue(PrefKeys.USER_ID, -1));
            params.put(APIConstants.Params.TOKEN, prefUtils.getStringValue(PrefKeys.SESSION_TOKEN, ""));
            params.put(APIConstants.Params.SUB_PROFILE_ID, prefUtils.getIntValue(PrefKeys.ACTIVE_SUB_PROFILE, -1));
            params.put(APIConstants.Params.ADMIN_VIDEO_ID, id);
            params.put(APIConstants.Params.SUBSCRIPTION_ID, 1/*plan.getId()*/);
            params.put(APIConstants.Params.COUPON_CODE, invoice.getCouponCode());
            params.put(APIConstants.Params.LANGUAGE, prefUtils.getStringValue(PrefKeys.APP_LANGUAGE_STRING, ""));
            params.put(AP_RESP_MSG, inResponse.getString(AP_RESP_MSG));
            params.put(AP_ORDER_ID, inResponse.getString(AP_ORDER_ID));
            params.put(APIConstants.Params.AP_AMOUNT, inResponse.getString(APIConstants.Params.AP_AMOUNT));
            params.put(APIConstants.Params.AP_TRANSACTION_ID, inResponse.getString(APIConstants.Params.AP_TRANSACTION_ID));
            params.put(APIConstants.Params.AP_PAYMENT_METHOD, inResponse.getString(APIConstants.Params.AP_PAYMENT_MODE));
            params.put(APIConstants.Params.CURRENCY, inResponse.getString(APIConstants.Params.CURRENCY));
            params.put(AP_ERROR_MSG, inResponse.getString(AP_ERROR_MSG));
            params.put(APIConstants.Params.PHONEDETAILS, new JSONObject(deviceDetails).toString());
            params.put(APIConstants.Params.APPVERSION, deviceDetails.get(APPVERSION));
            params.put(VERSION_CODE, deviceDetails.get(VERSION_CODE));
            params.put(APIConstants.Params.DEVICE, deviceDetails.get(DEVICE));
            params.put(APIConstants.Params.BEBUEXT, deviceDetails.get(BEBUEXT));
            params.put(APIConstants.Params.BRAND, deviceDetails.get(BRAND));
            params.put(APIConstants.Params.MODEL, deviceDetails.get(MODEL));
            params.put(APIConstants.Params.VERSION, deviceDetails.get(VERSION));
            params.put(APIConstants.Params.PLAT, deviceDetails.get(PLAT));
            params.put(APIConstants.Params.IP, CURRENT_IP);
            params.put(DISTRIBUTOR, APIConstants.Constants.DISTRIBUTOR);
            params.put(REFERRER, checkString(prefUtils.getStringValue(PrefKeys.REFERENCE_URL_CODE,""))? getResources().getString(R.string.app_name) : prefUtils.getStringValue(PrefKeys.REFERENCE_URL_CODE,""));
            //params.put(REF_ID, prefUtils.getIntValue(PrefKeys.REFERENCE_URL_ID,0));
            params.put(DEVICE_CODE, getSecureId(PayPerViewPlansActivity.this));

            Call<String> call = apiInterface.makePaytmPayment(MAKE_AGREEPAY_PAYMENT, params);
            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    UiUtils.hideLoadingDialog();
                    JSONObject paymentObject = null;
                    try {
                        paymentObject = new JSONObject(response.body());
                    } catch (Exception e) {
                        UiUtils.log(TAG, Log.getStackTraceString(e));
                    }
                    if (paymentObject != null) {
                        if (paymentObject.optString(SUCCESS).equals(APIConstants.Constants.TRUE)) {
                            prefUtils.setValue(PrefKeys.REFERENCE_URL_CODE,"");

                            UiUtils.showShortToast(PayPerViewPlansActivity.this, paymentObject.optString(APIConstants.Params.MESSAGE)/* +
                                    (!invoice.isPayingForPlan() ? "\n" + getString(R.string.play_automatically) : "")*/);
                            JSONObject payObject = paymentObject.optJSONObject(APIConstants.Params.DATA);
                            String paymentId = payObject.optString(APIConstants.Params.PAYMENT_ID);
                            invoice.setPaymentId(paymentId);
                            //finish();
                            paymentsInterface.onPaymentSucceeded(invoice);
                        } else {
                            UiUtils.hideLoadingDialog();
                            UiUtils.showShortToast(PayPerViewPlansActivity.this, paymentObject.optString(ERROR_MESSAGE));
                            paymentsInterface.onPaymentFailed(paymentObject.optString(ERROR_MESSAGE));
                        /*switch (paymentObject.optInt(Params.ERROR_CODE)) {
                            case APIConstants.ErrorCodes.NEED_TO_SUBSCRIBE_TO_MAKE_PAYMENT:
                                Intent i = new Intent(PlansActivity.this, PaymentsActivity.class);
                                startActivity(i);
                                break;
                            case APIConstants.ErrorCodes.NO_DEFAULT_CARD_FOUND:
                            default:
                                Intent toAddCard = new Intent(getApplicationContext(), AddCardActivity.class);
                                startActivity(toAddCard);
                                break;
                        }*/
                        }
                    }

                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    NetworkUtils.onApiError(PayPerViewPlansActivity.this);
                }
            });
        }catch (Exception e)
        {
            UiUtils.log(TAG,"ERROR: "+ e.getMessage());
        }
    }


    @Override
    public void onPaymentSucceeded(Invoice invoice) {
        PayPerViewPlansActivity.this.runOnUiThread(new Runnable() {
            public void run() {
                try {
                    invoice.setStatus(getString(R.string.success));
                    //invoiceSheet = InvoiceBottomSheet.getInstance(invoice);
                    //invoiceSheet.show(getSupportFragmentManager(), invoiceSheet.getTag());
                    /*if(billingClient.getConnectionState()== BillingClient.ConnectionState.CONNECTED)
                        billingClient.endConnection();*/
                    if(paymentDialog!=null)
                        paymentDialog.dismiss();
                    Dialog dialogSuccess = new Dialog(PayPerViewPlansActivity.this);

                    dialogSuccess.setContentView(R.layout.dialog_payment_success);
                    WindowManager.LayoutParams params = dialogSuccess.getWindow().getAttributes(); // change this to your dialog.

                    params.y = 100; // Here is the param to set your dialog position. Same with params.x
                    dialogSuccess.getWindow().setAttributes(params);
                    dialogSuccess.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                    dialogSuccess.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    dialogSuccess.getWindow().setGravity(Gravity.CENTER);
                    dialogSuccess.setCancelable(false);

                    TextView payment_message = dialogSuccess.findViewById(R.id.payment_message);
                    payment_message.setText(String.format("Your payment of %s%s was successful.", invoice.getCurrency(), invoice.getPaidAmount()));

                    dialogSuccess.findViewById(R.id.submit_btn).setOnClickListener((View v) -> {
                        dialogSuccess.dismiss();
                        finish();
                    });

                    //finish();
                    dialogSuccess.show();

                    if(!prefUtils.getStringValue(PrefKeys.REFERENCE_URL_CODE, "").equalsIgnoreCase("")) {
                        UiUtils.log(TAG, "API Called Subscribe ");
                        Utils.sendShareLinkUrlDetailsToServer(PayPerViewPlansActivity.this, prefUtils.getStringValue(PrefKeys.REFERENCE_URL_CODE,""), SHARE_TYPE_SUBSCRIBE, prefUtils.getIntValue(PrefKeys.USER_ID, -1));
                    }

                } catch (Exception e) {
                    //finish();
                    UiUtils.log(TAG, Log.getStackTraceString(e));
                }
            }
        });
    }

    @Override
    public void onPaymentFailed(String failureReason) {
        PayPerViewPlansActivity.this.runOnUiThread(new Runnable() {
            public void run() {
                if(paymentDialog!=null)
                    paymentDialog.dismiss();

                Dialog dialog = new Dialog(PayPerViewPlansActivity.this);

                dialog.setContentView(R.layout.dialog_payment_fail);
                WindowManager.LayoutParams params = dialog.getWindow().getAttributes(); // change this to your dialog.

                params.y = 100; // Here is the param to set your dialog position. Same with params.x
                dialog.getWindow().setAttributes(params);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                dialog.getWindow().setGravity(Gravity.CENTER);
                dialog.setCancelable(false);

                TextView payment_message = dialog.findViewById(R.id.payment_message);
                //payment_message.setText();

                dialog.findViewById(R.id.support_btn).setOnClickListener((View v) -> {
                    dialog.dismiss();
                    startActivity(new Intent(PayPerViewPlansActivity.this, SupportActivity.class));
                });

                dialog.findViewById(R.id.submit_btn).setOnClickListener((View v) -> dialog.dismiss());

                dialog.show();
            }
        });
    }

    @Override
    public void onMakePayPalPayment(Invoice invoice) {

    }
}