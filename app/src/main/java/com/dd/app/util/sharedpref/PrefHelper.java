package com.dd.app.util.sharedpref;

import static com.dd.app.util.sharedpref.PrefKeys.USER_SUBSCRIPTION;

import android.content.Context;
import android.util.Log;

import com.dd.app.dd.model.LoginUser;
import com.dd.app.dd.network.UserLoginDetails;
import com.dd.app.model.UserSubscription;
import com.dd.app.util.UiUtils;
import com.google.gson.Gson;

import org.json.JSONObject;


public class PrefHelper {

    public static void setUserLoggedIn(Context context, int id, String token, String loginToken,
                                       String loginType, String email, String name, String mobile,
                                       String about, String picture, String age, String userType,
                                       String pushNotifyStatus, String emailNotifyStatus,
                                       String defaultEmail, String defaultMobile, JSONObject dd_data) {
        PrefUtils preferences = PrefUtils.getInstance(context);
        preferences.setValue(PrefKeys.IS_LOGGED_IN, true);
        preferences.setValue(PrefKeys.USER_ID, id);
        preferences.setValue(PrefKeys.SESSION_TOKEN, token);
        preferences.setValue(PrefKeys.SESSION_NEW_TOKEN, loginToken);
        preferences.setValue(PrefKeys.LOGIN_TYPE, loginType);
        preferences.setValue(PrefKeys.USER_EMAIL, email);
        preferences.setValue(PrefKeys.USER_NAME, name);
        preferences.setValue(PrefKeys.USER_MOBILE, mobile);
        preferences.setValue(PrefKeys.USER_ABOUT, about);
        preferences.setValue(PrefKeys.USER_PICTURE, picture);
        preferences.setValue(PrefKeys.USER_AGE, age);
        preferences.setValue(PrefKeys.USER_TYPE, userType.equals("1"));
        preferences.setValue(PrefKeys.ACTIVE_SUB_PROFILE, 0);
        preferences.setValue(PrefKeys.PUSH_NOTIFICATIONS, pushNotifyStatus.equals("1"));
        preferences.setValue(PrefKeys.EMAIL_NOTIFICATIONS, emailNotifyStatus.equals("1"));
        preferences.setValue(PrefKeys.TERMS_AND_CONDITION, false);
        preferences.removeKey(PrefKeys.IS_PASS);
        preferences.removeKey(PrefKeys.PASS_CODE);
        preferences.setValue(PrefKeys.DEFAULT_EMAIL, defaultEmail);
        preferences.setValue(PrefKeys.DEFAULT_MOBILE, defaultMobile);

        setDDLoggedIn(context, preferences, dd_data);
    }

    public static void setDDLoggedIn(Context context,PrefUtils preferences, JSONObject dd_data) {

        try {
            UserLoginDetails.setUserLoggedIn(
                    context,
                    dd_data.optString("user_id"),
                    dd_data.optString("ott_id"),
                    dd_data.optString("auth_token"),
                    dd_data.optString("role"),
                    dd_data.optString("user_email"),
                    dd_data.optString("user_name"),
                    "",
                    "",
                    "",
                    "",
                    "",
                    dd_data.optInt("coins")
            );

            LoginUser.INSTANCE.setAuthToken(dd_data.optString("auth_token"));
            LoginUser.INSTANCE.setRole(dd_data.optString("role"));

            LoginUser.INSTANCE.setUserId(dd_data.optString("user_id"));
            LoginUser.INSTANCE.setOttId(dd_data.optString("ott_id"));
            LoginUser.INSTANCE.setEmail(dd_data.optString("user_email"));
            LoginUser.INSTANCE.setName(dd_data.optString("user_name"));
            LoginUser.INSTANCE.setMobile(dd_data.optString(""));
            LoginUser.INSTANCE.setProfilePhoto(dd_data.optString(""));
            LoginUser.INSTANCE.setCoins(dd_data.optInt("coins"));

        }catch (Exception e){
            UiUtils.log("DDPref", Log.getStackTraceString(e));
        }

    }

    public static void setUserLoggedOut(Context context) {
        PrefUtils preferences = PrefUtils.getInstance(context);
        preferences.removeKey(PrefKeys.IS_LOGGED_IN);
        preferences.removeKey(PrefKeys.USER_ID);
        preferences.removeKey(PrefKeys.SESSION_TOKEN);
        preferences.removeKey(PrefKeys.SESSION_NEW_TOKEN);
        preferences.removeKey(PrefKeys.LOGIN_TYPE);
        preferences.removeKey(PrefKeys.USER_EMAIL);
        preferences.removeKey(PrefKeys.USER_NAME);
        preferences.removeKey(PrefKeys.USER_MOBILE);
        preferences.removeKey(PrefKeys.USER_ABOUT);
        preferences.removeKey(PrefKeys.USER_PICTURE);
        preferences.removeKey(PrefKeys.USER_AGE);
        preferences.removeKey(PrefKeys.USER_TYPE);
        preferences.removeKey(PrefKeys.ACTIVE_SUB_PROFILE);
        preferences.removeKey(PrefKeys.PUSH_NOTIFICATIONS);
        preferences.removeKey(PrefKeys.EMAIL_NOTIFICATIONS);
        preferences.removeKey(PrefKeys.TERMS_AND_CONDITION);
        preferences.removeKey(PrefKeys.IS_PASS);
        preferences.removeKey(PrefKeys.PASS_CODE);
        preferences.removeKey(PrefKeys.DEFAULT_EMAIL);
        preferences.removeKey(PrefKeys.DEFAULT_MOBILE);

        preferences.removeKey(PrefKeys.USER_SUBSCRIPTION);

        //DD
        preferences.removeKey(PrefKeys.DD_EMAIL);
        preferences.removeKey(PrefKeys.DD_ROLE);

        UserLoginDetails.setUserLoggedOut(context);
    }


    public static void setSubscriptionStatus(Context context, UserSubscription userSubscription)
    {
        PrefUtils preferences = PrefUtils.getInstance(context);
        //Set
        String json = new Gson().toJson(userSubscription);
        preferences.setValue(USER_SUBSCRIPTION, json);

        /*//Retrieve
        String json = appSharedPrefs.getString(USER_SUBSCRIPTION, "");
        UserSubscription obj = new Gson().fromJson(json, UserSubscription.class);*/
    }

    public static void setOrderDetailForConfirmation(Context context, int userId, String orderId, int planId)
    {
        PrefUtils preferences = PrefUtils.getInstance(context);
        preferences.setValue(PrefKeys.TEMP_ID, userId);
        preferences.setValue(PrefKeys.ORDER_ID, orderId);
        preferences.setValue(PrefKeys.PLAN_ID, planId);
    }
}
