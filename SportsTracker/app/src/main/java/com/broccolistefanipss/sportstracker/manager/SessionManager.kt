package com.broccolistefanipss.sportstracker.manager

import android.content.Context
import android.content.SharedPreferences
import android.net.Uri

class SessionManager(context: Context) {
    private var pref: SharedPreferences = context.getSharedPreferences(PrefName, Context.MODE_PRIVATE)
    private var editor: SharedPreferences.Editor = pref.edit()

    val isLoggedIn: Boolean
        get() = pref.getBoolean(KeyIsLoggedIn, false)

    var userName: String?
        get() = pref.getString(KeyUserName, null)
        set(value) {
            editor.putString(KeyUserName, value)
            editor.apply()
        }

    fun setLogin(isLoggedIn: Boolean) {
        editor.putBoolean(KeyIsLoggedIn, isLoggedIn)
        editor.apply()
    }

    fun saveProfileImageUri(uri: Uri?, userName: String?) {
        userName?.let {
            editor.putString("profile_image_uri_$it", uri?.toString())
            editor.apply()
        }
    }

    fun getProfileImageUri(userName: String?): Uri? {
        return userName?.let { it ->
            val uriString = pref.getString("profile_image_uri_$it", null)
            uriString?.let { Uri.parse(it) }
        }
    }

    companion object {
        private const val PrefName = "AndroidLoginPref"
        private const val KeyUserName = "user_name"
        private const val KeyIsLoggedIn = "IsLoggedIn"
    }
}

