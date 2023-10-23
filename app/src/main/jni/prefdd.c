//
// Created by home on 11/22/2021.
//
#include <jni.h>
//
JNIEXPORT jstring JNICALL Java_com_dd_app_dd_utils_sharedPreference_PrefKeysDD_00024Companion_getUserIsLoggedInKeys(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "isLoggedIn");
}
JNIEXPORT jstring JNICALL Java_com_dd_app_dd_utils_sharedPreference_PrefKeysDD_00024Companion_getTokenKeys(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "SESSION_TOKEN");
}
JNIEXPORT jstring JNICALL Java_com_dd_app_dd_utils_sharedPreference_PrefKeysDD_00024Companion_getLoginTokenKeys(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "login_token");
}
JNIEXPORT jstring JNICALL Java_com_dd_app_dd_utils_sharedPreference_PrefKeysDD_00024Companion_getUserIdKeys(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "USER_ID");
}
JNIEXPORT jstring JNICALL Java_com_dd_app_dd_utils_sharedPreference_PrefKeysDD_00024Companion_getOttIdKeys(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "OTT_ID");
}

JNIEXPORT jstring JNICALL Java_com_dd_app_dd_utils_sharedPreference_PrefKeysDD_00024Companion_getUserEmailKeys(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "USER_EMAIL");
}
JNIEXPORT jstring JNICALL Java_com_dd_app_dd_utils_sharedPreference_PrefKeysDD_00024Companion_getUserNameKeys(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "FIRST_NAME");
}
JNIEXPORT jstring JNICALL Java_com_dd_app_dd_utils_sharedPreference_PrefKeysDD_00024Companion_getUserPictureKeys(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "USER_PICTURE");
}
JNIEXPORT jstring JNICALL Java_com_dd_app_dd_utils_sharedPreference_PrefKeysDD_00024Companion_getUserAboutKeys(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "about");
}
JNIEXPORT jstring JNICALL Java_com_dd_app_dd_utils_sharedPreference_PrefKeysDD_00024Companion_getUserAgeKeys(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "age");
}
JNIEXPORT jstring JNICALL Java_com_dd_app_dd_utils_sharedPreference_PrefKeysDD_00024Companion_getUserTypeKeys(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "user_type");
}
JNIEXPORT jstring JNICALL Java_com_dd_app_dd_utils_sharedPreference_PrefKeysDD_00024Companion_getLoginTypeKeys(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "LOGIN_TYPE");
}
JNIEXPORT jstring JNICALL Java_com_dd_app_dd_utils_sharedPreference_PrefKeysDD_00024Companion_getIsPassKeys(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "is_pass");
}
JNIEXPORT jstring JNICALL Java_com_dd_app_dd_utils_sharedPreference_PrefKeysDD_00024Companion_getPassCodeKeys(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "pass_code");
}
JNIEXPORT jstring JNICALL Java_com_dd_app_dd_utils_sharedPreference_PrefKeysDD_00024Companion_getServerAppVersionKeys(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "version");
}
JNIEXPORT jstring JNICALL Java_com_dd_app_dd_utils_sharedPreference_PrefKeysDD_00024Companion_getUpdateAppCompulsoryKeys(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "is_compulsory");
}
JNIEXPORT jstring JNICALL Java_com_dd_app_dd_utils_sharedPreference_PrefKeysDD_00024Companion_getPushNotiKeys(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "push_notifications");
}
JNIEXPORT jstring JNICALL Java_com_dd_app_dd_utils_sharedPreference_PrefKeysDD_00024Companion_getEmailNotiKeys(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "email_notifications");
}
JNIEXPORT jstring JNICALL Java_com_dd_app_dd_utils_sharedPreference_PrefKeysDD_00024Companion_getPaymentCurrencyKeys(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "payment_currency");
}
JNIEXPORT jstring JNICALL Java_com_dd_app_dd_utils_sharedPreference_PrefKeysDD_00024Companion_getUserMobileKeys(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "USER_MOBILE");
}
JNIEXPORT jstring JNICALL Java_com_dd_app_dd_utils_sharedPreference_PrefKeysDD_00024Companion_getUserCoinsKeys(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "USER_COINS");
}

JNIEXPORT jstring JNICALL Java_com_dd_app_dd_utils_sharedPreference_PrefKeysDD_00024Companion_getActiveSubProfileKeys(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "activeSubProfile");
}
JNIEXPORT jstring JNICALL Java_com_dd_app_dd_utils_sharedPreference_PrefKeysDD_00024Companion_getDeleteVideoDownloadKeys(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "deleteVideosDownload");
}
JNIEXPORT jstring JNICALL Java_com_dd_app_dd_utils_sharedPreference_PrefKeysDD_00024Companion_getCancelVideoDownloadKeys(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "cancelVideosDownload");
}
JNIEXPORT jstring JNICALL Java_com_dd_app_dd_utils_sharedPreference_PrefKeysDD_00024Companion_getPauseVideoDownloadKeys(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "pauseVideosDownload");
}
JNIEXPORT jstring JNICALL Java_com_dd_app_dd_utils_sharedPreference_PrefKeysDD_00024Companion_getFCMTokenKeys(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "fcm_token");
}
JNIEXPORT jstring JNICALL Java_com_dd_app_dd_utils_sharedPreference_PrefKeysDD_00024Companion_getAppLanguageStringKeys(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "applanguagestr");
}
JNIEXPORT jstring JNICALL Java_com_dd_app_dd_utils_sharedPreference_PrefKeysDD_00024Companion_getAppLanguageKeys(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "applanguage");
}
JNIEXPORT jstring JNICALL Java_com_dd_app_dd_utils_sharedPreference_PrefKeysDD_00024Companion_getPIPModeOnMinimizeKeys(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "pipmodeOnMinimize");
}
JNIEXPORT jstring JNICALL Java_com_dd_app_dd_utils_sharedPreference_PrefKeysDD_00024Companion_getFirstTimeLoginKeys(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "first_time_login");
}
JNIEXPORT jstring JNICALL Java_com_dd_app_dd_utils_sharedPreference_PrefKeysDD_00024Companion_getTermsAndConditionKeys(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "terms_and_condition");
}

JNIEXPORT jstring JNICALL Java_com_dd_app_dd_utils_sharedPreference_PrefKeysDD_00024Companion_getOrderIdKeys(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "order_id");
}
JNIEXPORT jstring JNICALL Java_com_dd_app_dd_utils_sharedPreference_PrefKeysDD_00024Companion_getPlanIdKeys(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "plan_id");
}
JNIEXPORT jstring JNICALL Java_com_dd_app_dd_utils_sharedPreference_PrefKeysDD_00024Companion_getTempIdKeys(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "temp_id");
}

JNIEXPORT jstring JNICALL Java_com_dd_app_dd_utils_sharedPreference_PrefKeysDD_00024Companion_getVideoAgeVerifyKeys(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "video_age_verification");
}
JNIEXPORT jstring JNICALL Java_com_dd_app_dd_utils_sharedPreference_PrefKeysDD_00024Companion_getSubscriptionDaysKeys(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "subscription_days");
}
JNIEXPORT jstring JNICALL Java_com_dd_app_dd_utils_sharedPreference_PrefKeysDD_00024Companion_getIpAddressKeys(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "ip");
}

JNIEXPORT jstring JNICALL Java_com_dd_app_dd_utils_sharedPreference_PrefKeysDD_00024Companion_getRefUrlKeys(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "share_url_code");
}
JNIEXPORT jstring JNICALL Java_com_dd_app_dd_utils_sharedPreference_PrefKeysDD_00024Companion_getRefInstallKeys(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "share_url_install");
}
JNIEXPORT jstring JNICALL Java_com_dd_app_dd_utils_sharedPreference_PrefKeysDD_00024Companion_getRefIdKeys(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "share_url_id");
}

JNIEXPORT jstring JNICALL Java_com_dd_app_dd_utils_sharedPreference_PrefKeysDD_00024Companion_getUserSubscriptionKeys(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "user_subscription");
}