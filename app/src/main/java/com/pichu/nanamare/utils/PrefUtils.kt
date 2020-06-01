package com.pichu.nanamare.utils

import android.content.Context
import androidx.preference.PreferenceManager
import com.securepreferences.SecurePreferences

class PrefUtils(context: Context) {

    private val sharedPref = PreferenceManager.getDefaultSharedPreferences(context)
    private val secureSharedPref = SecurePreferences(context)

    private fun getEdit() = sharedPref.edit()

    private fun getSecureEdit() = secureSharedPref.edit()

    companion object {
        const val PREF_KEY_ACCESS_TOKEN = "PREF_KEY_ACCESS_TOKEN"
        const val PREF_KEY_REFRESH_TOKEN = "PREF_KEY_REFRESH_TOKEN"
        const val PREF_IS_FIRST_OPEN = "PREF_IS_FIRST_OPEN"
    }

    fun saveAccessToken(token: String) {
        getSecureEdit().putString(PREF_KEY_ACCESS_TOKEN, token).apply()
    }

    fun loadAccessToken(): String {
        return secureSharedPref.getString(PREF_KEY_ACCESS_TOKEN, "") ?: ""
    }

    fun saveRefreshToken(token: String) {
        getSecureEdit().putString(PREF_KEY_REFRESH_TOKEN, token).apply()
    }

    fun loadRefreshToken(): String {
        return secureSharedPref.getString(PREF_KEY_REFRESH_TOKEN, "") ?: ""
    }

    fun saveIsFirstTime(first: Boolean) {
        getEdit().putBoolean(PREF_IS_FIRST_OPEN, first).apply()
    }

    fun getIsFirstTime(): Boolean {
        return sharedPref.getBoolean(PREF_IS_FIRST_OPEN, true)
    }


}