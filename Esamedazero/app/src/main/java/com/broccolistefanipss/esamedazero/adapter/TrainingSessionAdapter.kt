package com.broccolistefanipss.esamedazero.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.broccolistefanipss.esamedazero.databinding.ItemTrainingSessionBinding
import com.broccolistefanipss.esamedazero.model.TrainingSession

class TrainingSessionAdapter(
    var sessions: List<TrainingSession>,
    private val onDeleteClick: (Int) -> Unit // Funzione lambda per gestire il click di eliminazione
) : RecyclerView.Adapter<TrainingSessionAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // Crea un nuovo view binding per l'item
        val binding = ItemTrainingSessionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val session = sessions[position]
        holder.bind(session)

        // Aggiungi il listener per il bottone di eliminazione
        holder.binding.buttonDeleteTraining.setOnClickListener {
            onDeleteClick(session.sessionId) // Passa l'ID della sessione da eliminare
        }
    }

    override fun getItemCount(): Int = sessions.size

    inner class ViewHolder(val binding: ItemTrainingSessionBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(session: TrainingSession) {
            // Imposta i dati della sessione nei componenti della view
            binding.sessionIdTextView.text = "Id allenamento:  ${session.sessionId}"
            binding.sessionDateTextView.text = session.sessionDate
            binding.durationTextView.text = "${session.duration} sec - ${session.burntCalories} kcal"
            binding.trainingTypeTextView.text = session.trainingType
        }
    }

    // Funzione per aggiornare le sessioni
    fun updateSessions(newSessions: List<TrainingSession>) {
        sessions = newSessions
        notifyDataSetChanged() // Notifica l'adapter che i dati sono cambiati
    }
}