package com.broccolistefanipss.sportstracker.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.broccolistefanipss.sportstracker.R
import com.broccolistefanipss.sportstracker.databinding.ActivityWelcomeBinding
import com.broccolistefanipss.sportstracker.global.DB
import com.broccolistefanipss.sportstracker.manager.SessionManager

class WelcomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWelcomeBinding

    private lateinit var userNameEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var sessoSpinner: Spinner
    private lateinit var etaEditText: EditText
    private lateinit var altezzaEditText: EditText
    private lateinit var pesoEditText: EditText
    private lateinit var objectiveSpinner: Spinner
    private lateinit var loginButton: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityWelcomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Controlla se l'utente è già loggato
        val sessionManager = SessionManager(this)
        if (sessionManager.isLoggedIn) {
            // Reindirizza l'utente alla BotMenuActivity
            val intent = Intent(this, BotMenuActivity::class.java)
            startActivity(intent)
            finish()
        }

        // Inizializza EditText e Spinner
        userNameEditText = binding.userName
        passwordEditText = binding.password
        sessoSpinner = binding.spinnerSesso
        etaEditText = binding.eta
        altezzaEditText = binding.altezza
        pesoEditText = binding.peso
        objectiveSpinner = binding.spinnerObiettivo
        loginButton = binding.loginButton

        // Spinner con due opzioni
        val objectiveOptions : Array<String> = resources.getStringArray(R.array.objectives_array)
        objectiveSpinner.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, objectiveOptions)

        val sessoOptions : Array<String> = resources.getStringArray(R.array.sesso_array)
        sessoSpinner.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, sessoOptions)

        val nextButton = findViewById<Button>(R.id.nextButton)
        nextButton.setOnClickListener {
            saveUserData()
        }
        loginButton.setOnClickListener { btnLoginOnclick() }
    }

    private fun btnLoginOnclick(){
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }


    //private fun saveUserData() {
    //    // Prendi le info dell'utente da EditText e Spinner
    //    val userNameString = userNameEditText.text.toString()
    //    val passwordString = passwordEditText.text.toString()
    //    val sessoString = sessoSpinner.selectedItem.toString()
    //    val etaValue = etaEditText.text.toString().toIntOrNull() ?: 0
    //    val altezzaValue = altezzaEditText.text.toString().toIntOrNull() ?: 0
    //    val pesoValue = pesoEditText.text.toString().toIntOrNull() ?: 0
    //    val selectedObjective = objectiveSpinner.selectedItem.toString()
//
    //    // Inserisci dati nel database
    //    DB(this).insertUser(userNameString, passwordString, sessoString, etaValue, altezzaValue, pesoValue, selectedObjective)
//
    //    // Salva i dati dell'utente (tranne password e obiettivo) nelle SharedPreferences
    //    saveToSharedPreferences(userNameString, sessoString, etaValue, altezzaValue, pesoValue) //+ obiettivo al limite
//
    //    // Vai a LoginActivity
    //    startActivity(Intent(this, LoginActivity::class.java))
    //    finish()
    //}

    private fun saveUserData() {
        // Prendi le info dell'utente da EditText e Spinner
        val userNameString = userNameEditText.text.toString()
        val passwordString = passwordEditText.text.toString()
        val sessoString = sessoSpinner.selectedItem.toString()
        val etaValue = etaEditText.text.toString().toIntOrNull() ?: 0
        val altezzaValue = altezzaEditText.text.toString().toIntOrNull() ?: 0
        val pesoValue = pesoEditText.text.toString().toIntOrNull() ?: 0
        val selectedObjective = objectiveSpinner.selectedItem.toString()

        // Inserisci dati nel database
        val result = DB(this).insertUser(userNameString, passwordString, sessoString, etaValue, altezzaValue, pesoValue, selectedObjective)

        // Salva i dati dell'utente (tranne password e obiettivo) nelle SharedPreferences
        saveToSharedPreferences(userNameString, sessoString, etaValue, altezzaValue, pesoValue)

        // Se lo user non esiste già, vai a LoginActivity
        if (result) {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        } else {
            // Se il metodo ha fallito, mostra un messaggio di errore
            Toast.makeText(this, "Questo UserName esiste già", Toast.LENGTH_SHORT).show()
        }
    }

    // Metodo per salvare i dati dell'utente (tranne password e obiettivo) nelle SharedPreferences
    private fun saveToSharedPreferences(
        userName: String?,
        sesso: String,
        eta: Int,
        altezza: Int,
        peso: Int,
        //obiettivo: String
    ) {
        val sharedPreferences = getSharedPreferences("UserData", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("userName", userName)
        editor.putString("sesso", sesso)
        editor.putInt("eta", eta)
        editor.putInt("altezza", altezza)
        editor.putInt("peso", peso)
        //editor.putString("obiettivo", obiettivo)
        editor.apply()

        // Aggiungi log per visualizzare ciò che viene salvato nelle SharedPreferences
        Log.d("WelcomeActivity", "Username: $userName")
        Log.d("WelcomeActivity", "Sesso: $sesso")
        Log.d("WelcomeActivity", "Età: $eta")
        Log.d("WelcomeActivity", "Altezza: $altezza")
        Log.d("WelcomeActivity", "Peso: $peso")
        //Log.d("WelcomeActivity", "Obiettivo: $obiettivo")
    }
}
