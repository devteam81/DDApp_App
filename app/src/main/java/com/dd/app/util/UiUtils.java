package com.dd.app.util;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.transition.ViewPropertyTransition;
import com.bumptech.glide.signature.ObjectKey;
import com.google.android.material.snackbar.Snackbar;
import com.dd.app.R;


public class UiUtils {

    private static final String TAG = UiUtils.class.getSimpleName();

    private static Dialog loadingDialog;
    private static Dialog internetDialog;
    private static Context mContext;

    public static void showShortToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public static void showLongToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

    public static void showCustomToast(Context context, String message)
    {

        Activity activity = (Activity) context;
        Snackbar mySnackbar = Snackbar.make(activity.findViewById(R.id.main_layout),
                message, Snackbar.LENGTH_LONG);
        mySnackbar.setAction("Show Download", view -> Toast.makeText(context,"hello",Toast.LENGTH_LONG).show());
        mySnackbar.setActionTextColor(Color.RED);
        mySnackbar.show();

    }

    public static void showNoInternetConnection(Context context)
    {
        if(internetDialog== null ) {
            internetDialog = new Dialog(context);
            mContext = context;
            log(TAG,"Dialog-> NULL");
        }
        else if(mContext != context) {
            log(TAG,"Dialog-> "+mContext);
            log(TAG,"Dialog-> "+context);
            internetDialog = new Dialog(context);
            mContext = context;
        }
        //internetDialog = new Dialog(context);

        internetDialog.setContentView(R.layout.dialog_no_internet);
        WindowManager.LayoutParams params = internetDialog.getWindow().getAttributes(); // change this to your dialog.

        params.y = 10; // Here is the param to set your dialog position. Same with params.x
        internetDialog.getWindow().setAttributes(params);
        internetDialog.getWindow().setGravity(Gravity.BOTTOM);
        internetDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        internetDialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        internetDialog.setCancelable(false);

        TextView btnOk = (TextView) internetDialog.findViewById(R.id.btnOk);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { hideNoInternetConnection(); }
        });

        if (!internetDialog.isShowing())
            internetDialog.show();
    }

    public static void hideNoInternetConnection() {
        log(TAG,"Dialog-> "+ internetDialog);
        if (internetDialog != null && internetDialog.isShowing())
            try {
                log(TAG,"Dialog-> "+ internetDialog.isShowing());
                internetDialog.dismiss();
            } catch (Exception e) {
                UiUtils.log(TAG, Log.getStackTraceString(e));
            }
    }

    public static void showLoadingDialog(Context context) {
        hideLoadingDialog();
        loadingDialog = new Dialog(context);
        loadingDialog.setCancelable(false);
        loadingDialog.setContentView(R.layout.api_loading_lottie);
        loadingDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        //ImageView img = loadingDialog.findViewById(R.id.img);
        //img.setBackgroundResource(R.drawable.anim_loader);
        if (!loadingDialog.isShowing()) {
            loadingDialog.show();
            /*AnimationDrawable rocketAnimation = (AnimationDrawable) img.getBackground();

            new Thread(new Runnable() {
                @Override
                public void run() {
                    rocketAnimation.start();
                }
            }).start();*/
            /*Ion.with(imgView)
                    .error(R.drawable.default_image)
                    .animateGif(AnimateGifMode.ANIMATE)
                    .load("file:///android_asset/animated.gif");*/
            //Glide.with(context).asGif().load("file:///android_asset/bebu_icon_gif.gif").into(img);
        }
    }

    public static void hideLoadingDialog() {
        if (loadingDialog != null && loadingDialog.isShowing())
            try {
                loadingDialog.dismiss();
            } catch (Exception e) {
                UiUtils.log(TAG, Log.getStackTraceString(e));
            }
    }

    public static RequestOptions getRequestOptionHorizontal() {
        return new RequestOptions()
                .placeholder(R.drawable.bebu_placeholder_horizontal)
                .error(R.drawable.bebu_placeholder_horizontal)
                .fallback(R.drawable.bebu_placeholder_horizontal)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .signature(new ObjectKey(0));
    }

    public static RequestOptions getRequestOptionVertical() {
        return new RequestOptions()
                .placeholder(R.drawable.bebu_placeholder_vertical)
                .error(R.drawable.bebu_placeholder_vertical)
                .fallback(R.drawable.bebu_placeholder_vertical)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .signature(new ObjectKey(0));
    }

    public static ViewPropertyTransition.Animator animationObject = view -> {
//        view.setAlpha(0f);
        ObjectAnimator fadeAnim = ObjectAnimator.ofFloat(view, "alpha", 0f, 1f);
        fadeAnim.setDuration(1000);
        fadeAnim.start();
    };

    public static boolean checkString(String value)
    {
        return value == null || value.equalsIgnoreCase("null") || value.equalsIgnoreCase("");
    }

    public static void log( String tag, String message)
    {
        Log.e(tag,message);
    }

}
