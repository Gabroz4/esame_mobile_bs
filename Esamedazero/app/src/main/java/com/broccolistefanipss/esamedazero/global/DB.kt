package com.broccolistefanipss.esamedazero.global

import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.broccolistefanipss.esamedazero.SqlTable
import java.lang.Exception

class DB(val context: Context) : SQLiteOpenHelper(context, DB_Name, null, DB_Version) {

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(SqlTable.Utente)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        onCreate(db)
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
    fun getData(){
        var cursor: Cursor?=null
        try {
            val database = this.readableDatabase
            cursor = database.rawQuery("select Nome from Utente", null)
            if(cursor != null && cursor.count>0){
                cursor.moveToFirst()
            }
            do{
                //val nome =  cursor.getString(cursor.getColumnIndex("Nome")) --> nullable da risolvere
                val nome =  cursor.getColumnIndex("Nome")
                Log.d("Utente", "Nome $nome")
            }while(cursor.moveToNext())

        }catch (e:Exception){
            e.printStackTrace()
        }finally {
            cursor?.close()
        }
    }
    companion object{
        private const val DB_Version = 1
        private const val DB_Name="Run Tracker"

    }

}