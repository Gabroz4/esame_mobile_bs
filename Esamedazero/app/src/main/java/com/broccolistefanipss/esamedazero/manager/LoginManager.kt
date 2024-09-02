package com.broccolistefanipss.esamedazero.manager

import android.content.Context
import android.content.Intent
import android.util.Log
import com.broccolistefanipss.esamedazero.activity.LoginActivity
import com.broccolistefanipss.esamedazero.global.DB

class LoginManager(private val context: Context) {

    fun attemptLogin(username: String, password: String): Boolean {
        val db = DB(context)
        if (db.userLogin(username, password)) {
            val sessionManager = SessionManager(context)
            sessionManager.setLogin(true)
            saveToSharedPreferences(context, username)
            sessionManager.userName = username
            return true
        }
        return false
    }
    private fun saveToSharedPreferences(context: Context, username: String) {
        val sharedPreferences = context.getSharedPreferences("UserData", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("userName", username)
        editor.apply()
    }

    fun logout() {
        val sessionManager = SessionManager(context)
        sessionManager.setLogin(false)
        sessionManager.userName = null

        // Reindirizza alla LoginActivity dopo il logout
        val intent = Intent(context, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        context.startActivity(intent)
    }
}
