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

    fun saveProfileImageUri(uri: Uri?) {
        editor.putString(KeyProfileImageUri, uri?.toString())
        editor.apply()
    }

    // metodo per recuperare l'URI dell'immagine del profilo
    fun getProfileImageUri(): Uri? {
        val uriString = pref.getString(KeyProfileImageUri, null)
        return uriString?.let { Uri.parse(it) }
    }

    companion object {
        const val PrefName = "AndroidLoginPref" //TODO: rimetti private dopo test
        const val KeyUserName = "user_name"
        const val KeyProfileImageUri = "profile_image_uri"
        const val KeyIsLoggedIn = "IsLoggedIn"
    }
}

