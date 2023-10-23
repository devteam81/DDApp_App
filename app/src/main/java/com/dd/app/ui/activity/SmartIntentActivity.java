package com.dd.app.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.dd.app.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SmartIntentActivity extends AppCompatActivity {

    private final int REQUEST_CODE = 123;

    @BindView(R.id.paytm)
    Button paytm;
    @BindView(R.id.gpay)
    Button gpay;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_smart_intent);
        ButterKnife.bind(this);

        /*1.1 Defining the variables with sample UPI Apps package names. The merchant can add more UPI apps with their package names according to their requirement *//*
        String BHIM_UPI = "in.org.npci.upiapp";
        String GOOGLE_PAY = "com.google.android.apps.nbu.paisa.user";
        String PHONE_PE = "com.phonepe.app";
        String PAYTM = "net.one97.paytm";

        *//*1.2 Combining the UPI app package name variables in a list *//*
        ArrayList<String> upiApps = new ArrayList<String>();
        upiApps.add(PAYTM);
        upiApps.add(GOOGLE_PAY);
        upiApps.add(PHONE_PE);
        upiApps.add(BHIM_UPI);

        *//*2.1 Defining button elements for generic UPI OS intent and specific UPI Apps *//*
        //var upiButton = findViewById(R.id.upi) as Button
        //var paytmButton = findViewById(R.id.paytm) as Button
        //var gpayButton = findViewById(R.id.gpay) as Button
        //var phonepeButton = findViewById(R.id.phonepe) as Button
        //var bhimButton = findViewById(R.id.bhim) as Button

        *//*2.2 Combining button elements of specific UPI Apps in a list in the same order as the above upiApps list of UPI app package names *//*
        //val upiAppButtons = listOf<Button>(paytmButton, gpayButton, phonepeButton, bhimButton)
        ArrayList<Button> upiAppButtons = new ArrayList<Button>();
        upiAppButtons.add(paytm);
        upiAppButtons.add(gpay);
        //upiApps.add(PHONE_PE);
        //upiApps.add(BHIM_UPI);

        *//*3. Defining a UPI intent with a Paytm merchant UPI spec deeplink *//*
        //"upi://pay?pa=hbheda1990-1@okicici&pn=Paytm%20Merchant&mc=5499&mode=02&orgid=000000&paytmqr=2810050501011OOQGGB29A01&am=11&sign=MEYCIQDq96qhUnqvyLsdgxtfdZ11SQP//6F7f7VGJ0qr//lF/gIhAPgTMsopbn4Y9DiE7AwkQEPPnb2Obx5Fcr0HJghd4gzo"
        String uri =
                "upi://pay?pa=hbheda1990-1@okicici&pn=Harshit%20Bheda&mc=5499&mode=02&orgid=000000&paytmqr=2810050501011OOQGGB29A01&am=11&sign=MEYCIQDq96qhUnqvyLsdgxtfdZ11SQP//6F7f7VGJ0qr//lF/gIhAPgTMsopbn4Y9DiE7AwkQEPPnb2Obx5Fcr0HJghd4gzo";
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        intent.data = Uri.parse(uri);

                *//*4.1 Defining an on click action for the UPI generic OS intent chooser. This is just for reference, not needed in case of UPI Smart Intent.
            - This will display a list of all apps available to respond to the UPI intent
            in a chooser tray by the Android OS *//*
        paytm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent chooser = new Intent.createChooser(intent, "Pay with...");
                startActivityForResult(chooser, REQUEST_CODE);
            }
        });*/
    }
}
