package com.broccolistefanipss.esamedazero.manager

import android.content.Context
import com.broccolistefanipss.esamedazero.global.DB

class LoginManager(private val context: Context) {

    fun attemptLogin(username: String, password: String): Boolean {
        val db = DB(context)
        if (db.userLogin(username, password)) {
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
    }
}
