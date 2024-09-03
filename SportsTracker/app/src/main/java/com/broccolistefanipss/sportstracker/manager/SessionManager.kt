package com.broccolistefanipss.sportstracker.manager

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
            editor.apply()
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

    companion object {
        private val Tag = SessionManager::class.java.simpleName
        private const val PrefName = "AndroidLoginPref"
        const val KeyUserName = "user_name"
        private const val KeyIsLoggedIn = "IsLoggedIn"
    }
}
