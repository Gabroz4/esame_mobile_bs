package com.broccolistefanipss.esamedazero.activity

import android.os.Bundle
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

        // Inizializzazione del database e session manager
        db = DB(this)
        sessionManager = SessionManager(this)

        // Ottieni i dati dell'utente corrente
        val currentUserName = sessionManager.userName ?: ""
        val user = db.getUserData(currentUserName)

        if (user != null) {
            binding.userName.text = currentUserName
            binding.editAge.setText(user.eta.toString() ?: "")
            binding.editHeight.setText(user.altezza.toString() ?: "")
            binding.editWeight.setText(user.peso.toString() ?: "")
            binding.editObjective.setSelection(getObjectiveIndex(user.obiettivo ?: ""))
        } else {
            Toast.makeText(this, "Errore: utente non trovato", Toast.LENGTH_SHORT).show()
            finish() // Chiude l'activity se l'utente non è trovato
        }


        // Gestisce il click sul pulsante "Salva Modifiche"
        binding.saveButton.setOnClickListener {
            // Recupera i dati modificati dai campi di input
            val newUserName = binding.userName.text.toString()
            val newAge = binding.editAge.text.toString().toIntOrNull()
            val newHeight = binding.editHeight.text.toString().toIntOrNull()
            val newWeight = binding.editWeight.text.toString().toDoubleOrNull()
            val newObjective = binding.editObjective.selectedItem.toString()


            // Aggiorna i dati nel database
            val isUpdated = db.updateUser(
                currentUserName = currentUserName,
                //newUserName = newUserName,
                newEta = newAge,
                newAltezza = newHeight,
                newPeso = newWeight,
                newObiettivo = newObjective
            )

            // Mostra un messaggio di conferma
            if (isUpdated) {
                Toast.makeText(this, "Dati aggiornati con successo", Toast.LENGTH_SHORT).show()
                // Aggiorna il nome utente nella sessione, se modificato
                sessionManager.userName = userName
                finish() // Chiudi l'activity e torna alla precedente
            } else {
                Toast.makeText(this, "Nessuna modifica rilevata", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Funzione per ottenere l'indice dell'obiettivo selezionato (da adattare alla tua implementazione del Spinner)
    private fun getObjectiveIndex(objective: String): Int {
        val objectivesArray = resources.getStringArray(R.array.objectives_array)
        return objectivesArray.indexOf(objective)
    }
}
