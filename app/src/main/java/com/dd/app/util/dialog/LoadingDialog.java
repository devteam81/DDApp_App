package com.dd.app.util.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.widget.ImageView;

import com.dd.app.R;
import com.dd.app.util.UiUtils;

public class LoadingDialog {

    private final String TAG = LoadingDialog.class.getSimpleName();

    private Dialog dialog;
    private static final LoadingDialog ourInstance = new LoadingDialog();
    public static LoadingDialog getInstance() {
        return ourInstance;
    }
    private LoadingDialog() { }
    public void show(Context context) {
        if (dialog != null && dialog.isShowing()) {
            return;
        }
        dialog = new Dialog(context);
        dialog.setContentView(R.layout.api_loading_lottie);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(false);
        //ImageView img = dialog.findViewById(R.id.img);
        //img.setBackgroundResource(R.drawable.anim_loader);

        if (!dialog.isShowing()) {
            dialog.show();
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

    public void dismiss() {

        try {
            if (dialog != null && dialog.isShowing()) {
                dialog.dismiss();
            }
        }catch (Exception e)
        {
            UiUtils.log(TAG, Log.getStackTraceString(e));
        }
    }
}
