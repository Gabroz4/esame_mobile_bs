package com.broccolistefanipss.esamedazero.activity

import android.content.Intent
import android.widget.Button
import com.broccolistefanipss.esamedazero.R
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.TextView
import com.broccolistefanipss.esamedazero.global.DB
import com.broccolistefanipss.esamedazero.manager.SessionManager
//import com.broccolistefanipss.esamedazero.manager.SharedPrefs.Companion.eta
//import com.broccolistefanipss.esamedazero.manager.SharedPrefs.Companion.sesso
import com.broccolistefanipss.esamedazero.model.Utente

class WelcomeActivity: AppCompatActivity() {

    //val myNumberView: View = findViewById(R.id.myNumber) // Ora puoi utilizzare myNumberView come desideri nel tuo codice

    var db: DB?=null
    var session: SessionManager?=null

    private lateinit var userName: TextView
    private lateinit var sesso: TextView
    private lateinit var obiettivo: TextView
    private lateinit var etaEditText: EditText
    private lateinit var altezzaEditText: EditText
    private lateinit var pesoEditText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)

        userName = findViewById(R.id.userName)
        sesso = findViewById(R.id.sesso)
        obiettivo = findViewById(R.id.obiettivo)
        etaEditText = findViewById(R.id.eta)
        altezzaEditText = findViewById(R.id.altezza)
        pesoEditText = findViewById(R.id.peso)

        val nextButton = findViewById<Button>(R.id.nextButton)
        val intent = Intent(this, BotMenuActivity::class.java)

        db = DB(this)

        nextButton.setOnClickListener {
            val userNameString = userName.text.toString()
            val sessoString = sesso.text.toString()
            val obiettivoString = obiettivo.text.toString()

            val etaValue = try {
                etaEditText.text.toString().toDouble().toInt()
            } catch (e: NumberFormatException) {
                0
            }

            val altezzaValue = try {
                altezzaEditText.text.toString().toDouble().toInt()
            } catch (e: NumberFormatException) {
                0
            }

            val pesoValue = try {
                pesoEditText.text.toString().toDouble().toInt()
            } catch (e: NumberFormatException) {
                0
            }

            db?.insertData(userNameString, sessoString, etaValue, altezzaValue, pesoValue, obiettivoString)
            db?.getData()

            startActivity(intent)
        }
    }
}