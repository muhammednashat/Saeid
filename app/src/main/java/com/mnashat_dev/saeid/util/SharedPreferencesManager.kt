package com.mnashat_dev.saeid.util

import android.content.Context
import android.content.SharedPreferences

class SharedPreferencesManager(context: Context) {
    private val PREFS_NAME = "MyAppPrefs"
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun storeString(key: String, value: String) {
        val editor = sharedPreferences.edit()
        editor.putString(key, value)
        editor.apply()
    }

    fun getString(key: String, defaultValue: String = ""): String {
        return sharedPreferences.getString(key, "") ?: defaultValue
    }

    fun storeBoolean(key: String, value: Boolean) {
        val editor = sharedPreferences.edit()
        editor.putBoolean(key, value)
        editor.apply()
    }

    fun getBoolean(key: String): Boolean {
        return sharedPreferences.getBoolean(key, false)
    }


    fun clearData() {
        val editor = sharedPreferences.edit()
        editor.clear()
        editor.apply()
    }

}
