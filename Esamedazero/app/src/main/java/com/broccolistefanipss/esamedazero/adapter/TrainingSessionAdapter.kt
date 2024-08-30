package com.broccolistefanipss.esamedazero.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.broccolistefanipss.esamedazero.databinding.ItemTrainingSessionBinding
import com.broccolistefanipss.esamedazero.model.TrainingSession

// Definisce un adapter per RecyclerView che gestisce oggetti di tipo TrainingSession.
class TrainingSessionAdapter(var sessions: List<TrainingSession>) : RecyclerView.Adapter<TrainingSessionAdapter.ViewHolder>() {

    // Crea nuove viste (invocato dal layout manager del RecyclerView)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // Crea un nuovo view binding per l'item. Questo permette di accedere facilmente ai componenti dell'item senza usare findViewById.
        val binding = ItemTrainingSessionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    // Sostituisce il contenuto di una vista (invocato dal layout manager del RecyclerView)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // Ottiene l'elemento dalla tua dataset alle posizione specificata e sostituisce il contenuto della vista con tale elemento
        val session = sessions[position]
        with(holder.binding) {
            // Imposta il testo per la data della sessione usando i dati dell'oggetto TrainingSession
            sessionDateTextView.text = session.sessionDate
            // Imposta il testo per la durata della sessione, aggiungendo "min" per chiarire che si tratta di minuti
            durationTextView.text = "${session.duration} sec - ${session.burntCalories} kcal"
            // Imposta il testo per il tipo di allenamento usando i dati dell'oggetto TrainingSession
            trainingTypeTextView.text = session.trainingType
        }
    }

    // Ritorna la dimensione della tua dataset (invocato dal layout manager del RecyclerView)
    override fun getItemCount() = sessions.size

    // Fornisce un riferimento alle viste per ogni item di dati.
    // Complesse strutture dati possono richiedere più di un view per item, e
    // si accede a ciascuna delle viste attraverso un campo di questo ViewHolder.
    class ViewHolder(val binding: ItemTrainingSessionBinding) : RecyclerView.ViewHolder(binding.root)
}
