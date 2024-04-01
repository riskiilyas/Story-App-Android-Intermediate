package com.riskee.livestorybyriski.utils

import android.content.Context
import android.content.SharedPreferences

class SharedPrefManager(context: Context) {

    companion object {
        private const val PREF_NAME = "MyAppPrefs"
        private const val KEY_TOKEN = "token"
    }

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    var token: String?
        get() = sharedPreferences.getString(KEY_TOKEN, null)
        set(value) {
            sharedPreferences.edit().putString(KEY_TOKEN, value).apply()
        }
}
