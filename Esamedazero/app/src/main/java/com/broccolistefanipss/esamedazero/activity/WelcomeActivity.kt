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

        // Initialize your EditText and Spinner fields
        userNameEditText = findViewById(R.id.userName)
        sessoEditText = findViewById(R.id.sesso)
        etaEditText = findViewById(R.id.eta)
        altezzaEditText = findViewById(R.id.altezza)
        pesoEditText = findViewById(R.id.peso)
        objectiveSpinner = findViewById(R.id.spinnerOptions)

        // Initialize the spinner with the two options
        val options = arrayOf("Lose weight", "Enhance performance")
        objectiveSpinner.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, options)

        // Set up the button click listener
        val nextButton = findViewById<Button>(R.id.nextButton)
        nextButton.setOnClickListener {
            saveUserData()
        }
    }

    private fun saveUserData() {
        // Extract user input from the EditText fields and spinner
        val userNameString = userNameEditText.text.toString()
        val sessoString = sessoEditText.text.toString()
        val etaValue = etaEditText.text.toString().toIntOrNull() ?: 0
        val altezzaValue = altezzaEditText.text.toString().toIntOrNull() ?: 0
        val pesoValue = pesoEditText.text.toString().toIntOrNull() ?: 0
        val selectedObjective = objectiveSpinner.selectedItem.toString()

        // Assuming DB class has an insertData method to handle user data insertion
        DB(this).insertData(userNameString, sessoString, etaValue, altezzaValue, pesoValue, selectedObjective)

        // After saving the data, navigate to the next activity
        startActivity(Intent(this, BotMenuActivity::class.java))
    }
}
