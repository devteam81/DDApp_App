package com.dd.app.util.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dd.app.R;
import com.dd.app.util.UiUtils;

public class NoInternetDialog {

    private final String TAG = NoInternetDialog.class.getSimpleName();

    private Dialog dialog;
    private static final NoInternetDialog ourInstance = new NoInternetDialog();
    public static NoInternetDialog getInstance() {
        return ourInstance;
    }
    private NoInternetDialog() { }
    public void show(Context context) {
        if (dialog != null && dialog.isShowing()) {
            return;
        }
        dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_no_internet);
        WindowManager.LayoutParams params = dialog.getWindow().getAttributes(); // change this to your dialog.

        params.y = 10; // Here is the param to set your dialog position. Same with params.x
        dialog.getWindow().setAttributes(params);
        dialog.getWindow().setGravity(Gravity.BOTTOM);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false);

        TextView btnOk = (TextView) dialog.findViewById(R.id.btnOk);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { dismiss(); }
        });

        dialog.show();

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
