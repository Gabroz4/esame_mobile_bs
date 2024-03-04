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
            editor.commit()
        }

    fun setLogin(isLoggedIn: Boolean) {
        editor.putBoolean(KeyIsLoggedIn, isLoggedIn)
        editor.commit() // non blocca main thread
        Log.d(Tag, "Sessione modificata")
    }

    companion object {
        private val Tag = SessionManager::class.java.simpleName
        private val PrefName = "AndroidLoginPref"
        const val KeyUserName = "user_name"
        private val KeyIsLoggedIn = "IsLoggedIn"
    }
}
