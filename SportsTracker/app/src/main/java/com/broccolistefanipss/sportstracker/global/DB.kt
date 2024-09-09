package com.broccolistefanipss.sportstracker.global

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.broccolistefanipss.sportstracker.model.CalendarTraining
import com.broccolistefanipss.sportstracker.model.TrainingSession
import com.broccolistefanipss.sportstracker.model.User
import com.google.android.gms.maps.model.LatLng

// Classe DB che estende SQLiteOpenHelper per gestire la creazione, l'apertura e l'aggiornamento del database.
class DB(context: Context) : SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {

    // Viene chiamata quando il database viene creato per la prima volta. Qui vengono create le tabelle.
    override fun onCreate(db: SQLiteDatabase?) {
        // Esegue SQL per creare le tabelle User e TrainingSessions.
        db?.execSQL(SqlTable.User)
        db?.execSQL(SqlTable.TrainingSessions)
        db?.execSQL(SqlTable.CalendarTraining)
        db?.execSQL(SqlTable.Location)
    }

    // Viene chiamata quando il database deve essere aggiornato, ad esempio quando incrementi la versione del database.
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // Rimuove le tabelle esistenti per ricrearle.
        db.execSQL("DROP TABLE IF EXISTS TrainingSessions")
        db.execSQL("DROP TABLE IF EXISTS CalendarTraining")
        db.execSQL("DROP TABLE IF EXISTS User")
        db.execSQL("DROP TABLE IF EXISTS Location")
        onCreate(db) // Richiama onCreate per ricreare le tabelle
    }

    // Inserisce un nuovo utente nel database.
    fun insertUser(userName: String, password: String, sesso: String, eta: Int, altezza: Int, peso: Int, obiettivo: String): Boolean {
        // Verifica se l'utente esiste già.
        if (!isNameExists(userName)) {
            // Prepara la query SQL e inserisce i dati.
            val sqlQuery = "INSERT INTO User(userName, password, sesso, eta, altezza, peso, obiettivo) VALUES(?, ?, ?, ?, ?, ?, ?)"
            val database = this.writableDatabase
            database.execSQL(sqlQuery, arrayOf(userName, password, sesso, eta, altezza, peso, obiettivo))
            Log.d("Username: ", userName)
            Log.d("sesso: ", sesso)
            Log.d("eta: ", eta.toString())
            Log.d("altezza: ", altezza.toString())
            Log.d("peso: ", peso.toString())
            Log.d("obiettivo: ", obiettivo)
            return true
        } else {
            Log.d("DB", "Username '$userName' esiste già nel database")
            return false
        }
    }

    fun getAllTrainingsByUserId(userId: String): List<TrainingSession> {
        val trainings = mutableListOf<TrainingSession>()
        val db = this.readableDatabase
        val query = "SELECT * FROM TrainingSessions WHERE userName = ?"

        db.rawQuery(query, arrayOf(userId)).use { cursor ->
            while (cursor.moveToNext()) {
                val id = cursor.getInt(cursor.getColumnIndexOrThrow("sessionId"))
                val sessionDate = cursor.getString(cursor.getColumnIndexOrThrow("sessionDate"))
                val duration = cursor.getInt(cursor.getColumnIndexOrThrow("duration"))
                val trainingType = cursor.getString(cursor.getColumnIndexOrThrow("trainingType"))
                val distance = cursor.getFloat(cursor.getColumnIndexOrThrow("distance"))
                val burntCalories = cursor.getInt(cursor.getColumnIndexOrThrow("burntCalories"))

                val training =
                    TrainingSession(id, userId, sessionDate, duration,distance, trainingType, burntCalories)
                trainings.add(training)
            }
        }

        return trainings
    }

    // Funzione per ottenere tutte le posizioni di un allenamento
    fun getLocationsByTrainingId(trainingId: Long): List<LatLng> {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT latitude, longitude FROM Location WHERE trainingId = ?", arrayOf(trainingId.toString()))

        val locations = mutableListOf<LatLng>()
        if (cursor.moveToFirst()) {
            do {
                val latitude = cursor.getDouble(cursor.getColumnIndexOrThrow("latitude"))
                val longitude = cursor.getDouble(cursor.getColumnIndexOrThrow("longitude"))
                locations.add(LatLng(latitude, longitude))
            } while (cursor.moveToNext())
        }
        cursor.close()
        return locations
    }

    fun insertTrainingLocation(sessionId: Long, latitude: Double, longitude: Double, timestamp: Long) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("trainingId", sessionId)
            put("latitude", latitude)
            put("longitude", longitude)
            put("timestamp", timestamp)
        }
        db.insert("Location", null, values)
        db.close()
    }

    // Controlla se un nome utente esiste già nel database.
    private fun isNameExists(userName: String): Boolean {
        val database = readableDatabase
        database.rawQuery("SELECT * FROM User WHERE userName = ?", arrayOf(userName)).use { cursor ->
            return cursor.count > 0 // True se esiste già, False altrimenti.
        }
    }

    fun insertTrainingSession(userName: String, sessionDate: String, duration: Int, distance: Float, trainingType: String, burntCalories: Int): Long {
        val database = this.writableDatabase
        val contentValues = ContentValues().apply {
            put("userName", userName)
            put("sessionDate", sessionDate)
            put("duration", duration)
            put("distance", distance)
            put("trainingType", trainingType)
            put("burntCalories", burntCalories)
        }
        // Inserisci i dati e restituisci l'ID della riga appena inserita
        val sessionId = database.insert("TrainingSessions", null, contentValues)
        database.close() // Chiude il database
        return sessionId
    }

    fun insertCalendarTraining(userName: String, date: String, description: String) {
        val database = this.writableDatabase
        val contentValues = ContentValues().apply {
            put("userName", userName)
            put("date", date)
            put("description", description)
        }
        database.insert("CalendarTraining", null, contentValues)
        database.close()
    }

    fun getCalendarTrainingsUser(userName: String): MutableList<CalendarTraining> {
        val calendarTrainingsList = mutableListOf<CalendarTraining>()
        val db = this.readableDatabase

        db.rawQuery("SELECT * FROM CalendarTraining WHERE userName = ?", arrayOf(userName)).use { cursor ->
            while (cursor.moveToNext()) {
                // Estrai i dati da ogni colonna

                val date = cursor.getString(cursor.getColumnIndexOrThrow("date"))
                val description = cursor.getString(cursor.getColumnIndexOrThrow("description"))

                // Crea un oggetto TrainingSession con i dati estratti
                val calendarTraining = CalendarTraining(userName, date, description)

                // Aggiungi l'oggetto alla lista
                calendarTrainingsList.add(calendarTraining)
            }
        }
        return calendarTrainingsList
    }

    // Recupera le sessioni di allenamento per un utente specifico.
    fun getUserTrainingSessions(userName: String): List<TrainingSession> {
        val trainingSessionsList = mutableListOf<TrainingSession>()
        val db = this.readableDatabase

        db.rawQuery("SELECT * FROM TrainingSessions WHERE userName = ?", arrayOf(userName)).use { cursor ->
            while (cursor.moveToNext()) {
                // Estrai i dati da ogni colonna
                val id = cursor.getInt(cursor.getColumnIndexOrThrow("sessionId"))
                val sessionDate = cursor.getString(cursor.getColumnIndexOrThrow("sessionDate"))
                val duration = cursor.getInt(cursor.getColumnIndexOrThrow("duration"))
                val trainingType = cursor.getString(cursor.getColumnIndexOrThrow("trainingType"))
                val distance = cursor.getFloat(cursor.getColumnIndexOrThrow("distance"))
                val burntCalories = cursor.getInt(cursor.getColumnIndexOrThrow("burntCalories"))

                // Crea un oggetto TrainingSession con i dati estratti
                val trainingSession = TrainingSession(id, userName, sessionDate, duration,distance, trainingType,  burntCalories)

                // Aggiungi l'oggetto alla lista
                trainingSessionsList.add(trainingSession)
            }
        }
        return trainingSessionsList
    }

    fun deleteTrainingSession(sessionId: Int): Boolean {
        val database = this.writableDatabase
        // La funzione restituisce true se una riga è stata effettivamente eliminata
        val affectedRows = database.delete("TrainingSessions", "sessionId = ?", arrayOf(sessionId.toString()))
        database.close() // Chiude il database per liberare risorse
        return affectedRows > 0
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
        newEta: Int? = null,
        newAltezza: Int? = null,
        newPeso: Double? = null,
        newObiettivo: String? = null,
        newSesso: String? = null
    ): Boolean {
        // Ottieni un'istanza del database in modalità scrittura
        val db = this.writableDatabase

        // valori da aggiornare
        val contentValues = ContentValues().apply {
            newEta?.let { put("eta", it) }
            newAltezza?.let { put("altezza", it) }
            newPeso?.let { put("peso", it) }
            newObiettivo?.let { put("obiettivo", it) }
            newSesso?.let { put("sesso", it) }
        }

        // Verifica se ci sono valori da aggiornare
        if (contentValues.size() > 0) {
            val affectedRows = db.update(
                "User",
                contentValues,
                "userName = ?",
                arrayOf(currentUserName)
            )

            Log.d("EditUserActivity", "Valori aggiornati: $contentValues")
            Log.d("EditUserActivity", "Righe modificate: $affectedRows")

            db.close() // Chiudi il database
            return affectedRows > 0 // True se almeno una riga è stata aggiornata
        } else {
            db.close() // Chiudi il database anche se non ci sono modifiche
            return false // Nessun valore da aggiornare
        }
    }

    companion object {
        private const val DB_VERSION = 18 // Versione del database.
        private const val DB_NAME = "SportsTracker.db" // Nome del database.
    }
}
