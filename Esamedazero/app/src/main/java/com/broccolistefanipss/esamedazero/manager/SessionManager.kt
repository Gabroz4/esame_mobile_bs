package com.broccolistefanipss.esamedazero.manager

import android.content.Context
import android.content.SharedPreferences
import android.content.SharedPreferences.Editor
import android.util.Log

class SessionManager(private var _context:Context) {
    private var pref:SharedPreferences = _context.getSharedPreferences(PrefName, Context.MODE_PRIVATE)
    private var editor: SharedPreferences.Editor = pref.edit()

    internal var PRIVATE_MODE = 0
    val isLoggedIn: Boolean
        get() = pref.getBoolean(KeyUserName, false)

    fun setLogin(isLoggedIn: Boolean) {
        editor.putBoolean(KeyIsLoggedIn, isLoggedIn)
        //applica cambiamenti

        editor.commit()

        Log.d(Tag, "Sessione modificata")
    }
    companion object{
        private val Tag = SessionManager::class.java.simpleName
        //shared preferences file name
        private val PrefName = "Login"
        var KeyUserName = "user_name"
        private val KeyIsLoggedIn = "IsLoggedIn"
    }
}