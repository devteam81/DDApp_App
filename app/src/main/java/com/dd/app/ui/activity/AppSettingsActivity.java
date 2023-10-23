package com.dd.app.ui.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.res.ResourcesCompat;

import android.util.Log;
import android.view.View;
import android.view.Gravity;
import android.view.Window;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dd.app.network.APIConstants;
import com.dd.app.util.sharedpref.Utils;
import com.github.angads25.toggle.model.ToggleableView;
import com.github.angads25.toggle.widget.LabeledSwitch;
import com.dd.app.BuildConfig;
import com.dd.app.R;
import com.dd.app.network.APIClient;
import com.dd.app.network.APIInterface;
import com.dd.app.util.NetworkUtils;
import com.dd.app.util.UiUtils;
import com.dd.app.util.sharedpref.PrefKeys;
import com.dd.app.util.sharedpref.PrefUtils;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.dd.app.R.*;
import static com.dd.app.network.APIConstants.APIs.UPDATE_DOWNLOAD_EXPIRY;
import static com.dd.app.network.APIConstants.Constants;
import static com.dd.app.network.APIConstants.Constants.MANUAL_LOGIN;
import static com.dd.app.network.APIConstants.Params;
import static com.dd.app.network.APIConstants.Params.APPVERSION;
import static com.dd.app.network.APIConstants.Params.VERSION_CODE;
import static com.dd.app.ui.activity.MainActivity.SERVER_VERSION_CODE;
import static com.dd.app.ui.activity.MainActivity.SERVER_VERSION_NAME;
import static com.dd.app.ui.activity.MainActivity.SHOW_UPDATE_BUTTON;
import static com.dd.app.util.sharedpref.Utils.getUserLoginStatus;

public class AppSettingsActivity extends BaseActivity {

    private final String TAG = AppSettingsActivity.class.getSimpleName();
    public static final String PUSH_NOTIFICATIONS = "push";
    public static final String EMAIL_NOTIFICATIONS = "email";

    @BindView(R.id.aboutDevice)
    TextView aboutDevice;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.ll_push_notifications)
    LinearLayout ll_push_notifications;
    @BindView(R.id.pushNotificationToggle)
    LabeledSwitch pushNotificationToggle;
    @BindView(R.id.pushNotificationToggleOld)
    SwitchCompat pushNotificationToggleOld;
    @BindView(R.id.emailNotificationToggle)
    SwitchCompat emailNotificationToggle;
    @BindView(R.id.pipModeToggle)
    SwitchCompat pipModeToggle;
    @BindView(R.id.pipSettingsView)
    View pipSetting;

    @BindView(R.id.rl_download_validity)
    RelativeLayout rl_download_validity;

    @BindView(R.id.rl_social_login)
    RelativeLayout rl_social_login;
    @BindView(R.id.txt_social_login)
    TextView txt_social_login;

    @BindView(R.id.btnUpdate)
    Button btnUpdate;
    @BindView(R.id.app_version)
    TextView app_version;

    @BindView(R.id.rl_language)
    RelativeLayout rl_language;

    APIInterface apiInterface;

    private final String[] availableAppLanguages = new String[]{"English", "Spanish", "Hindi"};
    private final String[] availableAppLanguageLocalStr = new String[]{"en", "es", "hi"};
    private int currentLanguageIndex, selectedLanguageIndex;
    private String currentLanguage;

    boolean isPushOn = false;
    boolean isEmailOn = false;
    private final CompoundButton.OnCheckedChangeListener pushListener
            = (buttonView, isChecked) -> updateNotificationSettingOld(PUSH_NOTIFICATIONS, buttonView, isPushOn);
    private final CompoundButton.OnCheckedChangeListener emailListener
            = (buttonView, isChecked) -> updateNotificationSettingOld(EMAIL_NOTIFICATIONS, buttonView, isEmailOn);
    private final CompoundButton.OnCheckedChangeListener pipListener
            = (buttonView, isChecked)
            -> PrefUtils.getInstance(AppSettingsActivity.this).setValue(PrefKeys.PIP_ENTER_WHEN_MINIMIZED, isChecked);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(layout.activity_app_settings);
        ButterKnife.bind(this);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
        apiInterface = APIClient.getClient().create(APIInterface.class);

        String loginType = prefUtils.getStringValue(PrefKeys.LOGIN_TYPE, "");
        app_version.setText(BuildConfig.VERSION_NAME);

        if(PrefUtils.getInstance(this).getBoolanValue(PrefKeys.IS_LOGGED_IN, false)) {
            ll_push_notifications.setVisibility(View.VISIBLE);
            rl_download_validity.setVisibility(View.VISIBLE);
            if(!loginType.equalsIgnoreCase(MANUAL_LOGIN)) {
                rl_social_login.setVisibility(View.VISIBLE);
                txt_social_login.setText(String.format("You have registered using your %s Social Account, you can't set or change password for social accounts.", loginType.toUpperCase()));
            }
        }
        else {
            ll_push_notifications.setVisibility(View.GONE);
            rl_download_validity.setVisibility(View.GONE);
        }

        if(PrefUtils.getInstance(this).getIntValue(PrefKeys.USER_ID, -1) > 1)
            getUserLoginStatus(this);

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            pipSetting.setVisibility(View.GONE);
        } else {
            pipModeToggle.setChecked(PrefUtils.getInstance(this).getBoolanValue(PrefKeys.PIP_ENTER_WHEN_MINIMIZED, true));
        }

        aboutDevice.setText(getAboutDeviceText());

        getPushNotificationStatus();
        getEmailNotificationStatus();
        pipModeToggle.setOnCheckedChangeListener(pipListener);

        if(SERVER_VERSION_CODE > BuildConfig.VERSION_CODE && SHOW_UPDATE_BUTTON) {
            btnUpdate.setVisibility(View.VISIBLE);
            btnUpdate.setOnClickListener(v -> Utils.openPlayStorePage(AppSettingsActivity.this));
            /*btnUpdate.setOnClickListener(v -> startActivity(new Intent(this, UpdateAppActivity.class)
                    .putExtra(VERSION_CODE, SERVER_VERSION_CODE)
                    .putExtra(APPVERSION, SERVER_VERSION_NAME)));*/
        }

        currentLanguage = prefUtils.getStringValue(PrefKeys.APP_LANGUAGE_STRING, "");
        for (int i = 0; i < availableAppLanguageLocalStr.length; i++) {
            if (currentLanguage.equals(availableAppLanguageLocalStr[i])) {
                currentLanguageIndex = selectedLanguageIndex = i;
                break;
            }
        }

        //rl_language.setOnClickListener(v-> startActivity(new Intent(this, ProfileViewActivity.class)));
        rl_language.setOnClickListener(v-> {
            showLanguageDialogChooser(availableAppLanguages);
            //changeLanguage();
        });


    }

    private void showLanguageDialogChooser(String[] availableAppLanguages) {

        Dialog dialog = new Dialog(AppSettingsActivity.this);
        dialog.setContentView(R.layout.dialog_download);

        TextView txtHeader = (TextView) dialog.findViewById(R.id.txt_heading);
        txtHeader.setText(getString(R.string.language_select));

        RadioGroup rg_resolutions = (RadioGroup) dialog.findViewById(R.id.rg_resolutions);
        rg_resolutions.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
        rg_resolutions.setDividerDrawable(ResourcesCompat.getDrawable(getResources(),R.drawable.divider_drawable,null));

            for (int i = 0; i < availableAppLanguages.length; i++) {
                RadioButton rdbtn = new RadioButton(this);
                rdbtn.setId(i);
                rdbtn.setPadding(20,30,20,30);
                rdbtn.setGravity(Gravity.END);
                rdbtn.setText(availableAppLanguages[i]);
                rdbtn.setTextColor(getResources().getColor(R.color.white));
                rdbtn.setTextSize(18f);
                rdbtn.setPadding(20,10,0,0);
                if(availableAppLanguages[i].equalsIgnoreCase(prefUtils.getStringValue(PrefKeys.APP_LANGUAGE,"")))
                    rdbtn.setChecked(true);
                /*rdbtn.setOnClickListener(v -> {
                    UiUtils.log(TAG,"Selected Id:"+ rdbtn.getId());
                    UiUtils.log(TAG,"Selected Id:"+ rdbtn.getText());
                    UiUtils.log(TAG,"Selected Id:"+ availableAppLanguageLocalStr[rdbtn.getId()]);
                    *//*selectedLanguageIndex = rdbtn.getId();
                    currentLanguageIndex = selectedLanguageIndex;
                    prefUtils.setValue(PrefKeys.APP_LANGUAGE_STRING, availableAppLanguageLocalStr[currentLanguageIndex]);
                    prefUtils.setValue(PrefKeys.APP_LANGUAGE, availableAppLanguages[currentLanguageIndex]);
                    setLanguage(availableAppLanguageLocalStr[currentLanguageIndex]);*//*
                    Toast.makeText(this, getString(R.string.language_chng), Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                });*/
                ColorStateList colorStateList = new ColorStateList(new int[][]{
                                        new int[]{-android.R.attr.state_enabled}, // Disabled
                                        new int[]{android.R.attr.state_enabled}   // Enabled
                                },
                        new int[]{
                                    getResources().getColor(R.color.colorPrimaryLight), // disabled
                                    getResources().getColor(R.color.element_green)   // enabled
                                }
                );

                rdbtn.setButtonTintList(colorStateList); // set the color tint list
                rg_resolutions.addView(rdbtn);

            }

        TextView btnOk = (TextView) dialog.findViewById(R.id.btnOk);
        btnOk.setVisibility(View.VISIBLE);
            btnOk.setOnClickListener(v -> {
                currentLanguageIndex = rg_resolutions.getCheckedRadioButtonId();
                //currentLanguageIndex = se
                //selectedLanguageIndex;
                UiUtils.log(TAG,"Selected Id:"+ currentLanguageIndex);
                UiUtils.log(TAG,"Selected Id:"+ availableAppLanguages[currentLanguageIndex]);
                UiUtils.log(TAG,"Selected Id:"+ availableAppLanguageLocalStr[currentLanguageIndex]);
                /*prefUtils.setValue(PrefKeys.APP_LANGUAGE_STRING, availableAppLanguageLocalStr[currentLanguageIndex]);
                prefUtils.setValue(PrefKeys.APP_LANGUAGE, availableAppLanguages[currentLanguageIndex]);
                setLanguage(availableAppLanguageLocalStr[currentLanguageIndex]);*/
                Toast.makeText(this, getString(R.string.language_chng), Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            });

            dialog.show();
    }

    /*private void changeLanguage() {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(AppSettingsActivity.this, R.style.MyDialogAlertTheme);
        builder.setTitle(getString(R.string.update_language));
        builder.setSingleChoiceItems(availableAppLanguages, currentLanguageIndex, (dialog, which) -> selectedLanguageIndex = which);
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setPositiveButton(getString(R.string.ok), (dialog, which) -> {
            currentLanguageIndex = selectedLanguageIndex;
            prefUtils.setValue(PrefKeys.APP_LANGUAGE_STRING, availableAppLanguageLocalStr[currentLanguageIndex]);
            prefUtils.setValue(PrefKeys.APP_LANGUAGE, availableAppLanguages[currentLanguageIndex]);
            setLanguage(availableAppLanguageLocalStr[currentLanguageIndex]);
            Toast.makeText(this, getString(R.string.language_chng), Toast.LENGTH_SHORT).show();
        });
        builder.create().show();
    }*/

    private String getAboutDeviceText() {
        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            return "Model: " + Build.MODEL + "\n" + "Name: " +
                    Build.DEVICE + "\n" + "Serial: " + Build.getSerial() + "\n" +
                    "Fingerprint: " + Build.FINGERPRINT + "\n" +
                    "Board: " + Build.BOARD + "\n";
        } else {*/
        return "Model: " + Build.MODEL + "\n" + "Name: " +
                Build.DEVICE + "\n" + "Serial: " + Build.SERIAL + "\n" +
                "Fingerprint: " + Build.FINGERPRINT + "\n" +
                "Board: " + Build.BOARD + "\n";
        //}
    }

    @OnClick({R.id.checkNetworkTile, R.id.speedTestTile, R.id.privacyTile, R.id.termsOfUseTile, R.id.rl_download_validity})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.checkNetworkTile:
                startActivity(new Intent(this, TestNetworkActivity.class));
                break;
            case R.id.speedTestTile:
                startActivity(new Intent(this, WebViewActivity.class)
                        .putExtra(WebViewActivity.PAGE_TYPE, WebViewActivity.PageTypes.SPEEDTEST));
                break;
            case R.id.privacyTile:
                startActivity(new Intent(this, WebViewActivity.class)
                        .putExtra(WebViewActivity.PAGE_TYPE, WebViewActivity.PageTypes.PRIVACY));
                break;
            case R.id.termsOfUseTile:
                startActivity(new Intent(this, WebViewActivity.class)
                        .putExtra(WebViewActivity.PAGE_TYPE, WebViewActivity.PageTypes.TERMS));
                break;
            case R.id.rl_download_validity:
                showDownloadValidityPopup();
                break;
        }
    }

    private void getPushNotificationStatus() {
        isPushOn = PrefUtils.getInstance(this).getBoolanValue(PrefKeys.PUSH_NOTIFICATIONS, true);
        pushNotificationToggleOld.setOnCheckedChangeListener(null);
        pushNotificationToggleOld.setChecked(isPushOn);
        pushNotificationToggleOld.setOnCheckedChangeListener(pushListener);

        pushNotificationToggle.setOn(isPushOn);
        pushNotificationToggle.setOnToggledListener((toggleableView, isOn) -> updateNotificationSetting(PUSH_NOTIFICATIONS, toggleableView, isPushOn));
    }

    private void getEmailNotificationStatus() {
        isEmailOn = PrefUtils.getInstance(this).getBoolanValue(PrefKeys.EMAIL_NOTIFICATIONS, true);
        emailNotificationToggle.setOnCheckedChangeListener(null);
        emailNotificationToggle.setChecked(isEmailOn);
        emailNotificationToggle.setOnCheckedChangeListener(emailListener);
    }

    protected void updateNotificationSettingOld(String pushSettingItem, CompoundButton button, boolean isOn) {
        UiUtils.showLoadingDialog(this);
        PrefUtils prefUtils = PrefUtils.getInstance(this);
        Map<String, Object> params = new HashMap<>();
        params.put(Params.ID, id);
        params.put(Params.TOKEN, token);
        params.put(Params.SUB_PROFILE_ID, subProfileId);
        params.put(Params.NOTIFICATION_TYPE, pushSettingItem);
        params.put(Params.STATUS, isOn ? 0 : 1);
        params.put(Params.LANGUAGE, prefUtils.getStringValue(PrefKeys.APP_LANGUAGE_STRING, ""));

        Call<String> call = apiInterface.updateNotificationSetting(APIConstants.APIs.NOTIFICATION_SETTING_UPDATE, params);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                UiUtils.hideLoadingDialog();
                JSONObject notifSettingUpdateResponse = null;
                try {
                    notifSettingUpdateResponse = new JSONObject(response.body());
                } catch (Exception e) {
                    UiUtils.log(TAG, Log.getStackTraceString(e));
                }
                if (notifSettingUpdateResponse != null) {
                    if (notifSettingUpdateResponse.optString(Params.SUCCESS).equals(Constants.TRUE)) {
                        UiUtils.showShortToast(AppSettingsActivity.this, notifSettingUpdateResponse.optString(Params.MESSAGE));
                        switch (pushSettingItem) {
                            case PUSH_NOTIFICATIONS:
                                isPushOn = !isOn;
                                PrefUtils.getInstance(AppSettingsActivity.this).setValue(PrefKeys.PUSH_NOTIFICATIONS, isPushOn);
                                break;
                            case EMAIL_NOTIFICATIONS:
                                isEmailOn = !isOn;
                                PrefUtils.getInstance(AppSettingsActivity.this).setValue(PrefKeys.EMAIL_NOTIFICATIONS, isEmailOn);
                                break;
                        }
                    } else {
                        UiUtils.showShortToast(AppSettingsActivity.this, notifSettingUpdateResponse.optString(Params.ERROR_MESSAGE));
                        rollBackPushToggledOld(button, isOn);
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                UiUtils.hideLoadingDialog();
                rollBackPushToggledOld(button, isOn);
                NetworkUtils.onApiError(AppSettingsActivity.this);
            }
        });
    }

    private void rollBackPushToggledOld(CompoundButton button, boolean isOn) {
        button.setChecked(isOn);
    }

    protected void updateNotificationSetting(String pushSettingItem, ToggleableView button, boolean isOn) {
        UiUtils.showLoadingDialog(this);
        PrefUtils prefUtils = PrefUtils.getInstance(this);
        Map<String, Object> params = new HashMap<>();
        params.put(Params.ID, id);
        params.put(Params.TOKEN, token);
        params.put(Params.SUB_PROFILE_ID, subProfileId);
        params.put(Params.NOTIFICATION_TYPE, pushSettingItem);
        params.put(Params.STATUS, isOn ? 0 : 1);
        params.put(Params.LANGUAGE, prefUtils.getStringValue(PrefKeys.APP_LANGUAGE_STRING, ""));

        Call<String> call = apiInterface.updateNotificationSetting(APIConstants.APIs.NOTIFICATION_SETTING_UPDATE, params);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                UiUtils.hideLoadingDialog();
                JSONObject notifSettingUpdateResponse = null;
                try {
                    notifSettingUpdateResponse = new JSONObject(response.body());
                } catch (Exception e) {
                    UiUtils.log(TAG, Log.getStackTraceString(e));
                }
                if (notifSettingUpdateResponse != null) {
                    if (notifSettingUpdateResponse.optString(Params.SUCCESS).equals(Constants.TRUE)) {
                        UiUtils.showShortToast(AppSettingsActivity.this, notifSettingUpdateResponse.optString(Params.MESSAGE));
                        switch (pushSettingItem) {
                            case PUSH_NOTIFICATIONS:
                                isPushOn = !isOn;
                                PrefUtils.getInstance(AppSettingsActivity.this).setValue(PrefKeys.PUSH_NOTIFICATIONS, isPushOn);
                                break;
                            case EMAIL_NOTIFICATIONS:
                                isEmailOn = !isOn;
                                PrefUtils.getInstance(AppSettingsActivity.this).setValue(PrefKeys.EMAIL_NOTIFICATIONS, isEmailOn);
                                break;
                        }
                    } else {
                        UiUtils.showShortToast(AppSettingsActivity.this, notifSettingUpdateResponse.optString(Params.ERROR_MESSAGE));
                        rollBackPushToggled(button, isOn);
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                UiUtils.hideLoadingDialog();
                rollBackPushToggled(button, isOn);
                NetworkUtils.onApiError(AppSettingsActivity.this);
            }
        });
    }

    private void rollBackPushToggled(ToggleableView button, boolean isOn) {
        button.setOn(isOn);
    }

    public void showDownloadValidityPopup()
    {

        Dialog dialog = new Dialog(AppSettingsActivity.this);

        dialog.setContentView(layout.dialog_download_validity_popup);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        EditText ed_validity_days = dialog.findViewById(R.id.ed_validity_days);
        TextView txtSave = dialog.findViewById(R.id.txtSave);
        txtSave.setOnClickListener((View v) -> {

            if(ed_validity_days.getText().toString().trim().length()>0 && !ed_validity_days.getText().toString().equals("0")) {
                dialog.dismiss();
                updateDownloadExpiry(ed_validity_days.getText().toString());
            }
            else
                Toast.makeText(AppSettingsActivity.this,getResources().getString(string.enter_valid_days),Toast.LENGTH_SHORT).show();
        });

        dialog.show();
        Window window = dialog.getWindow();
        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        //window.setGravity(Gravity.CENTER);
    }

    protected void updateDownloadExpiry(String days) {
        UiUtils.showLoadingDialog(this);
        Map<String, Object> params = new HashMap<>();
        params.put(Params.ID, id);
        params.put(Params.TOKEN, token);
        params.put(Params.DOWNLOAD_EXPIRY, days);

        Call<String> call = apiInterface.updateDownloadExpiry(UPDATE_DOWNLOAD_EXPIRY, params);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                UiUtils.hideLoadingDialog();
                JSONObject downloadExpiryUpdateResponse = null;
                try {
                    downloadExpiryUpdateResponse = new JSONObject(response.body());
                } catch (Exception e) {
                    UiUtils.log(TAG, Log.getStackTraceString(e));
                }
                if (downloadExpiryUpdateResponse != null) {
                    if (downloadExpiryUpdateResponse.optString(Params.SUCCESS).equals(Constants.TRUE)) {
                        UiUtils.showShortToast(AppSettingsActivity.this, downloadExpiryUpdateResponse.optString(Params.MESSAGE));
                    } else {
                        UiUtils.showShortToast(AppSettingsActivity.this, downloadExpiryUpdateResponse.optString(Params.ERROR_MESSAGE));
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                UiUtils.hideLoadingDialog();
                NetworkUtils.onApiError(AppSettingsActivity.this);
            }
        });
    }

}

