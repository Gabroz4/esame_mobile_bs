package com.broccolistefanipss.esamedazero.activity

import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.broccolistefanipss.esamedazero.R
import com.broccolistefanipss.esamedazero.databinding.ActivityEditUserBinding
import com.broccolistefanipss.esamedazero.global.DB
import com.broccolistefanipss.esamedazero.manager.SessionManager
import com.broccolistefanipss.esamedazero.manager.SharedPrefs.Companion.userName

class EditUserActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditUserBinding
    private lateinit var db: DB
    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = DB(this)
        sessionManager = SessionManager(this)

        setupObjectiveSpinner()
        setupSexSpinner()
        loadUserData()
        setupSaveButton()
    }

    private fun setupObjectiveSpinner() {
        val objectivesArray = resources.getStringArray(R.array.objectives_array)
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, objectivesArray)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.editObjective.adapter = adapter
    }

    private fun setupSexSpinner() {
        val sexArray = resources.getStringArray(R.array.sesso_array)
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, sexArray)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.editSex.adapter = adapter
    }

    private fun loadUserData() {
        val currentUserName = sessionManager.userName ?: ""
        val user = db.getUserData(currentUserName)

        if (user != null) {
            Log.d("EditUserActivity", "Loading user data: $user")
            binding.userName.text = currentUserName
            binding.editAge.setText(user.eta?.toString() ?: "")
            binding.editHeight.setText(user.altezza?.toString() ?: "")
            binding.editWeight.setText(user.peso?.toString() ?: "")
            binding.editObjective.setSelection(getObjectiveIndex(user.obiettivo ?: ""))
            binding.editSex.setSelection(getSexIndex(user.sesso ?: ""))
        } else {
            Log.e("EditUserActivity", "User not found: $currentUserName")
            Toast.makeText(this, "Errore: utente non trovato", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    private fun setupSaveButton() {
        binding.saveButton.setOnClickListener {
            if (validateInputs()) {
                saveUserData()
            }
        }
    }

    private fun validateInputs(): Boolean {
        if (binding.editAge.text.isNullOrEmpty() ||
            binding.editHeight.text.isNullOrEmpty() ||
            binding.editWeight.text.isNullOrEmpty()
        ) {
            Toast.makeText(this, "Tutti i campi devono essere compilati", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    private fun saveUserData() {
        val currentUserName = sessionManager.userName ?: ""
        val newAge = binding.editAge.text.toString().toIntOrNull()
        val newHeight = binding.editHeight.text.toString().toIntOrNull()
        val newWeight = binding.editWeight.text.toString().toDoubleOrNull()
        val newObjective = binding.editObjective.selectedItem.toString()
        val newSex = binding.editSex.selectedItem.toString()

        Log.d("EditUserActivity", "Attempting to update user: $currentUserName")
        Log.d("EditUserActivity", "New data: Age=$newAge, Height=$newHeight, Weight=$newWeight, Objective=$newObjective, Sex=$newSex")

        val isUpdated = try {
            db.updateUser(
                currentUserName = currentUserName,
                newEta = newAge,
                newAltezza = newHeight,
                newPeso = newWeight,
                newObiettivo = newObjective,
                newSesso = newSex
            )
        } catch (e: Exception) {
            Log.e("EditUserActivity", "Error updating user data", e)
            Toast.makeText(this, "Errore nell'aggiornamento dei dati: ${e.message}", Toast.LENGTH_LONG).show()
            false
        }

        Log.d("EditUserActivity", "Update result: $isUpdated")

        if (isUpdated) {
            Toast.makeText(this, "Dati aggiornati con successo", Toast.LENGTH_SHORT).show()
            sessionManager.userName = currentUserName
            setResult(RESULT_OK)
            finish()
        } else {
            Toast.makeText(this, "Nessuna modifica rilevata", Toast.LENGTH_SHORT).show()
        }
    }

    private fun getObjectiveIndex(objective: String): Int {
        val objectivesArray = resources.getStringArray(R.array.objectives_array)
        return objectivesArray.indexOf(objective)
    }

    private fun getSexIndex(sex: String): Int {
        val sexArray = resources.getStringArray(R.array.sesso_array)
        return sexArray.indexOf(sex)
    }
}