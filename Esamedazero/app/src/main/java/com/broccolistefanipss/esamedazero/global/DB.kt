package com.broccolistefanipss.esamedazero.global

import android.annotation.SuppressLint
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.broccolistefanipss.esamedazero.manager.SharedPrefs
import com.broccolistefanipss.esamedazero.model.Utente
import java.lang.Exception

class DB(val context: Context) : SQLiteOpenHelper(context, DB_Name, null, DB_Version) {

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(SqlTable.Utente)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // Drop the existing Utente table
        db.execSQL("DROP TABLE IF EXISTS Utente")

        // Recreate the Utente table
        db.execSQL(SqlTable.Utente)
    }

    //Funzione per runnare una query
    fun executeQuery(sql:String):Boolean{
        try {
            val database = this.writableDatabase
            database.execSQL(sql)
        }catch (e:Exception){
            e.printStackTrace()
            return false
        }
        return true
    }


    //Funzione per il recupero dei dati
    fun getData(): List<Utente> {
        val utenteList = mutableListOf<Utente>()
        var cursor: Cursor? = null
        try {
            val database = this.readableDatabase
            cursor = database.rawQuery("SELECT * FROM Utente", null)
            if (cursor != null && cursor.count > 0) {
                val usernameIndex = cursor.getColumnIndex("userName")
                val sessoIndex = cursor.getColumnIndex("Sesso") // Add other column indices as needed
                val etaIndex = cursor.getColumnIndex("Eta")
                val altezzaIndex = cursor.getColumnIndex("Altezza")
                val pesoIndex = cursor.getColumnIndex("Peso")
                val obiettivoIndex = cursor.getColumnIndex("Obiettivo")

                if (usernameIndex >= 0) {
                    cursor.moveToFirst()
                    do {
                        val userName = cursor.getString(usernameIndex) ?: "Default Value"
                        Log.d("Utente", "userName: $userName")

                        // Instantiate Utente with necessary parameters
                        val utente = Utente(
                            userName,
                            cursor.getString(sessoIndex) ?: "",
                            cursor.getInt(etaIndex),
                            cursor.getInt(altezzaIndex),
                            cursor.getDouble(pesoIndex),
                            cursor.getString(obiettivoIndex) ?: ""
                        )

                        utenteList.add(utente)
                    } while (cursor.moveToNext())
                } else {
                    Log.e("Utente", "Column 'userName' not found in cursor")
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            cursor?.close()
        }

        return utenteList
    }



    fun logUtenteTable() {
        var cursor: Cursor? = null
        try {
            val database = this.readableDatabase
            cursor = database.rawQuery("SELECT * FROM Utente", null)

            if (cursor != null && cursor.count > 0) {
                val usernameIndex = cursor.getColumnIndex("userName")
                val sessoIndex = cursor.getColumnIndex("Sesso")
                val etaIndex = cursor.getColumnIndex("Eta")
                val altezzaIndex = cursor.getColumnIndex("Altezza")
                val pesoIndex = cursor.getColumnIndex("Peso")
                val obiettivoIndex = cursor.getColumnIndex("Obiettivo")

                cursor.moveToFirst()
                do {
                    val userName = if (usernameIndex >= 0) cursor.getString(usernameIndex) else "N/A"
                    val sesso = if (sessoIndex >= 0) cursor.getString(sessoIndex) else "N/A"
                    val eta = if (etaIndex >= 0) cursor.getInt(etaIndex) else -1
                    val altezza = if (altezzaIndex >= 0) cursor.getInt(altezzaIndex) else -1
                    val peso = if (pesoIndex >= 0) cursor.getInt(pesoIndex) else -1
                    val obiettivo = if (obiettivoIndex >= 0) cursor.getString(obiettivoIndex) else "N/A"

                    val rowData = "userName: $userName, Sesso: $sesso, " +
                            "Età: $eta, Altezza: $altezza, Peso: $peso, Obiettivo: $obiettivo"

                    Log.d("Utente", rowData)
                } while (cursor.moveToNext())
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            cursor?.close()
        }
    }
    // Usage in your insertData function
    fun insertData(userName: String, sesso: String, eta: Int, altezza: Int, peso: Int, obiettivo: String) {
        // Check if the name already exists
        if (!isNameExists(userName)) {
            val sqlQuery = "INSERT INTO Utente(userName, Sesso, Eta, Altezza, Peso, Obiettivo) " +
                    "VALUES('$userName', '$sesso', $eta, $altezza, $peso, '$obiettivo')"
            executeQuery(sqlQuery)
            logUtenteTable()

        } else {
            // Handle the case where the name already exists (e.g., show a message or perform some action)
            Log.d("Utente", "userName '$userName' already exists in the database")
        }
    }

    private fun isNameExists(userName: String): Boolean {
        val database = readableDatabase
        val cursor = database.rawQuery("SELECT * FROM Utente WHERE userName = ?", arrayOf(userName))
        val exists = cursor.count > 0
        cursor.close()
        return exists
    }




    companion object{
        private const val DB_Version = 2
        private const val DB_Name="Sports Tracker"

    }

}