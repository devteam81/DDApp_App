package com.dd.app.dd.network

import android.content.Context
import android.content.Intent
import com.dd.app.dd.model.LoginUser
import com.dd.app.dd.utils.sharedPreference.PrefKeysDD
import com.dd.app.dd.utils.sharedPreference.SharedPreferenceHelper

class UserLoginDetails {

    companion object{

        @JvmStatic
        fun setUserLoggedIn(
            context: Context,
            id: String,
            ottId: String,
            token: String?,
            loginType: String?,
            email: String?,
            firstName: String?,
            middleName: String?,
            lastName: String?,
            mobile: String?,
            dob: String?,
            picture: String?,
            coins: Int
        ) {
            val preferences: SharedPreferenceHelper = SharedPreferenceHelper(context)
            //preferences.setValue("IS_LOGGED_IN", true)
            preferences.setValue(PrefKeysDD.USER_ID, id)
            preferences.setValue(PrefKeysDD.OTT_ID, ottId)
            preferences.setValue(PrefKeysDD.SESSION_TOKEN, token)
            preferences.setValue(PrefKeysDD.LOGIN_TYPE, loginType)
            preferences.setValue(PrefKeysDD.USER_EMAIL, email)
            preferences.setValue(PrefKeysDD.USER_MOBILE, mobile)
            preferences.setValue(PrefKeysDD.USER_COINS, coins)
            /*preferences.setValue("FIRST_NAME", firstName)
            preferences.setValue("MIDDLE_NAME", middleName)
            preferences.setValue("LAST_NAME", lastName)
            preferences.setValue("USER_DOB", dob)
            preferences.setValue("USER_PICTURE", picture)
            preferences.setValue("TERMS_AND_CONDITION", false)*/
        }

        @JvmStatic
        fun setUserLoggedOut(context: Context) {
            val preferences: SharedPreferenceHelper = SharedPreferenceHelper(context)
            //preferences.removeKey("IS_LOGGED_IN")
            preferences.removeKey(PrefKeysDD.USER_ID)
            preferences.removeKey(PrefKeysDD.OTT_ID)
            preferences.removeKey(PrefKeysDD.SESSION_TOKEN)
            preferences.removeKey(PrefKeysDD.LOGIN_TYPE)
            preferences.removeKey(PrefKeysDD.USER_EMAIL)
            preferences.removeKey("FIRS_NAME")
            preferences.removeKey("MIDDLE_NAME")
            preferences.removeKey("LAST_NAME")
            preferences.removeKey(PrefKeysDD.USER_MOBILE)
            preferences.removeKey("USER_DOB")
            preferences.removeKey("USER_PICTURE")
            preferences.removeKey("TERMS_AND_CONDITION")
            preferences.removeKey(PrefKeysDD.USER_COINS)

            LoginUser.apply {
                authToken = ""
                role = ""

                userId = ""
                ottId = ""
                name = ""
                email = ""
                mobile = ""
                profilePhoto = ""
                coins = 0
            }

        }
    }

}