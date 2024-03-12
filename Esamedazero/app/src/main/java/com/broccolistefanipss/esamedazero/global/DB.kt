package com.broccolistefanipss.esamedazero.global

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.broccolistefanipss.esamedazero.model.TrainingSession
import com.broccolistefanipss.esamedazero.model.User

// Classe DB che estende SQLiteOpenHelper per gestire la creazione, l'apertura e l'aggiornamento del database.
class DB(context: Context) : SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {

    // Viene chiamata quando il database viene creato per la prima volta. Qui vengono create le tabelle.
    override fun onCreate(db: SQLiteDatabase?) {
        // Esegue SQL per creare le tabelle User e TrainingSessions.
        db?.execSQL(SqlTable.User)
        db?.execSQL(SqlTable.TrainingSessions)
    }

    // Viene chiamata quando il database deve essere aggiornato, ad esempio quando incrementi la versione del database.
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // Rimuove le tabelle esistenti per ricrearle.
        db.execSQL("DROP TABLE IF EXISTS TrainingSessions")
        db.execSQL("DROP TABLE IF EXISTS User")
        onCreate(db) // Richiama onCreate per ricreare le tabelle

        // Logica specifica per la versione 6 del database: inserisce dati di prova.
        if (newVersion == 6) {
            // Inserisce sessioni di allenamento di prova per l'utente "gab".
            insertTrainingSessionForUser(db, "gab", "2024-03-12", 60, "Cardio", 300)
            insertTrainingSessionForUser(db, "gab", "2024-03-14", 45, "Strength", 250)
        }
    }

    // Funzione di utilità per loggare gli utenti nel database (per debugging).
    fun logUserTable() {
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM User", null)
        with(cursor) {
            while (moveToNext()) {
                // Estrae i dati dell'utente e li logga.
                val userName = getString(getColumnIndexOrThrow("userName"))
                // Ometto i dettagli per brevità
                Log.d("UserTable", "Dettagli dell'utente")
            }
            close() // Importante chiudere il cursor per liberare risorse.
        }
    }

    // Inserisce un nuovo utente nel database.
    fun insertUser(userName: String, password: String, sesso: String, eta: Int, altezza: Int, peso: Int, obiettivo: String) {
        // Verifica se l'utente esiste già.
        if (!isNameExists(userName)) {
            // Prepara la query SQL e inserisce i dati.
            val sqlQuery = "INSERT INTO User(userName, password, Sesso, Eta, Altezza, Peso, Obiettivo) VALUES(?, ?, ?, ?, ?, ?, ?)"
            val database = this.writableDatabase
            database.execSQL(sqlQuery, arrayOf(userName, password, sesso, eta, altezza, peso, obiettivo))
            logUserTable() // Logga gli utenti per debug.
        } else {
            Log.d("Utente", "userName '$userName' esiste già nel database")
        }
    }

    // Helper per inserire una sessione di allenamento per l'utente direttamente nel DB durante l'upgrade.
    private fun insertTrainingSessionForUser(db: SQLiteDatabase, userName: String, sessionDate: String, duration: Int, trainingType: String, burntCalories: Int) {
        val sqlQuery = "INSERT INTO TrainingSessions(userName, sessionDate, duration, trainingType, burntCalories) VALUES(?, ?, ?, ?, ?)"
        db.execSQL(sqlQuery, arrayOf(userName, sessionDate, duration, trainingType, burntCalories))
    }

    // Controlla se un nome utente esiste già nel database.
    private fun isNameExists(userName: String): Boolean {
        val database = readableDatabase
        database.rawQuery("SELECT * FROM User WHERE userName = ?", arrayOf(userName)).use { cursor ->
            return cursor.count > 0 // True se esiste già, False altrimenti.
        }
    }

    // Inserisce una sessione di allenamento nel database.
    fun insertTrainingSession(userName: String, sessionDate: String, duration: Int, trainingType: String, burntCalories: Int) {
        val sqlQuery = "INSERT INTO TrainingSessions(userName, sessionDate, duration, trainingType, burntCalories) VALUES(?, ?, ?, ?, ?)"
        val database = this.writableDatabase
        database.execSQL(sqlQuery, arrayOf(userName, sessionDate, duration, trainingType, burntCalories))
    }

    // Recupera le sessioni di allenamento per un utente specifico.
    fun getUserTrainingSessions(userName: String): List<TrainingSession> {
        val trainingSessionsList = mutableListOf<TrainingSession>()
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM TrainingSessions WHERE userName = ?", arrayOf(userName))
        with(cursor) {
            while (moveToNext()) {
                // Estrae i dati di ogni sessione e aggiunge alla lista.
                // Ometto i dettagli per brevità.
            }
            close()
        }
        return trainingSessionsList
    }

    // Recupera i dati degli utenti.
    fun getData(): List<User> {
        // Logica simile a getUserTrainingSessions, ometto i dettagli per brevità.
        return listOf() // Placeholder per la lista di utenti.
    }

    // Verifica le credenziali di login di un utente.
    fun userLogin(userName: String, password: String): Boolean {
        // Implementazione omessa per brevità.
        return false
    }

    companion object {
        private const val DB_VERSION = 7 // Versione del database.
        private const val DB_NAME = "SportsTracker.db" // Nome del database.
    }
}
