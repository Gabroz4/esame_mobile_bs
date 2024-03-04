package com.broccolistefanipss.esamedazero.global

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.broccolistefanipss.esamedazero.model.TrainingSession
import com.broccolistefanipss.esamedazero.model.User

class DB(context: Context) : SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(SqlTable.User)
        db?.execSQL(SqlTable.TrainingSessions)
    }

    // in caso upgrade verisone DB
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS TrainingSessions")
        db.execSQL("DROP TABLE IF EXISTS User")
        onCreate(db)
    }

    // log in console degli utenti (debugging)
    fun logUserTable() {
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM User", null)
        with(cursor) {
            while (moveToNext()) {
                val userName = getString(getColumnIndexOrThrow("userName"))
                val password = getString(getColumnIndexOrThrow("password"))
                val sesso = getString(getColumnIndexOrThrow("Sesso"))
                val eta = getInt(getColumnIndexOrThrow("Eta"))
                val altezza = getInt(getColumnIndexOrThrow("Altezza"))
                val peso = getDouble(getColumnIndexOrThrow("Peso"))
                val obiettivo = getString(getColumnIndexOrThrow("Obiettivo"))
                Log.d("UserTable", "User: $userName, Password: $password, Sesso: $sesso, Età: $eta, Altezza: $altezza, Peso: $peso, Obiettivo: $obiettivo")
            }
            close()
        }
    }

    // inserisci user
    fun insertUser(userName: String, password: String, sesso: String, eta: Int, altezza: Int, peso: Int, obiettivo: String) {
        if (!isNameExists(userName)) {
            val sqlQuery = "INSERT INTO User(userName, password, Sesso, Eta, Altezza, Peso, Obiettivo) VALUES(?, ?, ?, ?, ?, ?, ?)"
            val database = this.writableDatabase
            database.execSQL(sqlQuery, arrayOf(userName, password, sesso, eta, altezza, peso, obiettivo))
            logUserTable() // Consider removing or securing this for production to protect user data
        } else {
            Log.d("Utente", "userName '$userName' esiste già nel database")
        }
    }


    // check se username esiste
    private fun isNameExists(userName: String): Boolean {
        val database = readableDatabase
        database.rawQuery("SELECT * FROM User WHERE userName = ?", arrayOf(userName)).use { cursor ->
            return cursor.count > 0
        }
    }

    // inserisci sessione
    fun insertTrainingSession(userName: String, sessionDate: String, duration: Int, trainingType: String, burntCalories: Int) {
        val sqlQuery = "INSERT INTO TrainingSessions(userName, sessionDate, duration, trainingType, burntCalories) VALUES(?, ?, ?, ?, ?)"
        val database = this.writableDatabase
        database.execSQL(sqlQuery, arrayOf(userName, sessionDate, duration, trainingType, burntCalories))
    }

    // dati allenamenti singolo utente
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
                val burntCalories = getInt(getColumnIndexOrThrow("burntCalories"))
                trainingSessionsList.add(TrainingSession(sessionId, userName, sessionDate, duration, trainingType, burntCalories))
            }
            close()
        }
        return trainingSessionsList
    }

    // dati degli utenti
    fun getData(): List<User> {
        val userList = mutableListOf<User>()
        val query = "SELECT * FROM User"
        val database = this.readableDatabase
        database.rawQuery(query, null).use { cursor ->
            if (cursor.moveToFirst()) {
                do {
                    val userName = cursor.getString(cursor.getColumnIndexOrThrow("userName"))
                    val password = cursor.getString(cursor.getColumnIndexOrThrow("password"))
                    val sesso = cursor.getString(cursor.getColumnIndexOrThrow("Sesso"))
                    val eta = cursor.getInt(cursor.getColumnIndexOrThrow("Eta"))
                    val altezza = cursor.getInt(cursor.getColumnIndexOrThrow("Altezza"))
                    val peso = cursor.getDouble(cursor.getColumnIndexOrThrow("Peso"))
                    val obiettivo = cursor.getString(cursor.getColumnIndexOrThrow("Obiettivo"))

                    userList.add(User(userName, password, sesso, eta, altezza, peso, obiettivo))
                } while (cursor.moveToNext())
            }
        }
        return userList
    }

    fun userLogin(userName: String, password: String): Boolean {
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM User WHERE userName = ? AND password = ?", arrayOf(userName, password))
        val userExists = cursor.count > 0
        cursor.close()
        return userExists
    }

    companion object {
        private const val DB_VERSION = 5
        private const val DB_NAME = "SportsTracker.db"
    }
}
