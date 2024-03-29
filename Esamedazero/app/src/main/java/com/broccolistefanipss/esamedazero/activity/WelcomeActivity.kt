package com.broccolistefanipss.esamedazero.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import com.broccolistefanipss.esamedazero.R
import com.broccolistefanipss.esamedazero.global.DB
import com.broccolistefanipss.esamedazero.manager.SessionManager

class WelcomeActivity : AppCompatActivity() {

    private lateinit var userNameEditText: EditText
    private lateinit var passwordEditText: EditText  // Aggiunta password EditText
    private lateinit var sessoEditText: EditText
    private lateinit var etaEditText: EditText
    private lateinit var altezzaEditText: EditText
    private lateinit var pesoEditText: EditText
    private lateinit var objectiveSpinner: Spinner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)

        //Controlla se l'utente è già loggato
        val sessionManager = SessionManager(this)
        if (sessionManager.isLoggedIn) {
            // Reindirizza l'utente alla BotMenuActivity
            val intent = Intent(this, BotMenuActivity::class.java)
            startActivity(intent)
            finish()
        }

        // Inizializza EditText e Spinner
        userNameEditText = findViewById(R.id.userName)
        passwordEditText = findViewById(R.id.password)
        sessoEditText = findViewById(R.id.sesso)
        etaEditText = findViewById(R.id.eta)
        altezzaEditText = findViewById(R.id.altezza)
        pesoEditText = findViewById(R.id.peso)
        objectiveSpinner = findViewById(R.id.spinnerOptions)

        // Spinner con due opzioni
        val options = arrayOf("Perdi peso", "Migliora")
        objectiveSpinner.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, options)

        val nextButton = findViewById<Button>(R.id.nextButton)
        nextButton.setOnClickListener {
            saveUserData()
        }
    }

    private fun saveUserData() {
        // Prendi info utenti da EditText e Spinner
        val userNameString = userNameEditText.text.toString()
        val passwordString = passwordEditText.text.toString()  // Ottieni la password dall'EditText
        val sessoString = sessoEditText.text.toString()
        val etaValue = etaEditText.text.toString().toIntOrNull() ?: 0
        val altezzaValue = altezzaEditText.text.toString().toIntOrNull() ?: 0
        val pesoValue = pesoEditText.text.toString().toIntOrNull() ?: 0
        val selectedObjective = objectiveSpinner.selectedItem.toString()

        // Inserisci dati nel database
        DB(this).insertUser(userNameString, passwordString, sessoString, etaValue, altezzaValue, pesoValue, selectedObjective)

        // Salva i dati dell'utente (tranne password e obiettivo) nelle SharedPreferences
        saveToSharedPreferences(userNameString, sessoString, etaValue, altezzaValue, pesoValue, "")

        // Vai a LoginActivity
        startActivity(Intent(this, LoginActivity::class.java))
        finish() // Termina WelcomeActivity
    }

    // Metodo per salvare i dati dell'utente (tranne password e obiettivo) nelle SharedPreferences
    private fun saveToSharedPreferences(
        userName: String?,
        sesso: String,
        eta: Int,
        altezza: Int,
        peso: Int,
        obiettivo: String
    ) {
        val sharedPreferences = getSharedPreferences("UserData", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("userName", userName)
        editor.putString("sesso", sesso)
        editor.putInt("eta", eta)
        editor.putInt("altezza", altezza)
        editor.putInt("peso", peso)
        editor.putString("obiettivo", "")
        editor.apply()

        // Aggiungi log per visualizzare ciò che viene salvato nelle SharedPreferences
        Log.d("WelcomeActivity", "Username: $userName")
        Log.d("WelcomeActivity", "Sesso: $sesso")
        Log.d("WelcomeActivity", "Età: $eta")
        Log.d("WelcomeActivity", "Altezza: $altezza")
        Log.d("WelcomeActivity", "Peso: $peso")
    }
}
