package com.broccolistefanipss.esamedazero.manager

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

    // Salva una stringa nelle preferenze condivise.
    fun saveString(context: Context, key: String, value: String) {
        // Ottiene l'editor di SharedPreferences, imposta la stringa e applica le modifiche.
        getPrefs(context).edit().putString(key, value).apply()
    }

    // Recupera una stringa dalle preferenze condivise usando la chiave specificata.
    fun getString(context: Context, key: String): String? {
        // Ritorna il valore associato alla chiave, o null se non esiste tale chiave.
        return getPrefs(context).getString(key, null)
    }

    // Salva un intero nelle preferenze condivise.
    fun saveInt(context: Context, key: String, value: Int) {
        getPrefs(context).edit().putInt(key, value).apply()
    }

    // Recupera un intero dalle preferenze condivise usando la chiave specificata.
    fun getInt(context: Context, key: String): Int {
        // Ritorna il valore associato alla chiave, o 0 se non esiste tale chiave.
        return getPrefs(context).getInt(key, 0)
    }

    // Salva un double nelle preferenze condivise.
    fun saveDouble(context: Context, key: String, value: Double) {
        getPrefs(context).edit().putFloat(key, value.toFloat()).apply()
    }

    // Recupera un double dalle preferenze condivise usando la chiave specificata.
    fun getDouble(context: Context, key: String): Double {
        // Ritorna il valore associato alla chiave, o 0.0 se non esiste tale chiave.
        return getPrefs(context).getFloat(key, 0F).toDouble()
    }

    // Pulisce tutte le preferenze condivise.
    fun clearSharedPrefs(context: Context) {
        // Ottiene l'editor di SharedPreferences, cancella tutti i dati e applica le modifiche.
        getPrefs(context).edit().clear().apply()
    }
}

//package com.broccolistefanipss.esamedazero.manager
//
//import android.content.Context
//import android.content.SharedPreferences
//
//// Classe per gestire le preferenze condivise, utilizzata per salvare e recuperare dati dell'applicazione.
//public class SharedPrefs {
//    // Tag usato per il logging, se necessario.
//    private val Tag = SharedPrefs::class.java.simpleName
//
//    companion object {
//        // Chiavi usate per salvare i dati nelle preferenze condivise.
//        var userName = "userName"
//        var sesso = "sesso"
//        var eta = "eta"
//        var altezza = "altezza"
//        var peso = "peso"
//        var obiettivo = "obiettivo"
//
//        // Nome del file delle preferenze condivise.
//        private val SharedPrefsFile = "data"
//
//        // Metodo privato per ottenere un'istanza di SharedPreferences.
//        private fun getPrefs(context: Context): SharedPreferences {
//            // Ritorna l'istanza di SharedPreferences per il file specificato e in modalità privata.
//            return context.getSharedPreferences(SharedPrefsFile, Context.MODE_PRIVATE)
//        }
//
//        // Salva una stringa nelle preferenze condivise.
//        fun save(context: Context, key: String, value: String) {
//            // Ottiene l'editor di SharedPreferences, imposta la stringa e applica le modifiche.
//            getPrefs(context).edit().putString(key, value).apply()
//        }
//
//        // Recupera una stringa dalle preferenze condivise usando la chiave specificata.
//        fun getString(context: Context, key: String): String? {
//            // Ritorna il valore associato alla chiave, o null se non esiste tale chiave.
//            return getPrefs(context).getString(key, null)
//        }
//
//        // Recupera un intero dalle preferenze condivise usando la chiave specificata.
//        fun getInt(context: Context, key: Int): Int {
//            // Ritorna il valore associato alla chiave, o 0 se non esiste tale chiave.
//            return getPrefs(context).getInt(key.toString(), 0)
//        }
//
//        fun getDouble(context: Context, key: Double): Float {
//            // Ritorna il valore associato alla chiave, o 0 se non esiste tale chiave.
//            return getPrefs(context).getFloat(key.toString(), 0F)
//        }
//
//        // Pulisce tutte le preferenze condivise.
//        fun clearSharedPrefs(context: Context) {
//            // Ottiene l'editor di SharedPreferences, cancella tutti i dati e applica le modifiche.
//            getPrefs(context).edit().clear().apply()
//        }
//    }
//}