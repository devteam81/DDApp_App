package com.dd.app.ui.activity;

import static com.dd.app.network.APIConstants.URLs.SECRET_KEY;
import static com.dd.app.util.Crypt.encodeKey;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import androidx.annotation.RequiresApi;

import com.dd.app.R;
import com.dd.app.util.AppUtils;
import com.dd.app.util.Crypt;
import com.dd.app.util.UiUtils;
import com.dd.app.util.sharedpref.PrefKeys;
import com.dd.app.util.sharedpref.PrefUtils;
import com.dd.app.util.sharedpref.Utils;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.DigestException;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.spec.KeySpec;
import java.util.Arrays;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;


public class SplashActivity extends BaseActivity {

    private final String TAG = SplashActivity.class.getSimpleName();
    private static final long SPLASH_TIME_MILLIS = 3000;
    AnimationDrawable logoAnimation;
    ImageView splash_logo;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);
        Utils.getPublicIpAddress(this, true);
        Utils.sendFCMTokenToServerForRef(this);
       // throw new RuntimeException("Test Crash"); // Force a crash

        splash_logo = findViewById(R.id.splash_logo);
        //splash_logo.setBackgroundResource(R.drawable.bebu_splash_loader);
        checkWidth();

        /*logoAnimation = (AnimationDrawable) splash_logo.getBackground();

        Handler mAnimationHandler = new Handler();
        mAnimationHandler.post(new Runnable() {
            @Override
            public void run() {
                logoAnimation.start();
            }
        });*/

        /*mAnimationHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent splashIntent = new Intent(SplashActivity.this, MainActivity.class);
                if (PrefUtils.getInstance(SplashActivity.this).getBoolanValue(PrefKeys.IS_PASS, false) &&
                        !PrefUtils.getInstance(SplashActivity.this).getStringValue(PrefKeys.PASS_CODE, "").equalsIgnoreCase("")) {
                    splashIntent = new Intent(SplashActivity.this, PassCodeScreenActivity.class);
                    splashIntent.putExtra("screen", false);
                }
                startActivity(splashIntent);
                finish();
            }
        }, getTotalDuration());*/


        /*
         * Showing splash screen with a timer. This will be useful when you
         * want to show case your app logo / company
         */
        new Handler().postDelayed(() -> {
            // This method will be executed once the timer is over
            // Start your app main activity
            Intent splashIntent = new Intent(SplashActivity.this, MainActivity.class);
            if (PrefUtils.getInstance(SplashActivity.this).getBoolanValue(PrefKeys.IS_PASS, false) &&
                    !PrefUtils.getInstance(SplashActivity.this).getStringValue(PrefKeys.PASS_CODE, "").equalsIgnoreCase("")) {
                splashIntent = new Intent(SplashActivity.this, PassCodeScreenActivity.class);
                splashIntent.putExtra("screen", false);
            }
            startActivity(splashIntent);
            finish();
        }, SPLASH_TIME_MILLIS);
    }

    public int getTotalDuration() {

        int iDuration = 0;

        for (int i = 0; i < logoAnimation.getNumberOfFrames(); i++) {
            iDuration += logoAnimation.getDuration(i);
        }

        return iDuration;
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    private void checkWidth()
    {
        int[] dimension = Utils.getDimensions(this);

        //FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) splash_logo.getLayoutParams();

        if(dimension[3]<=400)
        {
            //params.width = Utils.dpToPx(350);
            splash_logo.getLayoutParams().height = Utils.dpToPx(350);
            //splash_logo.setScaleType(ImageView.ScaleType.FIT_CENTER);

        }else if(dimension[3]<=500)
        {
            //1148
            splash_logo.getLayoutParams().height = Utils.dpToPx(450);
        }
        /*if(dimension[3]>500 && dimension[3]<600)
        {
            UiUtils.log(TAG, "margin(500-600)");
            title.setTextSize(14);
        }else if(dp>600 && dp<700)
        {
            UiUtils.log(TAG, "margin(600-700)");
            title.setTextSize(14);
        }else if(dp>700 && dp<800)
        {
            UiUtils.log(TAG, "margin(700-800)");
            title.setTextSize(14);
        }else if(dp>800)
        {
            UiUtils.log(TAG, "margin(800)");
            title.setTextSize(16);
            FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) imgRating.getLayoutParams();
            params.width = 60;
            params.height = 60;
            params.setMargins(0, 30, 40, 0);
            imgRating.setLayoutParams(params);
        }*/
    }

}
