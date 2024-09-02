package com.broccolistefanipss.esamedazero.manager

import android.content.Context
import android.content.SharedPreferences
import android.util.Log

class SessionManager(context: Context) {
    private var pref: SharedPreferences = context.getSharedPreferences(PrefName, Context.MODE_PRIVATE)
    private var editor: SharedPreferences.Editor = pref.edit()

    val isLoggedIn: Boolean
        get() = pref.getBoolean(KeyIsLoggedIn, false)

    var userName: String?
        get() = pref.getString(KeyUserName, null)
        set(value) {
            editor.putString(KeyUserName, value)
            editor.apply()  // Modificato da commit() a apply()
        }

    var profileImagePath: String?
        get() = pref.getString("profile_image_path", null)
        set(value) {
            editor.putString("profile_image_path", value).apply()
        }

    fun createLoginSession(username: String) {
        this.userName = username
        setLogin(true)
    }

    fun setLogin(isLoggedIn: Boolean) {
        editor.putBoolean(KeyIsLoggedIn, isLoggedIn)
        editor.apply()  // Modificato da commit() a apply()
        Log.d(Tag, "Sessione modificata")
    }

    fun clearSession() {
        editor.clear()
        editor.apply()  // Cancella tutti i dati della sessione
        Log.d(Tag, "Sessione cancellata")
    }

    companion object {
        private val Tag = SessionManager::class.java.simpleName
        private val PrefName = "AndroidLoginPref"
        const val KeyUserName = "user_name"
        private val KeyIsLoggedIn = "IsLoggedIn"
    }
}
