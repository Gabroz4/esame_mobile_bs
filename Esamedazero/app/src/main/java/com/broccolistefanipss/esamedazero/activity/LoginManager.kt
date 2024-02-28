package com.broccolistefanipss.esamedazero.activity

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.broccolistefanipss.esamedazero.R
import com.broccolistefanipss.esamedazero.manager.SessionManager

class LoginManager(private val context: Context) {

    fun attemptLogin(username: String, password: String): Boolean {
        // confronta credenziali utente con quelle salvate sul database

        if (username == "admin" && password == "password") {
            val sessionManager = SessionManager(context)
            sessionManager.setLogin(true)  // se ha successo
            sessionManager.userName = username
            return true
        }

        // se  fallisce
        return false
    }

    fun logout() {
        val sessionManager = SessionManager(context)
        sessionManager.setLogin(false)
        sessionManager.userName = null
    }
}