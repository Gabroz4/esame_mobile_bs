package com.broccolistefanipss.esamedazero.manager

import android.content.Context
import android.content.SharedPreferences

public class SharedPrefs {
    private val Tag = SharedPrefs::class.java.simpleName

    companion object {
        var userName = "userName"
        var sesso = "sesso"
        var eta = "eta"
        var altezza = "altezza"
        var peso = "peso"
        var obiettivo = "obiettivo"

        private val SharedPrefsFile = "data"
        private fun getPrefs(context: Context): SharedPreferences{
            return context.getSharedPreferences(SharedPrefsFile, Context.MODE_PRIVATE)
        }

        fun save(context: Context, key: String, value: String) {
            getPrefs(context).edit().putString(key, value). apply()
        }

        fun getString(context: Context, key: String): String? {
            return getPrefs(context).getString(key, "")
        }

        fun clearSharedPrefs(context: Context) {
            getPrefs(context).edit().clear().apply()
        }
    }
}