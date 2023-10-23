package com.dd.app.dd.utils.sharedPreference

import android.content.Context
import com.dd.app.dd.model.LoginUser


class PrefKeysDD() {

    companion object {

        init {
            System.loadLibrary("keys")
        }

        private external fun getUserIsLoggedInKeys(): String
        private external fun getTokenKeys(): String
        private external fun getLoginTokenKeys(): String
        private external fun getUserIdKeys(): String
        private external fun getOttIdKeys(): String
        private external fun getUserEmailKeys(): String
        private external fun getUserNameKeys(): String
        private external fun getUserPictureKeys(): String
        private external fun getUserAboutKeys(): String
        private external fun getUserAgeKeys(): String
        private external fun getUserTypeKeys(): String
        private external fun getLoginTypeKeys(): String
        private external fun getIsPassKeys(): String
        private external fun getPassCodeKeys(): String
        private external fun getServerAppVersionKeys(): String
        private external fun getUpdateAppCompulsoryKeys(): String
        private external fun getPushNotiKeys(): String
        private external fun getEmailNotiKeys(): String
        private external fun getPaymentCurrencyKeys(): String
        private external fun getUserMobileKeys(): String
        private external fun getUserCoinsKeys(): String
        private external fun getActiveSubProfileKeys(): String
        private external fun getDeleteVideoDownloadKeys(): String
        private external fun getCancelVideoDownloadKeys(): String
        private external fun getPauseVideoDownloadKeys(): String
        private external fun getFCMTokenKeys(): String
        private external fun getAppLanguageStringKeys(): String
        private external fun getAppLanguageKeys(): String
        private external fun getPIPModeOnMinimizeKeys(): String
        private external fun getFirstTimeLoginKeys(): String
        private external fun getTermsAndConditionKeys(): String

        private external fun getOrderIdKeys(): String
        private external fun getPlanIdKeys(): String
        private external fun getTempIdKeys(): String

        private external fun getVideoAgeVerifyKeys(): String
        private external fun getSubscriptionDaysKeys(): String
        private external fun getIpAddressKeys(): String

        private external fun getRefUrlKeys(): String
        private external fun getRefInstallKeys(): String
        private external fun getRefIdKeys(): String

        private external fun getUserSubscriptionKeys(): String

        ////
        val IS_LOGGED_IN = getUserIsLoggedInKeys()
        val SESSION_TOKEN = getTokenKeys()
        val SESSION_NEW_TOKEN = getLoginTokenKeys()
        val USER_ID = getUserIdKeys()
        val OTT_ID = getOttIdKeys()
        val USER_EMAIL = getUserEmailKeys()
        val USER_NAME = getUserNameKeys()
        val USER_PICTURE = getUserPictureKeys()
        val USER_ABOUT = getUserAboutKeys()
        val USER_AGE = getUserAgeKeys()
        val USER_TYPE = getUserTypeKeys()
        val LOGIN_TYPE = getLoginTypeKeys()
        val IS_PASS = getIsPassKeys()
        val PASS_CODE = getPassCodeKeys()
        val SERVER_APP_VERSION = getServerAppVersionKeys()
        val UPDATE_APP_COMPULSORY = getUpdateAppCompulsoryKeys()
        val PUSH_NOTIFICATIONS = getPushNotiKeys()
        val EMAIL_NOTIFICATIONS = getEmailNotiKeys()
        val PAYMENT_CURRENCY = getPaymentCurrencyKeys()
        val USER_MOBILE = getUserMobileKeys()
        val USER_COINS = getUserCoinsKeys()
        val ACTIVE_SUB_PROFILE = getActiveSubProfileKeys()
        val DELETE_VIDEOS_DOWNLOAD = getDeleteVideoDownloadKeys()
        val CANCEL_VIDEOS_DOWNLOAD = getCancelVideoDownloadKeys()
        val PAUSE_VIDEOS_DOWNLOAD = getPauseVideoDownloadKeys()
        val FCM_TOKEN = getFCMTokenKeys()
        val APP_LANGUAGE_STRING = getAppLanguageStringKeys()
        val APP_LANGUAGE = getAppLanguageKeys()
        val PIP_ENTER_WHEN_MINIMIZED = getPIPModeOnMinimizeKeys()
        val FIRST_TIME_LOGIN = getFirstTimeLoginKeys()
        val TERMS_AND_CONDITION = getTermsAndConditionKeys()

        val ORDER_ID = getOrderIdKeys()
        val PLAN_ID = getPlanIdKeys()
        val TEMP_ID = getTempIdKeys()

        val VIDEO_AGE_VERIFICATION = getVideoAgeVerifyKeys()
        val SUBSCRIPTION_DAYS = getSubscriptionDaysKeys()
        val IP_ADDRESS = getIpAddressKeys()

        val REFERENCE_URL_CODE = getRefUrlKeys()
        val REFERENCE_INSTALL = getRefInstallKeys()
        val REFERENCE_URL_ID = getRefIdKeys()

        val USER_SUBSCRIPTION = getUserSubscriptionKeys()

        fun logoutUser(context: Context){

            val sharedPreferenceHelper = SharedPreferenceHelper(context)
            sharedPreferenceHelper.setValue(SESSION_TOKEN, "").toString()
            sharedPreferenceHelper.setValue(LOGIN_TYPE, "").toString()
            LoginUser.apply {
                authToken = ""
                role = ""
            }
        }

    }






}