package com.broccolistefanipss.esamedazero.manager

import android.content.Context
import android.content.SharedPreferences
import android.util.Log

class SessionManager(context: Context) {
    // Inizializza le SharedPreferences
    private var pref: SharedPreferences = context.getSharedPreferences(PrefName, Context.MODE_PRIVATE)
    // Crea un editor per modificare le SharedPreferences
    private var editor: SharedPreferences.Editor = pref.edit()

    // Controlla se l'utente è già loggato
    val isLoggedIn: Boolean
        get() = pref.getBoolean(KeyIsLoggedIn, false)

    // Ottiene o imposta il nome utente dell'utente loggato
    var userName: String?
        get() = pref.getString(KeyUserName, null)
        set(value) {
            editor.putString(KeyUserName, value)
            editor.commit() // Salva le modifiche
        }

    // Crea una sessione di login per l'utente
    fun createLoginSession(username: String) {
        this.userName = username // Imposta il nome utente
        setLogin(true) // Imposta lo stato di login a true
    }

    // Imposta lo stato di login
    fun setLogin(isLoggedIn: Boolean) {
        editor.putBoolean(KeyIsLoggedIn, isLoggedIn) // Imposta lo stato di login
        editor.commit() // Salva le modifiche
        Log.d(Tag, "Sessione modificata") // Log per il debug
    }

    companion object {
        // Tag per il debug
        private val Tag = SessionManager::class.java.simpleName
        // Nome delle SharedPreferences
        private val PrefName = "AndroidLoginPref"
        // Chiavi per le SharedPreferences
        const val KeyUserName = "user_name"
        private val KeyIsLoggedIn = "IsLoggedIn"
    }
}
