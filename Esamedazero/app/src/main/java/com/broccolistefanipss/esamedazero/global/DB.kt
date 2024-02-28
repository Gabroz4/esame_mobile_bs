package com.broccolistefanipss.esamedazero.global

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.broccolistefanipss.esamedazero.model.TrainingSession
import com.broccolistefanipss.esamedazero.model.Utente

class DB(context: Context) : SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(SqlTable.Utente)
        db?.execSQL(SqlTable.TrainingSessions)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS TrainingSessions")
        db.execSQL("DROP TABLE IF EXISTS Utente")
        onCreate(db)
    }

    fun logUtenteTable() {
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM Utente", null)
        with(cursor) {
            while (moveToNext()) {
                val userName = getString(getColumnIndexOrThrow("userName"))
                val sesso = getString(getColumnIndexOrThrow("Sesso"))
                val eta = getInt(getColumnIndexOrThrow("Eta"))
                val altezza = getInt(getColumnIndexOrThrow("Altezza"))
                val peso = getDouble(getColumnIndexOrThrow("Peso"))
                val obiettivo = getString(getColumnIndexOrThrow("Obiettivo"))
                Log.d("UtenteTable", "User: $userName, Sesso: $sesso, Età: $eta, Altezza: $altezza, Peso: $peso, Obiettivo: $obiettivo")
            }
            close()
        }
    }

    // Insert user data
    fun insertData(userName: String, sesso: String, eta: Int, altezza: Int, peso: Int, obiettivo: String) {
        if (!isNameExists(userName)) {
            val sqlQuery = "INSERT INTO Utente(userName, Sesso, Eta, Altezza, Peso, Obiettivo) VALUES(?, ?, ?, ?, ?, ?)"
            val database = this.writableDatabase
            database.execSQL(sqlQuery, arrayOf(userName, sesso, eta, altezza, peso, obiettivo))
            logUtenteTable() // Consider removing or securing this for production to protect user data
        } else {
            Log.d("Utente", "userName '$userName' already exists in the database")
        }
    }


    // Check if the userName exists
    private fun isNameExists(userName: String): Boolean {
        val database = readableDatabase
        database.rawQuery("SELECT * FROM Utente WHERE userName = ?", arrayOf(userName)).use { cursor ->
            return cursor.count > 0
        }
    }

    // Insert training session data
    fun insertTrainingSession(userName: String, sessionDate: String, duration: Int, trainingType: String) {
        val sqlQuery = "INSERT INTO TrainingSessions(userName, sessionDate, duration, trainingType) VALUES(?, ?, ?, ?)"
        val database = this.writableDatabase
        database.execSQL(sqlQuery, arrayOf(userName, sessionDate, duration, trainingType))
    }

    fun getUserTrainingSessions(userName: String): List<TrainingSession> {
        val trainingSessionsList = mutableListOf<TrainingSession>()
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM TrainingSessions WHERE userName = ?", arrayOf(userName))
        with(cursor) {
            while (moveToNext()) {
                val sessionId = getInt(getColumnIndexOrThrow("sessionId"))
                val sessionDate = getString(getColumnIndexOrThrow("sessionDate"))
                val duration = getInt(getColumnIndexOrThrow("duration"))
                val trainingType = getString(getColumnIndexOrThrow("trainingType"))
                trainingSessionsList.add(TrainingSession(sessionId, userName, sessionDate, duration, trainingType))
            }
            close()
        }
        return trainingSessionsList
    }

    // Retrieve user data
    fun getData(): List<Utente> {
        val utenteList = mutableListOf<Utente>()
        val query = "SELECT * FROM Utente"
        val database = this.readableDatabase
        database.rawQuery(query, null).use { cursor ->
            if (cursor.moveToFirst()) {
                do {
                    val userName = cursor.getString(cursor.getColumnIndexOrThrow("userName"))
                    val sesso = cursor.getString(cursor.getColumnIndexOrThrow("Sesso"))
                    val eta = cursor.getInt(cursor.getColumnIndexOrThrow("Eta"))
                    val altezza = cursor.getInt(cursor.getColumnIndexOrThrow("Altezza"))
                    val peso = cursor.getDouble(cursor.getColumnIndexOrThrow("Peso"))
                    val obiettivo = cursor.getString(cursor.getColumnIndexOrThrow("Obiettivo"))

                    utenteList.add(Utente(userName, sesso, eta, altezza, peso, obiettivo))
                } while (cursor.moveToNext())
            }
        }
        return utenteList
    }

    companion object {
        private const val DB_VERSION = 2
        private const val DB_NAME = "SportsTracker.db"
    }
}
