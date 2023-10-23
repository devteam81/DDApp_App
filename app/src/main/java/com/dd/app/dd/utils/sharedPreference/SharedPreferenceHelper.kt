package com.dd.app.dd.utils.sharedPreference

import android.content.Context

class SharedPreferenceHelper(val context: Context) {


    private var mSharedPreferenceHelper: SharedPreferenceHelper? = null
    val mSharedPreferences = context.getSharedPreferences("DDPreferences", android.content.Context.MODE_PRIVATE)
    val mSharedPreferencesEditor = mSharedPreferences.edit()

    fun setValue(key: String?, value: String?) {
        synchronized(this) {
            mSharedPreferencesEditor.putString(key, value)
            mSharedPreferencesEditor.commit()
        }
    }

    fun setValue(key: String?, value: Int) {
        synchronized(this) {
            mSharedPreferencesEditor.putInt(key, value)
            mSharedPreferencesEditor.commit()
        }
    }

    fun setValue(key: String?, value: Double) {
        synchronized(this) {
            setValue(key, value.toString()) }
    }

    fun setValue(key: String?, value: Long) {
        synchronized(this) {
            mSharedPreferencesEditor.putLong(key, value)
            mSharedPreferencesEditor.commit()
        }
    }

    fun setValue(key: String?, value: Boolean) {
        synchronized(this) {
            mSharedPreferencesEditor.putBoolean(key, value)
            mSharedPreferencesEditor.commit()
        }
    }

    fun getStringValue(key: String?, defaultValue: String?): String? {
        synchronized(this) { return mSharedPreferences.getString(key, defaultValue) }
    }

    fun getIntValue(key: String?, defaultValue: Int): Int {
        synchronized(this) { return mSharedPreferences.getInt(key, defaultValue) }
    }

    fun getFloatValue(key: String?, defaultValue: Float): Float {
        synchronized(this) { return mSharedPreferences.getFloat(key, defaultValue) }
    }

    fun getLongValue(key: String?, defaultValue: Long): Long {
        synchronized(this) { return mSharedPreferences.getLong(key, defaultValue) }
    }

    fun getBoolanValue(keyFlag: String?, defaultValue: Boolean): Boolean {
        synchronized(this) { return mSharedPreferences.getBoolean(keyFlag, defaultValue) }
    }


    fun removeKey(key: String?) {
        synchronized(this) {
            if (mSharedPreferencesEditor != null) {
                mSharedPreferencesEditor.remove(key)
                mSharedPreferencesEditor.commit()
            }
        }
    }

    fun clear() {
        synchronized(this) { mSharedPreferencesEditor.clear().commit() }
    }

    @Synchronized
    fun getInstance(context: Context): SharedPreferenceHelper? {
        if (mSharedPreferenceHelper == null) {
            mSharedPreferenceHelper = SharedPreferenceHelper(context.applicationContext)
        }
        return mSharedPreferenceHelper
    }
}