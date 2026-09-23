package com.kglabs28.netkut.util

import android.content.Context
import android.content.SharedPreferences

object PreferenceUtils {
    private fun getPrefs(context: Context): SharedPreferences {
        return context.getSharedPreferences(AppConstants.PREFS_NAME, Context.MODE_PRIVATE)
    }

    fun getBoolean(context: Context, key: String, defaultValue: Boolean = false): Boolean {
        return getPrefs(context).getBoolean(key, defaultValue)
    }

    fun setBoolean(context: Context, key: String, value: Boolean) {
        getPrefs(context).edit().putBoolean(key, value).apply()
    }

    fun getInt(context: Context, key: String, defaultValue: Int = 0): Int {
        return getPrefs(context).getInt(key, defaultValue)
    }

    fun setInt(context: Context, key: String, value: Int) {
        getPrefs(context).edit().putInt(key, value).apply()
    }

    fun getLong(context: Context, key: String, defaultValue: Long = 0L): Long {
        return getPrefs(context).getLong(key, defaultValue)
    }

    fun setLong(context: Context, key: String, value: Long) {
        getPrefs(context).edit().putLong(key, value).apply()
    }

    fun getString(context: Context, key: String, defaultValue: String = ""): String {
        return getPrefs(context).getString(key, defaultValue) ?: defaultValue
    }

    fun setString(context: Context, key: String, value: String) {
        getPrefs(context).edit().putString(key, value).apply()
    }
}
