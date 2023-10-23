package com.dd.app.ui.activity;

import static com.dd.app.network.APIConstants.APIs.VERIFY_UPI_PAYTM_PAYMENT;
import static com.dd.app.network.APIConstants.Params.APPVERSION;
import static com.dd.app.network.APIConstants.Params.BEBUEXT;
import static com.dd.app.network.APIConstants.Params.BRAND;
import static com.dd.app.network.APIConstants.Params.DEVICE;
import static com.dd.app.network.APIConstants.Params.DISTRIBUTOR;
import static com.dd.app.network.APIConstants.Params.FAILED;
import static com.dd.app.network.APIConstants.Params.MODEL;
import static com.dd.app.network.APIConstants.Params.PAYMENT_RESP;
import static com.dd.app.network.APIConstants.Params.PLAT;
import static com.dd.app.network.APIConstants.Params.SUCCESS;
import static com.dd.app.network.APIConstants.Params.VERSION;
import static com.dd.app.network.APIConstants.Params.VERSION_CODE;
import static com.dd.app.ui.activity.MainActivity.CURRENT_IP;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.dd.app.R;
import com.dd.app.network.APIConstants;
import com.dd.app.util.NetworkUtils;
import com.dd.app.util.UiUtils;
import com.dd.app.util.sharedpref.PrefKeys;
import com.dd.app.util.sharedpref.Utils;
import com.shreyaspatil.EasyUpiPayment.EasyUpiPayment;
import com.shreyaspatil.EasyUpiPayment.listener.PaymentStatusListener;
import com.shreyaspatil.EasyUpiPayment.model.TransactionDetails;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import butterknife.BindView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UPIPaymentListActivity extends AppCompatActivity implements PaymentStatusListener {

    private final String TAG = UPIPaymentListActivity.class.getSimpleName();
    private CountDownTimer countDownTimer;

    @BindView(R.id.tvMessage)
    TextView tvMessage;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upi_payment_list);


        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("ddMMyyyyHHmmss", Locale.getDefault());
        String transcId = df.format(c);

        makePayment("2.0", "7777777777@paytm", "BEBU", "3 days plan", transcId);
    }

    private void makePayment(String amount, String upi, String name, String desc, String transactionId) {
        // on below line we are calling an easy payment method and passing
        // all parameters to it such as upi id,name, description and others.
        final EasyUpiPayment easyUpiPayment = new EasyUpiPayment.Builder()
                .with(this)
                // on below line we are adding upi id.
                .setPayeeVpa(upi)
                // on below line we are setting name to which we are making payment.
                .setPayeeName(name)
                // on below line we are passing transaction id.
                .setTransactionId(transactionId)
                // on below line we are passing transaction ref id.
                .setTransactionRefId(transactionId)
                // on below line we are adding description to payment.
                .setDescription(desc)
                // on below line we are passing amount which is being paid.
                .setAmount(amount)
                // on below line we are calling a build method to build this ui.
                .build();
        // on below line we are calling a start
        // payment method to start a payment.
        easyUpiPayment.startPayment();
        // on below line we are calling a set payment
        // status listener method to call other payment methods.
        easyUpiPayment.setPaymentStatusListener(this);
    }

    @Override
    public void onTransactionCompleted(TransactionDetails transactionDetails) {
        // on below line we are getting details about transaction when completed.
        String transac = transactionDetails.getStatus().toString() + "\n" + "Transaction ID : " + transactionDetails.getTransactionId();
        //transactionDetailsTV.setVisibility(View.VISIBLE);
        // on below line we are setting details to our text view.
        //transactionDetailsTV.setText(transcDetails);
        startPollingForTransStatus();
    }

    @Override
    public void onTransactionSuccess() {
        // this method is called when transaction is successful and we are displaying a toast message.
        Toast.makeText(this, "Transaction successfully completed..", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onTransactionSubmitted() {
        // this method is called when transaction is done
        // but it may be successful or failure.
        Log.e("TAG", "TRANSACTION SUBMIT");
    }

    @Override
    public void onTransactionFailed() {
        // this method is called when transaction is failure.
        Toast.makeText(this, "Failed to complete transaction", Toast.LENGTH_SHORT).show();
        sendStatusBack(FAILED);
        try{
            if(countDownTimer!=null){
                countDownTimer.cancel();
            }
        }catch (Exception ex){ex.printStackTrace();}
    }

    @Override
    public void onTransactionCancelled() {
        // this method is called when transaction is cancelled.
        Toast.makeText(this, "Transaction cancelled..", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onAppNotFound() {
        // this method is called when the users device is not having any app installed for making payment.
        Toast.makeText(this, "No app found for making transaction..", Toast.LENGTH_SHORT).show();
        onBackPressed();
    }


    private void startPollingForTransStatus(){

        if(countDownTimer!=null){
            //avoid multiple invokes
            return;
        }

        countDownTimer = new CountDownTimer(180000, 1000) {

            public void onTick(long millisUntilFinished) {


                long timeSecs = millisUntilFinished / 1000;

                if (timeSecs % 5 == 0) {
                    sendResultToServerForValidation();
                }

                //tvMessage.setText("We are verifying your transaction. Kindly wait for " + timeSecs + " seconds.");
            }

            public void onFinish() {
                //Couldn't get any txn success even after continuously polling for 120Secs
                //sendResultBack(new UserInfo(), "FAILED");
                UiUtils.log(TAG,"onFinish");
                sendStatusBack(FAILED);
                try{
                    if(countDownTimer!=null){
                        countDownTimer.cancel();
                    }
                }catch (Exception ex){ex.printStackTrace();}
            }
        }.start();
    }

    private void sendResultToServerForValidation() {
        sendStatusBack(SUCCESS);
        try{
            if(countDownTimer!=null){
                countDownTimer.cancel();
            }
        }catch (Exception ex){ex.printStackTrace();}
    }
    /*private void sendResultToServerForValidation() {
        HashMap<String,String> deviceDetails = Utils.getDeviceDetails(this);
        //Toast.makeText(getApplicationContext(),"OrderId:" + orderId,Toast.LENGTH_LONG).show();

        UiUtils.showLoadingDialog(this);
        Map<String, Object> params = new HashMap<>();
        params.put(APIConstants.Params.ID, prefUtils.getIntValue(PrefKeys.USER_ID, -1));
        params.put(APIConstants.Params.TOKEN, prefUtils.getStringValue(PrefKeys.SESSION_TOKEN, ""));
        params.put(APIConstants.Params.ORDER_ID, orderId);
        params.put(APIConstants.Params.SUBSCRIPTION_ID, plan.getId());
        params.put(APIConstants.Params.COUPON_CODE, "");
        params.put(APIConstants.Params.AGE, "0");
        params.put(APIConstants.Params.LANGUAGE, prefUtils.getStringValue(PrefKeys.APP_LANGUAGE_STRING, ""));
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

        Call<String> call = apiInterface.verifyUpiPaytmPayment(VERIFY_UPI_PAYTM_PAYMENT, params);
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

                        *//*UiUtils.showShortToast(PaytmUPIActivity.this, paymentObject.optString(APIConstants.Params.MESSAGE) +
                                (!invoice.isPayingForPlan() ? "\n" + getString(R.string.play_automatically) : ""));
                        JSONObject payObject = paymentObject.optJSONObject(APIConstants.Params.DATA);*//*

                        UiUtils.log(TAG,"SUCCESS");
                        sendStatusBack(SUCCESS);

                        try{
                            if(countDownTimer!=null){
                                countDownTimer.cancel();
                            }
                        }catch (Exception ex){ex.printStackTrace();}

                    } else {
                        UiUtils.hideLoadingDialog();
                        UiUtils.log(TAG,"API FAIL");
                        *//*UiUtils.showShortToast(PaytmUPIActivity.this, paymentObject.optString(APIConstants.Params.ERROR_MESSAGE));*//*
                        //sendStatusBack("FAILED");
                        *//*switch (paymentObject.optInt(Params.ERROR_CODE)) {
                            case APIConstants.ErrorCodes.NEED_TO_SUBSCRIBE_TO_MAKE_PAYMENT:
                                Intent i = new Intent(PlansActivity.this, PaymentsActivity.class);
                                startActivity(i);
                                break;
                            case APIConstants.ErrorCodes.NO_DEFAULT_CARD_FOUND:
                            default:
                                Intent toAddCard = new Intent(getApplicationContext(), AddCardActivity.class);
                                startActivity(toAddCard);
                                break;
                        }*//*
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                NetworkUtils.onApiError(PaytmUPIActivity.this);
            }
        });
    }*/


    private void sendStatusBack(String status) {
        Intent returnIntent = new Intent();
        returnIntent.putExtra(PAYMENT_RESP,status);
        setResult(Activity.RESULT_OK,returnIntent);
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        try{
            if(countDownTimer!=null){
                countDownTimer.cancel();
            }
        }catch (Exception ex){ex.printStackTrace();}
    }
}
