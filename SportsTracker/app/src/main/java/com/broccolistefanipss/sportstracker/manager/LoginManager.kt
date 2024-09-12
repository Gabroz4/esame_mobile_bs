package com.broccolistefanipss.sportstracker.manager

import android.content.Context
import android.content.Intent
import com.broccolistefanipss.sportstracker.activity.LoginActivity
import com.broccolistefanipss.sportstracker.global.DB

class LoginManager(private val context: Context) {

    fun attemptLogin(username: String, password: String): Boolean {
        val db = DB(context)
        if (db.userLogin(username, password) == true) {
            val sessionManager = SessionManager(context)
            sessionManager.setLogin(true)
            sessionManager.userName = username
            return true
        }
        return false
    }

    fun logout() {
        val sessionManager = SessionManager(context)
        sessionManager.setLogin(false)
        sessionManager.userName = null

        val intent = Intent(context, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        context.startActivity(intent)
    }
}
