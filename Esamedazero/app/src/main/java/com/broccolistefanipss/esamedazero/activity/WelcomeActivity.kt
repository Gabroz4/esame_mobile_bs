package com.broccolistefanipss.esamedazero.activity

import android.content.Intent
import android.os.Bundle
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
    private lateinit var sessoEditText: EditText
    private lateinit var etaEditText: EditText
    private lateinit var altezzaEditText: EditText
    private lateinit var pesoEditText: EditText
    private lateinit var objectiveSpinner: Spinner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)

        // Controlla se l'utente è già loggato
        val sessionManager = SessionManager(this)
        if (sessionManager.isLoggedIn) {
            // Reindirizza l'utente alla BotMenuActivity
            val intent = Intent(this, BotMenuActivity::class.java)
            startActivity(intent)
            finish()
        }

        // inizializza TextEditor e Spinner
        userNameEditText = findViewById(R.id.userName)
        sessoEditText = findViewById(R.id.sesso)
        etaEditText = findViewById(R.id.eta)
        altezzaEditText = findViewById(R.id.altezza)
        pesoEditText = findViewById(R.id.peso)
        objectiveSpinner = findViewById(R.id.spinnerOptions)

        // spinner con due opzioni
        val options = arrayOf("Perdi peso", "Migliora")
        objectiveSpinner.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, options)

        val nextButton = findViewById<Button>(R.id.nextButton)
        nextButton.setOnClickListener {
            saveUserData()
        }
    }

    private fun saveUserData() {
        // prendi info utenti da editText e spinner
        val userNameString = userNameEditText.text.toString()
        val sessoString = sessoEditText.text.toString()
        val etaValue = etaEditText.text.toString().toIntOrNull() ?: 0
        val altezzaValue = altezzaEditText.text.toString().toIntOrNull() ?: 0
        val pesoValue = pesoEditText.text.toString().toIntOrNull() ?: 0
        val selectedObjective = objectiveSpinner.selectedItem.toString()

        // inserisci dati
        // modifica password!
        DB(this).insertUser(userNameString, "password", sessoString, etaValue, altezzaValue, pesoValue, selectedObjective)

        // vai a LoginActivity
        startActivity(Intent(this, LoginActivity::class.java))
        finish() // termina WelcomeActivity
    }
}
