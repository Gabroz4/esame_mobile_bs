package com.broccolistefanipss.esamedazero.global

import android.content.ContentValues
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
            val sqlQuery = "INSERT INTO User(userName, password, sesso, eta, altezza, peso, obiettivo) VALUES(?, ?, ?, ?, ?, ?, ?)"
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

    // Funzione per ottenere la data corrente nel formato desiderato (es: "2024-08-20")
    private fun getCurrentDate(): String {
        val formatter = java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault())
        return formatter.format(java.util.Date())
    }

    // Recupera le sessioni di allenamento per un utente specifico.
    fun getUserTrainingSessions(userName: String): List<TrainingSession> {
        val trainingSessionsList = mutableListOf<TrainingSession>()
        val db = this.readableDatabase

        // Utilizza il cursore all'interno di un blocco "use" per garantire la chiusura.
        db.rawQuery("SELECT * FROM TrainingSessions WHERE userName = ?", arrayOf(userName)).use { cursor ->
            while (cursor.moveToNext()) {
                // Estrai i dati da ogni colonna del cursore
                val id = cursor.getInt(cursor.getColumnIndexOrThrow("sessionId"))
                val sessionDate = cursor.getString(cursor.getColumnIndexOrThrow("sessionDate"))
                val duration = cursor.getInt(cursor.getColumnIndexOrThrow("duration"))
                val trainingType = cursor.getString(cursor.getColumnIndexOrThrow("trainingType"))
                val burntCalories = cursor.getInt(cursor.getColumnIndexOrThrow("burntCalories"))

                // Crea un oggetto TrainingSession con i dati estratti
                val trainingSession = TrainingSession(id, userName, sessionDate, duration, trainingType, burntCalories)

                // Aggiungi l'oggetto alla lista
                trainingSessionsList.add(trainingSession)
            }
        }
        Log.d("NewTrainingActivity", "Sessioni salvate: $trainingSessionsList")
        return trainingSessionsList
    }

    // Recupera i dati degli utenti.
    fun getData(): List<User> {
        val userList = mutableListOf<User>()
        val query = "SELECT * FROM User"
        val database = this.readableDatabase
        database.rawQuery(query, null).use { cursor ->
            if (cursor.moveToFirst()) {
                do {
                    val userName = cursor.getString(cursor.getColumnIndexOrThrow("userName"))
                    val password = cursor.getString(cursor.getColumnIndexOrThrow("password"))
                    val sesso = cursor.getString(cursor.getColumnIndexOrThrow("sesso"))
                    val eta = cursor.getInt(cursor.getColumnIndexOrThrow("eta"))
                    val altezza = cursor.getInt(cursor.getColumnIndexOrThrow("altezza"))
                    val peso = cursor.getDouble(cursor.getColumnIndexOrThrow("peso"))
                    val obiettivo = cursor.getString(cursor.getColumnIndexOrThrow("obiettivo"))

                    userList.add(User(userName, password, sesso, eta, altezza, peso, obiettivo))
                } while (cursor.moveToNext())
            }
        }
        return userList
    }

    fun getUserData(username: String): User? {
        val db = this.readableDatabase
        val query = "SELECT * FROM User WHERE userName = ?"
        val cursor = db.rawQuery(query, arrayOf(username))

        var user: User? = null
        if (cursor != null && cursor.moveToFirst()) {
            val userName = cursor.getString(cursor.getColumnIndexOrThrow("userName"))
            val password = cursor.getString(cursor.getColumnIndexOrThrow("password"))
            val sesso = cursor.getString(cursor.getColumnIndexOrThrow("sesso"))
            val eta = cursor.getInt(cursor.getColumnIndexOrThrow("eta"))
            val altezza = cursor.getInt(cursor.getColumnIndexOrThrow("altezza"))
            val peso = cursor.getDouble(cursor.getColumnIndexOrThrow("peso"))
            val obiettivo = cursor.getString(cursor.getColumnIndexOrThrow("obiettivo"))

            user = User(userName, password, sesso, eta, altezza, peso, obiettivo)
        }

        cursor?.close()
        return user
    }

    fun userLogin(userName: String, password: String): Boolean {
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM User WHERE userName = ? AND password = ?", arrayOf(userName, password))
        val userExists = cursor.count > 0
        cursor.close()
        return userExists
    }

    // Funzione per aggiornare i dati di un utente
    fun updateUser(
        currentUserName: String,
        //newUserName: String? = null,
        //newPassword: String? = null,
        //newSesso: String? = null,
        newEta: Int? = null,
        newAltezza: Int? = null,
        newPeso: Double? = null,
        newObiettivo: String? = null
    ): Boolean {
        // Ottieni un'istanza del database in modalità scrittura
        val db = this.writableDatabase

        // Prepara i valori da aggiornare
        val contentValues = ContentValues().apply {
            //newUserName?.let { put("userName", it) }
            //newPassword?.let { put("password", it) }
            //newSesso?.let { put("sesso", it) }
            newEta?.let { put("eta", it) }
            newAltezza?.let { put("altezza", it) }
            newPeso?.let { put("peso", it) }
            newObiettivo?.let { put("obiettivo", it) }
        }

        val affectedRows = db.update(
            "User",
            contentValues,
            "userName = ?",
            arrayOf(currentUserName)
        )


        // Esegui l'aggiornamento solo se ci sono valori da modificare
        return if (contentValues.size() > 0) {
            val affectedRows = db.update(
                "User",
                contentValues,
                "userName = ?",
                arrayOf(currentUserName)
            )

            Log.d("EditUserActivity", "Valori aggiornati: $contentValues")
            Log.d("EditUserActivity", "Righe modificate: $affectedRows")

            db.close() // Chiudi il database
            affectedRows > 0 // True se almeno una riga è stata aggiornata
        } else {
            db.close() // Chiudi il database anche se non ci sono modifiche
            false // Nessun valore da aggiornare
        }


    }



    companion object {
        private const val DB_VERSION = 8 // Versione del database.
        private const val DB_NAME = "SportsTracker.db" // Nome del database.
    }
}
