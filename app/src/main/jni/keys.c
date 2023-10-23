//
// Created by home on 11/22/2021.
//
#include <jni.h>

//PAYTM DETAILS
//TEST Details
JNIEXPORT jstring JNICALL Java_com_dd_app_network_APIConstants_getTestMIDKeys(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "tWwvJq40191917862733");
}
JNIEXPORT jstring JNICALL Java_com_dd_app_network_APIConstants_getTestChannelIdKeys(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "WAP");
}
JNIEXPORT jstring JNICALL Java_com_dd_app_network_APIConstants_getTestWebsiteKeys(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "WEBSTAGING");
}
JNIEXPORT jstring JNICALL Java_com_dd_app_network_APIConstants_getTestIndustryTypeIdKeys(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "Retail");
}

//LIVE Details
JNIEXPORT jstring JNICALL Java_com_dd_app_network_APIConstants_getLiveMIDKeys(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "BEBUPr51888304386999");
}
JNIEXPORT jstring JNICALL Java_com_dd_app_network_APIConstants_getLiveChannelIdKeys(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "WAP");
}
JNIEXPORT jstring JNICALL Java_com_dd_app_network_APIConstants_getLiveWebsiteKeys(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "DEFAULT");
}
JNIEXPORT jstring JNICALL Java_com_dd_app_network_APIConstants_getLiveIndustryTypeIdKeys(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "ECommerce");
}

//Paytm UPI Details
JNIEXPORT jstring JNICALL Java_com_dd_app_network_APIConstants_getPayeeNameKeys(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "BEBU");
}
JNIEXPORT jstring JNICALL Java_com_dd_app_network_APIConstants_getPayeeVpaKeys(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "paytm-66384265@paytm");
}
JNIEXPORT jstring JNICALL Java_com_dd_app_network_APIConstants_getMerchantCodeKeys(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "5815");
}

//Paytm TYPES
JNIEXPORT jstring JNICALL Java_com_dd_app_network_APIConstants_getTypePaytmKeys(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "PAYTM");
}
JNIEXPORT jstring JNICALL Java_com_dd_app_network_APIConstants_getTypeCardKeys(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "CARD_NET_BANK");
}
JNIEXPORT jstring JNICALL Java_com_dd_app_network_APIConstants_getTypeUpiKeys(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "UPI");
}
JNIEXPORT jstring JNICALL Java_com_dd_app_network_APIConstants_getTypeAgreePayKeys(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "AGREEPAY");
}
JNIEXPORT jstring JNICALL Java_com_dd_app_network_APIConstants_getTypeGoogleInAppKeys(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "GOOGLE_IN_APP");
}
JNIEXPORT jstring JNICALL Java_com_dd_app_network_APIConstants_getTypePayTenKeys(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "PAY_TEN");
}