package com.broccolistefanipss.esamedazero.manager

import android.content.Context
import android.content.SharedPreferences

// Classe per gestire le preferenze condivise, utilizzata per salvare e recuperare dati dell'applicazione.
public class SharedPrefs {
    // Tag usato per il logging, se necessario.
    private val Tag = SharedPrefs::class.java.simpleName

    companion object {
        // Chiavi usate per salvare i dati nelle preferenze condivise.
        var userName = "userName"
        var sesso = "sesso"
        var eta = "eta"
        var altezza = "altezza"
        var peso = "peso"
        var obiettivo = "obiettivo"

        // Nome del file delle preferenze condivise.
        private val SharedPrefsFile = "data"

        // Metodo privato per ottenere un'istanza di SharedPreferences.
        private fun getPrefs(context: Context): SharedPreferences {
            // Ritorna l'istanza di SharedPreferences per il file specificato e in modalità privata.
            return context.getSharedPreferences(SharedPrefsFile, Context.MODE_PRIVATE)
        }

        // Salva una stringa nelle preferenze condivise.
        fun save(context: Context, key: String, value: String) {
            // Ottiene l'editor di SharedPreferences, imposta la stringa e applica le modifiche.
            getPrefs(context).edit().putString(key, value).apply()
        }

        // Recupera una stringa dalle preferenze condivise usando la chiave specificata.
        fun getString(context: Context, key: String): String? {
            // Ritorna il valore associato alla chiave, o "" se non esiste tale chiave.
            return getPrefs(context).getString(key, "")
        }

        // Pulisce tutte le preferenze condivise.
        fun clearSharedPrefs(context: Context) {
            // Ottiene l'editor di SharedPreferences, cancella tutti i dati e applica le modifiche.
            getPrefs(context).edit().clear().apply()
        }
    }
}