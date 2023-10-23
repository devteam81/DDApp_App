package com.dd.app.ui.fragment;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.dd.app.gcm.MessagingServices;
import com.dd.app.util.sharedpref.Utils;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.dd.app.R;
import com.dd.app.network.APIClient;
import com.dd.app.network.APIConstants;
import com.dd.app.network.APIInterface;
import com.dd.app.ui.activity.ForgotPasswordActivity;
import com.dd.app.ui.activity.NewLoginActivity;
import com.dd.app.util.NetworkUtils;
import com.dd.app.util.UiUtils;
import com.dd.app.util.sharedpref.PrefHelper;
import com.dd.app.util.sharedpref.PrefKeys;
import com.dd.app.util.sharedpref.PrefUtils;

import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.dd.app.network.APIConstants.APIs.USER_SUBSCRIPTION;
import static com.dd.app.network.APIConstants.Params;
import static com.dd.app.network.APIConstants.Params.DEVICE_CODE;
import static com.dd.app.network.APIConstants.Params.DISTRIBUTOR;
import static com.dd.app.network.APIConstants.Params.REFERRER;
import static com.dd.app.network.APIConstants.Params.SHARE_TYPE_LOGIN;
import static com.dd.app.network.APIConstants.Params.VERSION_CODE;
import static com.dd.app.ui.activity.MainActivity.CURRENT_IP;
import static com.dd.app.util.UiUtils.checkString;
import static com.dd.app.util.sharedpref.Utils.getSecureId;

public class LoginFragment extends Fragment {

    private final String TAG = LoginFragment.class.getSimpleName();
    private static final int RC_SIGN_IN = 100;

    @BindView(R.id.edt_phone)
    EditText edtPhone;
    @BindView(R.id.rl_layout_phone)
    RelativeLayout layoutPhone;
    @BindView(R.id.edt_password)
    EditText edtPassword;
    @BindView(R.id.rl_layout_password)
    RelativeLayout layoutPassword;

    @BindView(R.id.txtGoogle)
    TextView txtGoogle;
    @BindView(R.id.txtFacebook)
    TextView txtFacebook;


    GoogleSignInClient mGoogleSignInClient;
    CallbackManager callbackManager;
    private NewLoginActivity activity;
    private Unbinder unbinder;
    private APIInterface apiInterface;
    private PrefUtils prefUtils;
    private FirebaseAuth mAuth;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (NewLoginActivity) getActivity();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .requestId()
                .requestProfile()
                .build();

        Log.d("onCreatetest", "onCreate: ");


        //Fb setup
        callbackManager = CallbackManager.Factory.create();


        //Google setup
        FirebaseAuth.getInstance().signOut();
        mAuth = FirebaseAuth.getInstance();
        mGoogleSignInClient = GoogleSignIn.getClient(activity, gso);

        prefUtils = PrefUtils.getInstance(activity);
        if(prefUtils.getStringValue(PrefKeys.FCM_TOKEN,"").equalsIgnoreCase(""))
            MessagingServices.getFCMToken(activity);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_login, container, false);
        unbinder = ButterKnife.bind(this, view);
        apiInterface = APIClient.getClient().create(APIInterface.class);
        checkWidth();
        return view;
    }

    @OnClick({R.id.img_password})
    public void showHidePass(View view){

        if(view.getId()==R.id.img_password){

            if(edtPassword.getTransformationMethod().equals(PasswordTransformationMethod.getInstance())){
                ((ImageView)(view)).setImageResource(R.drawable.ic_eye_show);
                //Show Password
                edtPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            }
            else{
                ((ImageView)(view)).setImageResource(R.drawable.ic_eye_hide);
                //Hide Password
                edtPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());

            }
        }
    }

    @OnClick({R.id.ll_google_sign, R.id.ll_facebook_sign, R.id.submit_btn, R.id.forgot_pass})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_google_sign:
                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, RC_SIGN_IN);
                break;
            case R.id.ll_facebook_sign:
                LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile", "email"/*, "user_friends"*/));
                // LoginManager.getInstance().setLoginBehavior(LoginBehavior.WEB_VIEW_ONLY);
                LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        if (AccessToken.getCurrentAccessToken() != null) {
                            GraphRequest request = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(), (object, response) -> {
                                UiUtils.log(TAG,"Object--> "+object);
                                doSocialLoginUser(object.optString("id")
                                        , object.optString("email")
                                        , object.optString("name")
                                        , "https://graph.facebook.com/" + object.optString("id") + "/picture?type=large"
                                        , APIConstants.Constants.FACEBOOK_LOGIN);
                            });
                            Bundle params = new Bundle();
                            params.putString("fields", "id,name,link,email,picture");
                            request.setParameters(params);
                            request.executeAsync();
                        }
                        UiUtils.log(TAG, "onSuccess: ");
                        //handleFacebookAccessToken(loginResult.getAccessToken());
                    }

                    @Override
                    public void onCancel() {
                        UiUtils.log(TAG, "onCancel: ");
                    }

                    @Override
                    public void onError(FacebookException error) {
                        UiUtils.log(TAG, "onError: "+error.getLocalizedMessage());
                    }
                });
                break;
            case R.id.submit_btn:
                String phoneNo = edtPhone.getText().toString().trim();
                String password = edtPassword.getText().toString().trim();

                //boolean result = validateFields(phoneNo, password);
                if (validateFields(phoneNo, password)) {
                    doLoginUser();
                }
                break;
            case R.id.forgot_pass:
                startActivity(new Intent(activity, ForgotPasswordActivity.class));
                break;
        }
    }

    public boolean validateFields(String phoneNo, String password) {
        //Phone validation
        if (phoneNo.length() == 0) {
            UiUtils.showShortToast(activity, getString(R.string.enter_phone_no));
            return false;
        }

        if (phoneNo.length() < 6 || phoneNo.length() > 16) {
            UiUtils.showShortToast(getActivity(), getString(R.string.phone_cant_be_stuff));
            return false;
        }
        if (password.length() == 0) {
            UiUtils.showShortToast(activity, getString(R.string.password_cant_be_empty));
            return false;
        }
        /*if (!AppUtils.isValidEmail(edPhone.getText().toString())) {
            UiUtils.showShortToast(activity, getString(R.string.enter_valid_email));
            return false;
        }*/
        if (password.length() < 6) {
            UiUtils.showShortToast(activity, getString(R.string.minimum_six_characters));
            return false;
        }
        return true;
    }

    protected void doLoginUser() {
        UiUtils.showLoadingDialog(activity);
        HashMap<String,String> deviceDetails =  Utils.getDeviceDetails(getActivity());

        Map<String, String> params = new HashMap<>();
        params.put(Params.MOBILE, edtPhone.getText().toString());
        params.put(Params.PASSWORD, edtPassword.getText().toString());
        params.put(Params.LOGIN_BY, APIConstants.Constants.MANUAL_LOGIN);
        params.put(Params.DEVICE_TYPE, APIConstants.Constants.ANDROID);
        params.put(Params.DEVICE_TOKEN, prefUtils.getStringValue(PrefKeys.FCM_TOKEN,""));
        params.put(Params.TIME_ZONE, TimeZone.getDefault().getID());
        params.put(Params.LANGUAGE, prefUtils.getStringValue(PrefKeys.APP_LANGUAGE_STRING, ""));
        params.put(Params.PHONEDETAILS, new JSONObject(deviceDetails).toString());
        params.put(Params.APPVERSION, deviceDetails.get(Params.APPVERSION));
        params.put(VERSION_CODE, deviceDetails.get(VERSION_CODE));
        params.put(Params.DEVICE, deviceDetails.get(Params.DEVICE));
        params.put(Params.BEBUEXT, deviceDetails.get(Params.BEBUEXT));
        params.put(Params.BRAND, deviceDetails.get(Params.BRAND));
        params.put(Params.MODEL, deviceDetails.get(Params.MODEL));
        params.put(Params.VERSION, deviceDetails.get(Params.VERSION));
        params.put(Params.PLAT, deviceDetails.get(Params.PLAT));
        params.put(APIConstants.Params.IP, CURRENT_IP);
        //params.put(Params.DISTRIBUTOR, DISTRIBUTOR);

        Call<String> call = apiInterface.loginUser(APIConstants.APIs.LOGIN, params);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                UiUtils.hideLoadingDialog();
                JSONObject loginResponse = null;
                Log.d("onResponse_test", "onResponse: "+response.body());
                try {
                    loginResponse = new JSONObject(response.body());
                } catch (Exception e) {
                    UiUtils.log(TAG, Log.getStackTraceString(e));
                }
                if (loginResponse != null) {
                    if (loginResponse.optString(APIConstants.Params.SUCCESS).equals(APIConstants.Constants.TRUE)) {
                        UiUtils.showShortToast(activity, loginResponse.optString(APIConstants.Params.MESSAGE));
                        loginUserInDevice(loginResponse, APIConstants.Constants.MANUAL_LOGIN);
                    } else {
                        UiUtils.showShortToast(activity, loginResponse.optString(APIConstants.Params.ERROR_MESSAGE));
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                UiUtils.hideLoadingDialog();
            }
        });
    }

    protected void doSocialLoginUser(String socialUniqueId, String email, String name, String picture, String loginBy) {
        UiUtils.showLoadingDialog(activity);
        HashMap<String,String> deviceDetails =  Utils.getDeviceDetails(activity);
        Map<String, String> params = new HashMap<>();
        params.put(Params.SOCIAL_UNIQUE_ID, socialUniqueId);
        params.put(Params.LOGIN_BY, loginBy);
        params.put(Params.EMAIL, email);
        params.put(Params.NAME, name);
        params.put(Params.MOBILE,"");
        params.put(Params.PICTURE, picture);
        params.put(Params.DEVICE_TYPE, APIConstants.Constants.ANDROID);
        params.put(Params.DEVICE_TOKEN, prefUtils.getStringValue(PrefKeys.FCM_TOKEN,""));
        params.put(Params.TIME_ZONE, TimeZone.getDefault().getID());
        params.put(Params.LANGUAGE, prefUtils.getStringValue(PrefKeys.APP_LANGUAGE_STRING, ""));
        params.put(Params.PHONEDETAILS, new JSONObject(deviceDetails).toString());
        params.put(Params.APPVERSION, deviceDetails.get(Params.APPVERSION));
        params.put(VERSION_CODE, deviceDetails.get(VERSION_CODE));
        params.put(Params.DEVICE, deviceDetails.get(Params.DEVICE));
        params.put(Params.BEBUEXT, deviceDetails.get(Params.BEBUEXT));
        params.put(Params.BRAND, deviceDetails.get(Params.BRAND));
        params.put(Params.MODEL, deviceDetails.get(Params.MODEL));
        params.put(Params.VERSION, deviceDetails.get(Params.VERSION));
        params.put(Params.PLAT, deviceDetails.get(Params.PLAT));
        params.put(Params.IP, CURRENT_IP);
        params.put(DISTRIBUTOR, APIConstants.Constants.DISTRIBUTOR);
        params.put(REFERRER, checkString(prefUtils.getStringValue(PrefKeys.REFERENCE_URL_CODE,""))? getResources().getString(R.string.app_name) : prefUtils.getStringValue(PrefKeys.REFERENCE_URL_CODE,""));
        params.put(DEVICE_CODE, getSecureId(activity));

        Call<String> call = apiInterface.socialLoginUser(APIConstants.APIs.SOCIAL_LOGIN, params);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                UiUtils.hideLoadingDialog();
                Log.d("onResponse_test", "onResponse: 1"+response.body());
                if(response==null){
                    UiUtils.showShortToast(activity, getString(R.string.login_cancelled));
                    return;
                }
                JSONObject socialLoginResponse = null;
                try {
                    socialLoginResponse = new JSONObject(response.body());
                } catch (Exception e) {
                    UiUtils.log(TAG, Log.getStackTraceString(e));
                }
                if (socialLoginResponse != null) {
                    if (socialLoginResponse.optString(APIConstants.Params.SUCCESS).equals(APIConstants.Constants.TRUE)) {
                        UiUtils.showShortToast(activity, getString(R.string.welcome) + name + "!");
                        loginUserInDevice(socialLoginResponse, loginBy);
                    } else {
                        UiUtils.showShortToast(activity, socialLoginResponse.optString(APIConstants.Params.ERROR_MESSAGE));
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                UiUtils.hideLoadingDialog();
                NetworkUtils.onApiError(activity);
            }
        });
    }

    private void loginUserInDevice(JSONObject data, String loginBy) {
        Log.d("loginUserInDevice", "loginUserInDevice: 23"+ data.optJSONObject("dd_user_data"));
        PrefHelper.setUserLoggedIn(activity, data.optInt(APIConstants.Params.USER_ID)
                , data.optString(APIConstants.Params.TOKEN)
                , data.optString(Params.LOGIN_TOKEN)
                , loginBy
                , data.optString(APIConstants.Params.EMAIL)
                , data.optString(APIConstants.Params.NAME)
                , data.optString(APIConstants.Params.MOBILE)
                , data.optString(APIConstants.Params.DESCRIPTION)
                , data.optString(APIConstants.Params.PICTURE)
                , data.optString(APIConstants.Params.AGE)
                , data.optString(Params.USER_TYPE)
                , data.optString(APIConstants.Params.NOTIF_PUSH_STATUS)
                , data.optString(APIConstants.Params.NOTIF_PUSH_STATUS)
                , data.optString(APIConstants.Params.DEFAULT_EMAIL)
                , data.optString(APIConstants.Params.DEFAULT_MOBILE)
                , data.optJSONObject("dd_user_data"));

        /*UserSubscription userSubscription = new UserSubscription();
        userSubscription.setName("Subscribed");

        PayPerSubscription payPerSubscription = new PayPerSubscription();
        payPerSubscription.setActiveStatus(true);
        payPerSubscription.setConsumerId(122);
        ArrayList<PayPerSubscription> payPerSubscriptionArrayList = new ArrayList<>();
        payPerSubscriptionArrayList.add(payPerSubscription);
        userSubscription.setSeasonsPaySubscriptionList(payPerSubscriptionArrayList);

        PayPerSubscription payPerSubscription1 = new PayPerSubscription();
        payPerSubscription1.setActiveStatus(true);
        payPerSubscription1.setConsumerId(122);
        ArrayList<PayPerSubscription> payPerSubscriptionArrayList1 = new ArrayList<>();
        payPerSubscriptionArrayList1.add(payPerSubscription1);
        userSubscription.setVideoPaySubscriptionList(payPerSubscriptionArrayList1);

        //UserSubscription userSubscription = new Gson().fromJson(data.toString(), UserSubscription.class);
        PrefHelper.setSubscriptionStatus(activity,userSubscription);*/

        getSubscriptionStatus();
        if(!prefUtils.getStringValue(PrefKeys.REFERENCE_URL_CODE, "").equalsIgnoreCase("")) {
            UiUtils.log(TAG, "API Called Subscribe ");
            Utils.sendShareLinkUrlDetailsToServer(activity, prefUtils.getStringValue(PrefKeys.REFERENCE_URL_CODE,""), SHARE_TYPE_LOGIN, data.optInt(APIConstants.Params.USER_ID));
        }
     /*   Intent toHome = new Intent(activity, MainActivity.class);
        toHome.putExtra(MainActivity.FIRST_TIME,  true);
        toHome.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(toHome);*/
        activity.finish();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account.getIdToken());
                Log.d("onActivityResult_test", "onActivityResult: 1");
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                // ...
                Log.d("onActivityResult_test", "onActivityResult: 2");

                UiUtils.log(TAG, "onActivityResult: " + e.getStatusCode() + " " + e.getLocalizedMessage() + " " + e.getMessage());
                UiUtils.showShortToast(activity, getString(R.string.login_cancelled));
            }
        } else {
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void handleSignInResult(FirebaseUser firebaseUser) {
        try {
            String photoImg;
            try {
                photoImg = firebaseUser.getPhotoUrl().toString();
                Log.d("handleSignInResult_test", "handleSignInResult: 1");

            } catch (Exception e) {
                Log.d("handleSignInResult_test", "handleSignInResult: 2"+e.getMessage());

                UiUtils.log(TAG, "handleSignInResult: "+e.getMessage());
                UiUtils.log(TAG, Log.getStackTraceString(e));
                photoImg = "";
            }
            doSocialLoginUser(firebaseUser.getUid()
                    , firebaseUser.getEmail()
                    , firebaseUser.getDisplayName()
                    , photoImg
                    , APIConstants.Constants.GOOGLE_LOGIN);
        } catch (Exception e) {
            Log.d("handleSignInResult_test", "handleSignInResult: 3"+e.getMessage());
            UiUtils.log(TAG, "handleSignInResult: "+e.getMessage());
            UiUtils.showShortToast(activity, getString(R.string.login_cancelled));
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(activity, task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        FirebaseUser user = mAuth.getCurrentUser();
                        handleSignInResult(user);
                        Log.d("firebaseAuthWithGoogle", "firebaseAuthWithGoogle: 1"+user);
                    } else {
                        // If sign in fails, display a message to the user.
                        UiUtils.showShortToast(activity, getString(R.string.login_cancelled));
                        UiUtils.log(TAG, "onComplete: ");
                        Log.d("firebaseAuthWithGoogle", "firebaseAuthWithGoogle: 2");

                    }
                    task.addOnFailureListener(e -> UiUtils.log(TAG, "onFailure: " + e.getMessage()));
                    // ...
                });
    }

    private void handleFacebookAccessToken(AccessToken token) {
        UiUtils.log(TAG, "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(activity, task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        UiUtils.log(TAG, "signInWithCredential:success");
                        FirebaseUser user = mAuth.getCurrentUser();
                        handleSignInResult(user);
                    } else {
                        // If sign in fails, display a message to the user.
                        UiUtils.log(TAG, "signInWithCredential:failure: "+ task.getException());
                        Toast.makeText(activity, "Authentication failed.",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void checkWidth()
    {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;

        int dp = (int) (width / Resources.getSystem().getDisplayMetrics().density);

        UiUtils.log(TAG, "Width: "+ width);
        UiUtils.log(TAG, "Width dp: "+ dp);

        if(dp<400)
        {
            txtGoogle.setTextSize(16f);
            txtFacebook.setTextSize(16f);
        }
        /*if(dp>500 && dp<600)
        {
            binding.txtGoogle.setTextSize(18f);

        }else if(dp>600 && dp<700)
        {
            binding.txtGoogle.setTextSize(18f);
        }else if(dp>700 && dp<800)
        {
            binding.txtGoogle.setTextSize(18f);
        }else if(dp>800)
        {
            binding.txtGoogle.setTextSize(18f);
        }*/
    }

    private void getSubscriptionStatus()
    {
        UiUtils.log(TAG,"API Called");
        Map<String, Object> params = new HashMap<>();
        params.put(Params.ID, prefUtils.getIntValue(PrefKeys.USER_ID, -1));
        params.put(Params.TOKEN, prefUtils.getStringValue(PrefKeys.SESSION_TOKEN, ""));
        params.put(Params.IP, CURRENT_IP);

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
                NetworkUtils.onApiError(getActivity());
            }
        });
    }
}
