package com.broccolistefanipss.sportstracker.manager

import android.content.Context
import android.content.SharedPreferences

// Classe per gestire le preferenze condivise, utilizzata per salvare e recuperare dati dell'applicazione.

object SharedPrefs {
    // Chiavi usate per salvare i dati nelle preferenze condivise.
    const val userName = "userName"
    const val sesso = "sesso"
    const val eta = "eta"
    const val altezza = "altezza"
    const val peso = "peso"
    const val obiettivo = "obiettivo"

    // Nome del file delle preferenze condivise.
    private const val SharedPrefsFile = "data"

    // Metodo privato per ottenere un'istanza di SharedPreferences.
    private fun getPrefs(context: Context): SharedPreferences {
        // Ritorna l'istanza di SharedPreferences per il file specificato e in modalità privata.
        return context.getSharedPreferences(SharedPrefsFile, Context.MODE_PRIVATE)
    }

    // Pulisce tutte le preferenze condivise.
    fun clearSharedPrefs(context: Context) {
        getPrefs(context).edit().clear().apply()
    }
}