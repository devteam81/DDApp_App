package com.dd.app.ui.activity;

import android.Manifest;
import android.app.Dialog;
import android.app.DownloadManager;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;

import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.installreferrer.api.InstallReferrerClient;
import com.android.installreferrer.api.InstallReferrerStateListener;
import com.android.installreferrer.api.ReferrerDetails;
import com.dd.app.BuildConfig;
import com.dd.app.dd.model.LoginUser;
import com.dd.app.dd.ui.SubscribeDDFragment;
import com.dd.app.dd.ui.login.LoginDDFragment;
import com.dd.app.dd.ui.profile.ProfileFragment;
import com.dd.app.dd.utils.sharedPreference.PrefKeysDD;
import com.dd.app.dd.utils.sharedPreference.SharedPreferenceHelper;
import com.dd.app.gcm.MessagingServices;
import com.dd.app.util.download.NavigateToScreenEvent;
import com.dd.app.util.sharedpref.Utils;
import com.downloader.PRDownloader;
import com.downloader.Status;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.InstallStatus;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.dd.app.R;
import com.dd.app.model.Support;
import com.dd.app.network.APIClient;
import com.dd.app.network.APIConstants;
import com.dd.app.network.APIInterface;
import com.dd.app.network.events.APIEvent;
import com.dd.app.ui.fragment.DownloadsFragment;
import com.dd.app.ui.fragment.SearchFragment;
import com.dd.app.ui.fragment.SettingsFragment;
import com.dd.app.ui.fragment.VideoContentFragment;
import com.dd.app.util.ConfigParser;
import com.dd.app.util.NetworkUtils;
import com.dd.app.util.UiUtils;
import com.dd.app.util.download.DownloadCompleteListener;
import com.dd.app.util.download.Downloader;
import com.dd.app.util.sharedpref.PrefHelper;
import com.dd.app.util.sharedpref.PrefKeys;
import com.dd.app.util.sharedpref.PrefUtils;


import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import pub.devrel.easypermissions.EasyPermissions;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.dd.app.network.APIConstants.APIs.GETHELPQUERIES;
import static com.dd.app.network.APIConstants.APIs.GET_SERVER_APP_VERSION;
import static com.dd.app.network.APIConstants.APIs.USER_SUBSCRIPTION;
import static com.dd.app.network.APIConstants.Constants.DOWNLOAD_STATUS;
import static com.dd.app.network.APIConstants.Constants.SUCCESS;
import static com.dd.app.network.APIConstants.Params.ADMIN_UNIQUE_ID;
import static com.dd.app.network.APIConstants.Params.ADMIN_VIDEO_ID;
import static com.dd.app.network.APIConstants.Params.APPVERSION;
import static com.dd.app.network.APIConstants.Params.IMAGE;
import static com.dd.app.network.APIConstants.Params.LOGIN;
import static com.dd.app.network.APIConstants.Params.NAME;
import static com.dd.app.network.APIConstants.Params.PARENT_ID;
import static com.dd.app.network.APIConstants.Params.REF_ID;
import static com.dd.app.network.APIConstants.Params.SEASON_ID;
import static com.dd.app.network.APIConstants.Params.SHARE_TYPE_INSTALL;
import static com.dd.app.network.APIConstants.Params.SHARE_TYPE_LOGIN;
import static com.dd.app.network.APIConstants.Params.TITLE;
import static com.dd.app.network.APIConstants.Params.TYPE;
import static com.dd.app.network.APIConstants.Params.VERSION_CODE;
import static com.dd.app.ui.activity.VideoPageActivity.ACTION_DOWNLOAD_UPDATE;
import static com.dd.app.util.Fragments.HOME_FRAGMENTS;
import static com.dd.app.util.UiUtils.checkString;
import static com.dd.app.util.sharedpref.PrefKeys.IP_ADDRESS;
import static com.dd.app.util.sharedpref.PrefKeys.SERVER_APP_VERSION;
import static com.dd.app.util.sharedpref.PrefKeys.UPDATE_APP_COMPULSORY;
import static com.dd.app.util.sharedpref.Utils.getIPAddress;

public class MainActivity extends BaseActivity implements View.OnClickListener, DownloadCompleteListener {

    private final String TAG = MainActivity.class.getSimpleName();

    private final int STORAGE_PERMISSION = 124;
    private final int NOTIFICATION_PERMISSION = 126;

    public static final String FIRST_TIME = "firstTime";
    public static String CURRENT_FRAGMENT="";
    public Fragment fragment;

    @BindView(R.id.rlBottomNavigationBarContainer)
    LinearLayout rlBottomNavigationBarContainer;
    @BindView(R.id.networkStatusBar)
    TextView networkStatusBar;
    @BindView(R.id.container)
    FrameLayout container;
    @BindView(R.id.llHome)
    RelativeLayout llHome;
    @BindView(R.id.llSearch)
    RelativeLayout llSearch;
    @BindView(R.id.llYourVideos)
    RelativeLayout llYourVideos;
    @BindView(R.id.llProfile)
    RelativeLayout llProfile;
    @BindView(R.id.llDD)
    RelativeLayout llDD;
    @BindView(R.id.ivHome)
    ImageView ivHome;
    @BindView(R.id.ivSearch)
    ImageView ivSearch;
    @BindView(R.id.ivDownload)
    ImageView ivDownload;
    @BindView(R.id.ivMore)
    ImageView ivMore;
    @BindView(R.id.ivDD)
    ImageView ivDD;
    @BindView(R.id.tvHome)
    TextView tvHome;
    @BindView(R.id.tvSearch)
    TextView tvSearch;
    @BindView(R.id.tvProfile)
    TextView tvProfile;
    @BindView(R.id.tvYourVideos)
    TextView tvYourVideos;
    @BindView(R.id.tvDD)
    TextView tvDD;

    public static boolean skipLock = false;

    public static String COOKIEKEYPAIR = "";
    public static String COOKIEPOLICY = "";
    public static String COOKIESIGNATURE = "";

    public static String CURRENT_IP="103.58.155.174";
    public static String CURRENT_COUNTRY= "";
    public static String CURRENT_REGION = "";
    public static String[] STATES_PAYMENT;// = {"MAHARASHTRA","DELHI","PUNJAB","ABC","XYZ"};

    public static int SERVER_VERSION_CODE = BuildConfig.VERSION_CODE;
    public static String SERVER_VERSION_NAME = BuildConfig.VERSION_NAME;
    public static boolean SHOW_UPDATE_BUTTON = false;

    //Fragments
    APIInterface apiInterface;
    VideoContentFragment contentFragment;
    PrefUtils prefUtils;
    public static BroadcastReceiver connectivityReceiver;
    private boolean doubleTapToExitApp = false;
    private FirebaseAnalytics mFirebaseAnalytics;
    public static ArrayList<Support> supportArrayList  = new ArrayList<>();

    Dialog updateDialog;
    public static boolean CANCEL_UPDATE = false;

    public static int[] dimension;

    InstallReferrerClient referrerClient;

    private AppUpdateManager mAppUpdateManager;
    private static final int RC_APP_UPDATE = 11;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        prefUtils = PrefUtils.getInstance(this);
        apiInterface = APIClient.getClient().create(APIInterface.class);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        mAppUpdateManager = AppUpdateManagerFactory.create(getApplicationContext());
        //checkIfUpdateAvailable();

        //dimensions[0] = height, dimensions[1] = width, dimensions[2] = heightDp, dimensions[3] = widthDp;
        dimension = Utils.getDimensions(this);
        sendUnreportedDownloadStatuses();
        getAllQueryTypes();
        if(!prefUtils.getBoolanValue(PrefKeys.REFERENCE_INSTALL, false))
            getInstallReferrer();
        //sendIpAddress();
        CURRENT_IP = Utils.getStringFromPreferences(IP_ADDRESS, "103.58.155.174", this);
        UiUtils.log(TAG, "IP: " + CURRENT_IP);

        Intent caller = getIntent();
        //getConfigFromBackEnd();
        switch (CURRENT_FRAGMENT) {
            case "search":
                replaceFragmentWithAnimation(new SearchFragment(), HOME_FRAGMENTS[1], false);
                break;
            case "wishlist":
                replaceFragmentWithAnimation(new DownloadsFragment(), HOME_FRAGMENTS[3], false);
                break;
            case "settings":
                replaceFragmentWithAnimation(new SettingsFragment(), HOME_FRAGMENTS[4], false);
                break;
            case "dd":
                replaceFragmentWithAnimation(new ProfileFragment(), HOME_FRAGMENTS[5], false);
                break;
            default:
                contentFragment = VideoContentFragment.getInstance("HOME", 0, 0, 0);
                replaceFragmentWithAnimation(contentFragment, HOME_FRAGMENTS[0], false);
                break;
        }

        prepareGoogleAnalytics();

        llHome.setOnClickListener(this);
        llProfile.setOnClickListener(this);
        llSearch.setOnClickListener(this);
        llYourVideos.setOnClickListener(this);
        llDD.setOnClickListener(this);

        getDataFromShareUrl();
        getPermission();
        //throw new RuntimeException("Test Crash");

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_APP_UPDATE) {
            if (resultCode == RESULT_CANCELED) {
                Toast.makeText(getApplicationContext(), "Update canceled by user! Result Code: " + resultCode, Toast.LENGTH_LONG).show();
            } else if (resultCode == RESULT_OK) {
                Toast.makeText(getApplicationContext(), "Update success! Result Code: " + resultCode, Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getApplicationContext(), "Update Failed! Result Code: " + resultCode, Toast.LENGTH_LONG).show();
                //checkIfUpdateAvailable();
            }
            /*if (resultCode != RESULT_OK) {
                UiUtils.log(TAG, "Update flow failed! Result code: " + resultCode);
                // If the update is cancelled or fails,
                // you can request to start the update again.
            }*/
        }
    }

    @Override
    public void onResume() {
        super.onResume();



        //api call
        UiUtils.log(TAG,"CANCEL_UPDATE: " + CANCEL_UPDATE);
        //if(!CANCEL_UPDATE)
        getAppVersionFromServer();
        if(prefUtils.getBoolanValue(PrefKeys.IS_LOGGED_IN, false) && prefUtils.getIntValue(PrefKeys.SUBSCRIPTION_DAYS,-1)<1)
            getSubscriptionStatus();
        else
            UiUtils.log(TAG,"Not Called");

        if(prefUtils.getBoolanValue(PrefKeys.IS_LOGGED_IN, false) &&  !prefUtils.getStringValue(PrefKeys.FCM_TOKEN,"").equalsIgnoreCase(""))
            Utils.sendFCMTokenToServer(this, prefUtils.getStringValue(PrefKeys.FCM_TOKEN, ""));
        else
            MessagingServices.getFCMToken(this);

        //setDDDesign();

        if(prefUtils.getBoolanValue(PrefKeys.IS_LOGGED_IN, false)) {
            SharedPreferenceHelper sharedPreferenceHelper = new SharedPreferenceHelper(this);
            LoginUser.INSTANCE.setCoins(sharedPreferenceHelper.getIntValue(PrefKeysDD.Companion.getUSER_COINS(),0));
        }

        mAppUpdateManager.getAppUpdateInfo().addOnSuccessListener(appUpdateInfo -> {

            //UiUtils.log(TAG, "onResume updateAvailability: " + appUpdateInfo.updateAvailability());
            //UiUtils.log(TAG, "onResume availableVersionCode: " + appUpdateInfo.availableVersionCode());
            //UiUtils.log(TAG, "onResume isUpdateTypeAllowed: " + appUpdateInfo.isUpdateTypeAllowed(IMMEDIATE));
            //UiUtils.log(TAG, "onResume packageName: " + appUpdateInfo.packageName());

            if (appUpdateInfo.updateAvailability() == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS) {

                try {
                    mAppUpdateManager.startUpdateFlowForResult(
                            appUpdateInfo, AppUpdateType.IMMEDIATE, MainActivity.this, RC_APP_UPDATE);

                } catch (IntentSender.SendIntentException e) {
                    UiUtils.log(TAG, Log.getStackTraceString(e));
                }

            } else {
                UiUtils.log(TAG, "checkForAppUpdateAvailability: onresume something else");
            }
        });
    }

    private void setDDDesign(){

        float defaultWeight = 5f;

        if (PrefUtils.getInstance(this).getBoolanValue(PrefKeys.IS_LOGGED_IN, false)) {
            llDD.setVisibility(View.VISIBLE);
        }else{
            defaultWeight = 4f;
            llDD.setVisibility(View.GONE);
        }

        /*LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);*/
        rlBottomNavigationBarContainer.setWeightSum(defaultWeight);
        //rlBottomNavigationBarContainer.setLayoutParams(param);
    }

    public void prepareGoogleAnalytics() {
        try {
            Bundle bundle = new Bundle();
            bundle.putString(FirebaseAnalytics.Param.ITEM_ID, String.valueOf(id));
            bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, name);
            bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, IMAGE);
            mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);

            //After setting a user ID, all future events will be automatically tagged with this value, and you can access it by querying for the user_id value in BigQuery
            mFirebaseAnalytics.setUserId(String.valueOf(id));

            //Add Events
        /*Bundle params = new Bundle();
        params.putString("btn_name", "Name");
        mFirebaseAnalytics.logEvent("share_btn", params);*/
            //mFirebaseAnalytics.setUserProperty(USER_ID, String.valueOf(id));
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    private void sendUnreportedDownloadStatuses() {
        UiUtils.log(TAG,"Sending unreported stuff");
        try {
            if (NetworkUtils.isNetworkConnected(MainActivity.this)) {
                //cancel
                try {

                    String pendingCancels = prefUtils.getStringValue(PrefKeys.CANCEL_VIDEOS_DOWNLOAD, "");
                    UiUtils.log(TAG,"Download AdminID Cancels:"+ pendingCancels);
                    String[] strSplit = pendingCancels.split(",");

                    ArrayList<String> cancelArrayList = new ArrayList<String>(Arrays.asList(strSplit));
                    for (String id : cancelArrayList) {
                        if (id.trim().length() > 0) {
                            try {
                                UiUtils.log(TAG, "Download CancelAdminID:" + id.split("_")[0] + " CancelDownloadID:" + id.split("_")[1]);
                                Downloader.downloadCanceled(MainActivity.this, Integer.parseInt(id.split("_")[0]), Integer.parseInt(id.split("_")[1]));
                            } catch (NumberFormatException e) {
                                UiUtils.log(TAG, Log.getStackTraceString(e));
                            }
                        }
                    }

                }catch (Exception e)
                {
                    UiUtils.log(TAG, Log.getStackTraceString(e));
                }

                //pause
                try {

                    String pendingPauses = prefUtils.getStringValue(PrefKeys.PAUSE_VIDEOS_DOWNLOAD, "");
                    UiUtils.log(TAG,"Pause AdminID:"+ pendingPauses);
                    String[] strSplit = pendingPauses.split(",");
                    ArrayList<String> pauseArrayList = new ArrayList<String>(Arrays.asList(strSplit));
                    for (String id : pauseArrayList) {
                        if (id.trim().length() > 0) {
                            try {
                                UiUtils.log(TAG, "Pause AdminID:" + id.split("_")[0] + " DownloadID:" + id.split("_")[1]);
                                //Downloader.downloadPause(MainActivity.this, Integer.parseInt(id.split("_")[0]), Integer.parseInt(id.split("_")[1]));
                                Status status = PRDownloader.getStatus(Integer.parseInt(id.split("_")[1]));
                                UiUtils.log(TAG, "Pause Status AdminID:" + status);
                                if (status == Status.PAUSED) {
                                    PRDownloader.resume(Integer.parseInt(id.split("_")[1]));
                                } else if (status == Status.UNKNOWN) {

                                    String cookieValue = "CloudFront-Policy=" + COOKIEPOLICY +
                                            ";CloudFront-Signature=" + COOKIESIGNATURE +
                                            ";CloudFront-Key-Pair-Id=" + COOKIEKEYPAIR;
                                    Downloader downloader = new Downloader(MainActivity.this);
                                    downloader.setOnDownloadListener(MainActivity.this);
                                    downloader.downloadVideo(PRDownloader.download(id.split("_")[2], id.split("_")[3], id.split("_")[4]).setHeader("Cookie", cookieValue).build(), Integer.parseInt(id.split("_")[1]));
                                    /*Intent intent = new Intent();
                                    intent.putExtra(ADMIN_VIDEO_ID, Integer.parseInt(id.split("_")[0]));
                                    intent.putExtra(DOWNLOAD_STATUS, 3);
                                    intent.putExtra(CANCELLED_OR_COMPLETED, false);
                                    intent.setAction(VideoPageActivity.ACTION_DOWNLOAD_UPDATE);
                                    context.sendBroadcast(intent);*/
                                }

                            } catch (NumberFormatException e) {
                                UiUtils.log(TAG, Log.getStackTraceString(e));
                            }
                        }
                    }
                }catch (Exception e)
                {
                    UiUtils.log(TAG, Log.getStackTraceString(e));
                }

                //Report backend: Delete "failed reporting to backend" videos
                try {
                    if (NetworkUtils.isNetworkConnected(MainActivity.this)) {
                        String pendingDeletes = prefUtils.getStringValue(PrefKeys.DELETE_VIDEOS_DOWNLOAD, "");
                        UiUtils.log(TAG,"Delete AdminID:"+ pendingDeletes);
                        String[] strSplit = pendingDeletes.split(",");
                        ArrayList<String> deleteArrayList = new ArrayList<String>(Arrays.asList(strSplit));
                        for (String id : deleteArrayList) {
                            if (id.trim().length() > 0) {
                                try {
                                    UiUtils.log(TAG, "Delete AdminID:" + id.split("_")[0] + " DeleteDownloadID:" + id.split("_")[1]);
                                    Downloader.downloadDeleted(MainActivity.this, Integer.parseInt(id.split("_")[0]), Integer.parseInt(id.split("_")[1]));
                                } catch (NumberFormatException e) {
                                    UiUtils.log(TAG, Log.getStackTraceString(e));
                                }
                            }
                        }
                    }
                } catch (Exception e) {
                    UiUtils.log(TAG,Log.getStackTraceString(e));
                }
            }
        } catch (Exception e) {
            UiUtils.log(TAG,Log.getStackTraceString(e));
        }
    }


    private void getConfigFromBackEnd() {
        Map<String, Object> params = new HashMap<>();
        params.put(APIConstants.Params.ID, id);
        params.put(APIConstants.Params.TOKEN, token);

        Call<String> call = apiInterface.getAppConfigs(APIConstants.APIs.GET_APP_CONFIG, params);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                JSONObject configResponse = null;
                try {
                    configResponse = new JSONObject(response.body());
                } catch (Exception e) {
                    UiUtils.log(TAG, Log.getStackTraceString(e));
                }
                if (configResponse != null) {
                    if (configResponse.optString(APIConstants.Params.SUCCESS).equals(APIConstants.Constants.TRUE)) {
                        JSONObject data = configResponse.optJSONObject(APIConstants.Params.DATA);
                        UiUtils.log(TAG,String.valueOf(ConfigParser.getConfig(data)));
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                UiUtils.hideLoadingDialog();
            }
        });
    }

    public void setIvHome(){
     /*   LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(0,LinearLayout.LayoutParams.MATCH_PARENT);
        param.weight = 2.0f;
        llHome.setLayoutParams(param);
        LinearLayout.LayoutParams param1 = new LinearLayout.LayoutParams(0,LinearLayout.LayoutParams.MATCH_PARENT);
        param1.weight = 1.0f;
        llSearch.setLayoutParams(param1);
        llProfile.setLayoutParams(param1);
        llYourVideos.setLayoutParams(param1);*/

        ivHome.setVisibility(View.GONE);
        ivSearch.setVisibility(View.VISIBLE);
        ivDownload.setVisibility(View.VISIBLE);
        ivMore.setVisibility(View.VISIBLE);
        ivDD.setVisibility(View.VISIBLE);

        tvHome.setVisibility(View.VISIBLE);
        tvSearch.setVisibility(View.GONE);
        tvYourVideos.setVisibility(View.GONE);
        tvProfile.setVisibility(View.GONE);
        tvDD.setVisibility(View.GONE);

        navigateToHomeScreen();
    }

    public void setIvSearch(){
/*        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(0,LinearLayout.LayoutParams.MATCH_PARENT);
        param.weight = 2.0f;
        llSearch.setLayoutParams(param);
        LinearLayout.LayoutParams param1 = new LinearLayout.LayoutParams(0,LinearLayout.LayoutParams.MATCH_PARENT);
        param1.weight = 1.0f;
        llHome.setLayoutParams(param1);
        llProfile.setLayoutParams(param1);
        llYourVideos.setLayoutParams(param1);*/

        ivHome.setVisibility(View.VISIBLE);
        ivSearch.setVisibility(View.GONE);
        ivDownload.setVisibility(View.VISIBLE);
        ivMore.setVisibility(View.VISIBLE);
        ivDD.setVisibility(View.VISIBLE);

        tvHome.setVisibility(View.GONE);
        tvSearch.setVisibility(View.VISIBLE);
        tvYourVideos.setVisibility(View.GONE);
        tvProfile.setVisibility(View.GONE);
        tvDD.setVisibility(View.GONE);

        navigateToExploreScreen();
    }

    public void setIvDownload(){
 /*       LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(0,LinearLayout.LayoutParams.MATCH_PARENT);
        param.weight = 2.0f;
        llYourVideos.setLayoutParams(param);
        LinearLayout.LayoutParams param1 = new LinearLayout.LayoutParams(0,LinearLayout.LayoutParams.MATCH_PARENT);
        param1.weight = 1.0f;
        llHome.setLayoutParams(param1);
        llSearch.setLayoutParams(param1);
        llProfile.setLayoutParams(param1);*/

        ivHome.setVisibility(View.VISIBLE);
        ivSearch.setVisibility(View.VISIBLE);
        ivDownload.setVisibility(View.GONE);
        ivMore.setVisibility(View.VISIBLE);
        ivDD.setVisibility(View.VISIBLE);

        tvHome.setVisibility(View.GONE);
        tvSearch.setVisibility(View.GONE);
        tvYourVideos.setVisibility(View.VISIBLE);
        tvProfile.setVisibility(View.GONE);
        tvDD.setVisibility(View.GONE);

        navigateToDownloadScreen();
    }

    public void setIvMore(){
/*        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(0,LinearLayout.LayoutParams.MATCH_PARENT);
        param.weight = 2.0f;
        llProfile.setLayoutParams(param);
        LinearLayout.LayoutParams param1 = new LinearLayout.LayoutParams(0,LinearLayout.LayoutParams.MATCH_PARENT);
        param1.weight = 1.0f;
        llHome.setLayoutParams(param1);
        llSearch.setLayoutParams(param1);
        llYourVideos.setLayoutParams(param1);*/

        ivHome.setVisibility(View.VISIBLE);
        ivSearch.setVisibility(View.VISIBLE);
        ivDownload.setVisibility(View.VISIBLE);
        ivMore.setVisibility(View.GONE);
        ivDD.setVisibility(View.VISIBLE);

        tvHome.setVisibility(View.GONE);
        tvSearch.setVisibility(View.GONE);
        tvYourVideos.setVisibility(View.GONE);
        tvProfile.setVisibility(View.VISIBLE);
        tvDD.setVisibility(View.GONE);

        navigateToMoreScreen();
    }

    public void setIvDD(){

        ivHome.setVisibility(View.VISIBLE);
        ivSearch.setVisibility(View.VISIBLE);
        ivDownload.setVisibility(View.VISIBLE);
        ivMore.setVisibility(View.VISIBLE);
        ivDD.setVisibility(View.GONE);

        tvHome.setVisibility(View.GONE);
        tvSearch.setVisibility(View.GONE);
        tvYourVideos.setVisibility(View.GONE);
        tvProfile.setVisibility(View.GONE);
        tvDD.setVisibility(View.VISIBLE);

        navigateToDDScreen();
    }

    public void navigateToHomeScreen(){

        Fragment f = getSupportFragmentManager().findFragmentById(R.id.container);
        if(f instanceof VideoContentFragment){
            return;
        }
        contentFragment = VideoContentFragment.getInstance("HOME", 0, 0, 0);
        replaceFragmentWithAnimation(contentFragment, HOME_FRAGMENTS[0], false);
    }

    public void navigateToExploreScreen(){

        Fragment f = getSupportFragmentManager().findFragmentById(R.id.container);
        if(f instanceof SearchFragment){
            return;
        }

        replaceFragmentWithAnimation(new SearchFragment(), HOME_FRAGMENTS[1], false);

    }

    public void navigateToDownloadScreen(){

        Fragment f = getSupportFragmentManager().findFragmentById(R.id.container);
        if(f instanceof DownloadsFragment){
            return;
        }

        replaceFragmentWithAnimation(new DownloadsFragment(), HOME_FRAGMENTS[3], false);
    }

    public void navigateToMoreScreen(){

        Fragment f = getSupportFragmentManager().findFragmentById(R.id.container);
        if(f instanceof SettingsFragment){
            return;
        }

        replaceFragmentWithAnimation(new SettingsFragment(), HOME_FRAGMENTS[4], false);
    }

    public void navigateToDDScreen(){

        Fragment f = getSupportFragmentManager().findFragmentById(R.id.container);
        if (PrefUtils.getInstance(this).getBoolanValue(PrefKeys.IS_LOGGED_IN, false)) {
            if (f instanceof ProfileFragment) {
                return;
            }
            replaceFragmentWithAnimation(new ProfileFragment(), HOME_FRAGMENTS[5], false);
        }else{
            replaceFragmentWithAnimation(new LoginDDFragment(), HOME_FRAGMENTS[6], false);
        }
    }

    private void clearFragmentBackStack() {
        FragmentManager fm = getSupportFragmentManager();
        for (int i = 0; i < fm.getBackStackEntryCount(); ++i) {
            fm.popBackStack();
        }
    }

    private int getFragmentBackStackSize() {
        FragmentManager fm = getSupportFragmentManager();
        return fm.getBackStackEntryCount();
    }

    public void replaceFragmentWithAnimation(Fragment fragment, String tag, boolean toBackStack) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        CURRENT_FRAGMENT = tag;
        this.fragment = fragment;
        if (toBackStack) {
            transaction.addToBackStack(tag);
        }
        transaction.replace(R.id.container, fragment);
        transaction.commitAllowingStateLoss();
    }

    @Override
    public void onBackPressed() {
        if (!CURRENT_FRAGMENT.equals(HOME_FRAGMENTS[0])) {
            if (getFragmentBackStackSize() > 0) {
                super.onBackPressed();
                return;
            } else {
                setIvHome();
                return;
            }
        }

        if (doubleTapToExitApp) {
            super.onBackPressed();
            finish();
            return;
        }

        doubleTapToExitApp = true;
        UiUtils.showShortToast(this, getString(R.string.press_back_again_exit));
        new Handler().postDelayed(() -> doubleTapToExitApp = false, 2000);
    }


    @Override
    public void onStart() {
        super.onStart();
        registerEventBus();
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        connectivityReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if(NetworkUtils.isNetworkConnected(context)) {
                    UiUtils.log("NETWORK", "Internet Connected");
                    sendUnreportedDownloadStatuses();
                    UiUtils.hideNoInternetConnection();
                }
            }
        };
        registerReceiver(connectivityReceiver, filter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        try {
            unregisterReceiver(connectivityReceiver);
        }catch (Exception e)
        {

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterEventBus();
    }

    @Subscribe//(threadMode = ThreadMode.MAIN)
    public void onTokenExpiry(APIEvent event) {
        unregisterEventBus();
        logOutUserFromDevice();
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onMessage(NavigateToScreenEvent event) {

        if(event !=null){

            if(event.getScreenId()==1){
                navigateToHomeScreen();
                setIvHome();
            }

            if(event.getScreenId() == 3){
                navigateToDownloadScreen();
                setIvDownload();
            }
        }

        if(event != null) {
            EventBus.getDefault().removeStickyEvent(event);
        }

    }

    private void logOutUserFromDevice() {
        PrefHelper.setUserLoggedOut(MainActivity.this);
        Intent restartActivity = new Intent(MainActivity.this, SplashActivity.class);
        restartActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(restartActivity);
        MainActivity.this.finish();
    }

    private void registerEventBus() {
        if (!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this);
    }

    private void unregisterEventBus() {
        if (EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().unregister(this);
    }

    private void getAllQueryTypes()
    {
        Call<String> call = apiInterface.getHelpQueries(GETHELPQUERIES);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                JSONArray helpQueriesResponse = null;
                try {
                    helpQueriesResponse = new JSONArray(response.body());
                } catch (Exception e) {
                    UiUtils.log(TAG, Log.getStackTraceString(e));
                }
                if (helpQueriesResponse != null) {
                    supportArrayList = parseSupportData(helpQueriesResponse);
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                NetworkUtils.onApiError(MainActivity.this);
            }
        });
    }

    private void getAppVersionFromServer()
    {
        UiUtils.log(TAG,"Version API Called");

        Call<String> call = apiInterface.getAppVersionFromServer(GET_SERVER_APP_VERSION);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                JSONObject versionStatusResponse = null;
                try {
                    versionStatusResponse = new JSONObject(response.body());
                } catch (Exception e) {
                    UiUtils.log(TAG,Log.getStackTraceString(e));
                }
                if (versionStatusResponse != null) {
                    if (versionStatusResponse.optString(SUCCESS).equals(APIConstants.Constants.TRUE)) {
                        JSONObject versionArray = versionStatusResponse.optJSONObject(APIConstants.Params.DATA);
                        //dialog.dismiss();
                        try {
                            if (versionArray.optInt(SERVER_APP_VERSION) > BuildConfig.VERSION_CODE) {
                                SERVER_VERSION_CODE = versionArray.optInt(SERVER_APP_VERSION);
                                SERVER_VERSION_NAME = versionArray.optString(NAME);
                                SHOW_UPDATE_BUTTON = versionArray.optInt("update")==1;
                                //updateApp(versionArray.optInt(UPDATE_APP_COMPULSORY));
                            }
                        } catch (Exception e) {
                            UiUtils.log(TAG,Log.getStackTraceString(e));
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                NetworkUtils.onApiError(MainActivity.this);
            }
        });
    }

    private void getSubscriptionStatus()
    {
        UiUtils.log("TAG","API Called");
        Map<String, Object> params = new HashMap<>();
        params.put(APIConstants.Params.ID, prefUtils.getIntValue(PrefKeys.USER_ID, -1));
        params.put(APIConstants.Params.TOKEN, prefUtils.getStringValue(PrefKeys.SESSION_TOKEN, ""));
        params.put(APIConstants.Params.IP, CURRENT_IP);

        Call<String> call = apiInterface.getSubscriptionStatus(USER_SUBSCRIPTION, params);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                JSONObject subscriptionStatusResponse = null;
                try {
                    subscriptionStatusResponse = new JSONObject(response.body());
                } catch (Exception e) {
                    UiUtils.log(TAG, Log.getStackTraceString(e));
                }
                /*if (subscriptionStatusResponse != null) {
                    supportArrayList = parseSupportData(subscriptionStatusResponse);
                }*/
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                NetworkUtils.onApiError(MainActivity.this);
            }
        });
    }

    private ArrayList<Support> parseSupportData(JSONArray dataArray) {

        ArrayList<Support> parseSupportArrayList  = new ArrayList<>();
        try {
            for (int i = 0; i < dataArray.length(); i++) {
                JSONObject dataObj = dataArray.getJSONObject(i);
                Support support = new Support(dataObj.optInt(APIConstants.Params.ID),
                        dataObj.optString(NAME),
                        dataObj.optString(APIConstants.Params.STATUS));
                parseSupportArrayList.add(support);
            }
        }catch (Exception e)
        {
            UiUtils.log(TAG, Log.getStackTraceString(e));
        }
        return parseSupportArrayList;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.llHome:
                setIvHome();
                break;
            case R.id.llSearch:
                setIvSearch();
                break;
            case R.id.llProfile:
                setIvMore();
                break;
            case R.id.llYourVideos:
                setIvDownload();
                break;
            case R.id.llDD:
                setIvDD();
                break;


        }

    }

    @Override
    public void downloadCompleted(int adminVideoId) {
        Intent intent = new Intent();
        intent.putExtra(ADMIN_VIDEO_ID, adminVideoId);
        intent.putExtra(DOWNLOAD_STATUS, 4);
        //intent.putExtra(CANCELLED_OR_COMPLETED, cancelledOrCompleted);
        intent.setAction(VideoPageActivity.ACTION_DOWNLOAD_UPDATE);
        sendBroadcast(intent);
    }

    @Override
    public void downloadCancelled(int adminVideoId) {

    }

    @Override
    public void downloadPaused(int adminVideoId) {
        Intent intent = new Intent();
        intent.putExtra(ADMIN_VIDEO_ID, adminVideoId);
        intent.putExtra(DOWNLOAD_STATUS, 3);
        //intent.putExtra(CANCELLED_OR_COMPLETED, cancelledOrCompleted);
        intent.setAction(VideoPageActivity.ACTION_DOWNLOAD_UPDATE);
        sendBroadcast(intent);
    }

    //update App
    private void updateApp(int compulsoryUpdate)
    {
        updateDialog = new Dialog(MainActivity.this);

        updateDialog.setContentView(R.layout.dialog_update_popup);
        updateDialog.setCancelable(false);
        updateDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        TextView btnNotNow = updateDialog.findViewById(R.id.btnNotNow);
        UiUtils.log(TAG,"compulsoryUpdate: "+  compulsoryUpdate);
        if(compulsoryUpdate == 1) {
            btnNotNow.setVisibility(View.GONE);
        }else
        {
            btnNotNow.setOnClickListener(v -> {
                CANCEL_UPDATE = true;
                updateDialog.dismiss();
            });
        }

        TextView txtUpdate = updateDialog.findViewById(R.id.btnUpdate);
        txtUpdate.setOnClickListener((View v) -> {
            //dialog.dismiss();

            Utils.openPlayStorePage(this);

        });

        if(!updateDialog.isShowing())
            updateDialog.show();
        Window window = updateDialog.getWindow();
        window.setLayout(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        //window.setGravity(Gravity.CENTER);

    }

    public void getPermission() {
        /*EasyPermissions.requestPermissions(this, getString(R.string.needPermissionToAccessYourStoarge),
                STORAGE_PERMISSION, Manifest.permission.WRITE_EXTERNAL_STORAGE);*/
        if (Build.VERSION.SDK_INT >= 33) {
            EasyPermissions.requestPermissions(this, getString(R.string.needPermissionToKeepNotified),
                    NOTIFICATION_PERMISSION, Manifest.permission.POST_NOTIFICATIONS);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    private void getDataFromShareUrl()
    {
        try {
            Intent appLinkIntent = getIntent();
            String appLinkAction = appLinkIntent.getAction();
            Uri appLinkData = appLinkIntent.getData();
            //String id = appLinkData.toString().split("\\?")[1].split("=")[1];
            UiUtils.log(TAG, "appLinkAction: " + appLinkAction);
            UiUtils.log(TAG, "appLinkData: " + appLinkData);

            if(appLinkIntent !=null) {

                if(appLinkData!=null) {

                    String jsonData = appLinkData.toString().split("\\?")[1];

                    if (jsonData != null) {
                        UiUtils.log(TAG, "?-> : " + appLinkData.toString().split("\\?")[1]);
                        UiUtils.log(TAG, "id-> : " + jsonData.split("&")[0].split("=")[1]);
                        UiUtils.log(TAG, "parentId-> : " + jsonData.split("&")[1].split("=")[1]);
                        UiUtils.log(TAG, "seasonId-> : " + jsonData.split("&")[2].split("=")[1]);
                        UiUtils.log(TAG, "code-> : " + jsonData.split("&")[3].split("=")[1]);
                        //UiUtils.log(TAG, "referrerId-> : " + jsonData.split("&")[4].split("=")[1]);

                        String videoId = jsonData.split("&")[0].split("=")[1];
                        String parentId = jsonData.split("&")[1].split("=")[1];
                        String seasonId = jsonData.split("&")[2].split("=")[1];
                        String refCode = jsonData.split("&")[3].split("=")[1];
                        String refId = "";
                        try{refId = jsonData.split("&")[4].split("=")[1];}
                        catch (Exception e){ UiUtils.log(TAG, Log.getStackTraceString(e));}

                        prefUtils.setValue(PrefKeys.REFERENCE_URL_CODE, refCode);
                        prefUtils.setValue(PrefKeys.REFERENCE_URL_ID, refId);

                        //send to video page
                        Intent toVideo = new Intent(this, VideoPageActivity.class);
                        toVideo.putExtra(ADMIN_VIDEO_ID, Integer.valueOf(videoId));
                        toVideo.putExtra(PARENT_ID, checkString(parentId)? Integer.valueOf(videoId):Integer.valueOf(parentId));
                        toVideo.putExtra(SEASON_ID, checkString(seasonId)? -1 : Integer.parseInt(seasonId));
                        toVideo.putExtra(IMAGE, "");
                        toVideo.putExtra(TITLE, "");
                        //toVideo.putExtra(REF_ID,  Integer.valueOf(refId));
                        startActivity(toVideo);

                    } else {
                        UiUtils.log(TAG, "Json data invalid");
                    }
                } else {
                    if(getIntent().getExtras()!=null) {
                        setIvHome();

                        UiUtils.log(TAG,"Type: "+ getIntent().getStringExtra(TYPE));
                        //Subscription
                        if (getIntent().getStringExtra(TYPE)!=null &&
                                getIntent().getStringExtra(TYPE).equalsIgnoreCase("Subscription")) {
                            UiUtils.log(TAG, "Subscription Notification");
                            if (prefUtils.getIntValue(PrefKeys.USER_ID, -1) != -1) {
                                //go to subscribe Page
                                startActivity(new Intent(this, PlansActivity.class));
                            } else {
                                //go to login pageNewLoginActivity
                                startActivity(new Intent(this, NewLoginActivity.class));
                            }
                            //NotificationManager manager = (NotificationManager) getApplicationContext().getSystemService(NOTIFICATION_SERVICE);
                            //manager.cancel(0);
                        }
                        //Update
                        else if (getIntent().getStringExtra(TYPE)!=null &&
                                getIntent().getStringExtra(TYPE).equalsIgnoreCase("Update")) {
                            /*startActivity(new Intent(this, UpdateAppActivity.class)
                                    .putExtra(VERSION_CODE, getIntent().getIntExtra(VERSION_CODE, -1))
                                    .putExtra(APPVERSION, getIntent().getStringExtra(APPVERSION)));*/

                            Utils.openPlayStorePage(MainActivity.this);

                            //NotificationManager manager = (NotificationManager) getApplicationContext().getSystemService(NOTIFICATION_SERVICE);
                            //manager.cancel(0);

                        }
                        else if(getIntent().getStringExtra(TYPE)!=null &&
                                getIntent().getStringExtra(TYPE).equalsIgnoreCase("Download"))
                        {
                            setIvDownload();
                            //replaceFragmentWithAnimation(new DownloadsFragment(), HOME_FRAGMENTS[3], false);
                            //NotificationManager manager = (NotificationManager) getApplicationContext().getSystemService(NOTIFICATION_SERVICE);
                            //manager.cancel(0);
                        }
                        else if(getIntent().getStringExtra(TYPE)!=null &&
                                getIntent().getStringExtra(TYPE).equalsIgnoreCase("Login"))
                        {
                            setIvMore();
                            //replaceFragmentWithAnimation(new DownloadsFragment(), HOME_FRAGMENTS[3], false);
                            //NotificationManager manager = (NotificationManager) getApplicationContext().getSystemService(NOTIFICATION_SERVICE);
                            //manager.cancel(0);
                        }
                        else if(getIntent().getStringExtra(TYPE)!=null &&
                                getIntent().getStringExtra(TYPE).equalsIgnoreCase("Video"))
                        {
                            UiUtils.log(TAG, "Normal Notification");
                            UiUtils.log(TAG, "No data available");
                            UiUtils.log(TAG, "Data Id: " + getIntent().getIntExtra(ADMIN_VIDEO_ID, -1));
                            if (getIntent().getIntExtra(ADMIN_VIDEO_ID, -1) != -1) {

                                Intent intent = new Intent(getApplicationContext(), VideoPageActivity.class);
                                intent.putExtra(ADMIN_VIDEO_ID, getIntent().getIntExtra(ADMIN_VIDEO_ID, -1));
                                intent.putExtra(PARENT_ID, getIntent().getIntExtra(PARENT_ID, -1));
                                intent.putExtra(SEASON_ID, getIntent().getIntExtra(SEASON_ID, -1));
                                intent.putExtra(IMAGE, "");
                                intent.putExtra(TITLE, "");
                                /*if (getIntent().getStringExtra("type")!=null && getIntent().getStringExtra("type").equalsIgnoreCase("Subscribe")) {
                                    intent.putExtra("type", "Subscribe");
                                    NotificationManager manager = (NotificationManager) getApplicationContext().getSystemService(NOTIFICATION_SERVICE);
                                    manager.cancel(0);
                                }*/
                                startActivity(intent);
                            }
                        }
                    }
                }
            }

        }catch (Exception e)
        {
            UiUtils.log(TAG, Log.getStackTraceString(e));
        }
    }

    public void getInstallReferrer()
    {
        referrerClient = InstallReferrerClient.newBuilder(this).build();

        referrerClient.startConnection(new InstallReferrerStateListener() {
            @Override
            public void onInstallReferrerSetupFinished(int responseCode) {
                // this method is called when install referer setup is finished.
                switch (responseCode) {
                    // we are using switch case to check the response.
                    case InstallReferrerClient.InstallReferrerResponse.OK:
                        // this case is called when the status is OK and
                        ReferrerDetails response = null;
                        try {
                            // on below line we are getting referrer details
                            // by calling get install referrer.
                            response = referrerClient.getInstallReferrer();
                            System.out.println(response);
                            // on below line we are getting referrer url.
                            String referrerUrl = response.getInstallReferrer();

                            // on below line we are getting referrer click time.
                            long referrerClickTime = response.getReferrerClickTimestampSeconds();

                            // on below line we are getting app install time
                            long appInstallTime = response.getInstallBeginTimestampSeconds();

                            // on below line we are getting our time when
                            // user has used our apps instant experience.
                            boolean instantExperienceLaunched = response.getGooglePlayInstantParam();

                            String appVersion = response.getInstallVersion();

                            // on below line we are getting our
                            // apps install referrer.
                            String refrer = response.getInstallReferrer();
                            prefUtils.setValue(PrefKeys.REFERENCE_URL_CODE, refrer);
                            UiUtils.log("Referrer","Referrer is : " + refrer);

                            // on below line we are setting all detail to our text view.
                            UiUtils.log("Referrer","Referrer is : \n" + referrerUrl + "\n" + "Referrer Click Time is : " + referrerClickTime +
                                    "\nApp Install Time : " + appInstallTime + "\nApp Install Version : " + appVersion);

                            //Toast.makeText(MainActivity.this,"Data Received From Main Activity\n "+ refrer,Toast.LENGTH_LONG).show();

                            if(checkString(refrer))
                                refrer = getResources().getString(R.string.app_name);

                            Utils.sendShareLinkUrlDetailsToServer(MainActivity.this,refrer,SHARE_TYPE_INSTALL,1);

                        } catch (RemoteException e) {
                            // handling error case.
                            UiUtils.log("Referrer", Log.getStackTraceString(e));
                        }
                        break;
                    case InstallReferrerClient.InstallReferrerResponse.FEATURE_NOT_SUPPORTED:
                        // API not available on the current Play Store app.
                        //Toast.makeText(MainActivity.this, "Feature not supported..", Toast.LENGTH_SHORT).show();
                        UiUtils.log("Referrer","Error: Feature not supported..");
                        break;
                    case InstallReferrerClient.InstallReferrerResponse.SERVICE_UNAVAILABLE:
                        // Connection couldn't be established.
                        //Toast.makeText(MainActivity.this, "Fail to establish connection", Toast.LENGTH_SHORT).show();
                        UiUtils.log("Referrer","Error: Fail to establish connection");
                        break;
                }
            }

            @Override
            public void onInstallReferrerServiceDisconnected() {
                // Try to restart the connection on the next request to
                // Google Play by calling the startConnection() method.
                //Toast.makeText(MainActivity.this, "Service disconnected..", Toast.LENGTH_SHORT).show();
                UiUtils.log(TAG,"Error: Service disconnected..");
            }
        });
    }

    private void checkIfUpdateAvailable()
    {

        mAppUpdateManager.getAppUpdateInfo().addOnSuccessListener(appUpdateInfo -> {

            UiUtils.log(TAG, "updateAvailability: " + appUpdateInfo.updateAvailability());
            UiUtils.log(TAG, "availableVersionCode: " + appUpdateInfo.availableVersionCode());
            UiUtils.log(TAG, "isUpdateTypeAllowed: " + appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE));
            UiUtils.log(TAG, "packageName: " + appUpdateInfo.packageName());

            /*Toast.makeText(this, ""+appUpdateInfo.updateAvailability() + " -> "+ appUpdateInfo.availableVersionCode() +" -> "+
                    appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE),Toast.LENGTH_LONG ).show();*/
                /*if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                        && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE *//*AppUpdateType.IMMEDIATE*//*)) {

                    try {
                        mAppUpdateManager.startUpdateFlowForResult(
                                appUpdateInfo, AppUpdateType.FLEXIBLE *//*AppUpdateType.IMMEDIATE*//*, MainActivity.this, RC_APP_UPDATE);

                    } catch (IntentSender.SendIntentException e) {
                         UiUtils.log(TAG, Log.getStackTraceString(e));
                    }

                }else*/ if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                    && appUpdateInfo.isUpdateTypeAllowed(/*IMMEDIATE */AppUpdateType.IMMEDIATE)) {

                try {
                    mAppUpdateManager.startUpdateFlowForResult(
                            appUpdateInfo, AppUpdateType.IMMEDIATE, MainActivity.this, RC_APP_UPDATE);

                } catch (IntentSender.SendIntentException e) {
                    UiUtils.log(TAG, Log.getStackTraceString(e));
                }

            } else if (appUpdateInfo.installStatus() == InstallStatus.DOWNLOADED) {
                //CHECK THIS if AppUpdateType.FLEXIBLE, otherwise you can skip
                UiUtils.log(TAG, "App Downloaded");
                //popupSnackbarForCompleteUpdate();
            } else {
                UiUtils.log(TAG, "checkForAppUpdateAvailability: something else");
            }
        });
        //}
    }
}
