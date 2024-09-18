package com.broccolistefanipss.sportstracker.activity

import android.content.Intent
import android.os.Bundle
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
    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityWelcomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sessionManager = SessionManager(this)

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
        loginButton.setOnClickListener { loginIntentOnclick() }
    }

    private fun loginIntentOnclick(){
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }

    private fun saveUserData() {
        // prendi le info dell'utente da editText e spinner
        val userNameString = userNameEditText.text.toString()
        val passwordString = passwordEditText.text.toString()
        val sessoString = sessoSpinner.selectedItem.toString()
        val etaValue = etaEditText.text.toString().toIntOrNull() ?: 0
        val altezzaValue = altezzaEditText.text.toString().toIntOrNull() ?: 0
        val pesoValue = pesoEditText.text.toString().toIntOrNull() ?: 0
        val selectedObjective = objectiveSpinner.selectedItem.toString()

        // inserisci dati nel database
        val result = DB(this).insertUser(userNameString, passwordString, sessoString, etaValue, altezzaValue, pesoValue, selectedObjective)

        // se lo user non esiste già, memorizza le informazioni nel SessionManager
        if (result) {
            val sessionManager = SessionManager(this)

            // imposta l'utente come loggato e salva lo username nel session manager
            sessionManager.setLogin(true)
            sessionManager.userName = userNameString

            // vai alla LoginActivity
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        } else {
            // se il metodo ha fallito, mostra un messaggio di errore
            Toast.makeText(this, "Questo UserName esiste già", Toast.LENGTH_SHORT).show()
        }
    }
}
