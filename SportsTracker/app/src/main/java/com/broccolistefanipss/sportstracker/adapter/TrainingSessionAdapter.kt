package com.broccolistefanipss.sportstracker.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.broccolistefanipss.sportstracker.databinding.ItemTrainingSessionBinding
import com.broccolistefanipss.sportstracker.model.TrainingSession
import com.broccolistefanipss.sportstracker.R

//TODO: occhio a come vengono salvate le kcal e distanza, fa cose strane nel salvataggio

class TrainingSessionAdapter(
    private val context: Context,
    private var sessions: List<TrainingSession>,
    private val onDeleteClick: (Int) -> Unit // gestione del click di eliminazione
) : RecyclerView.Adapter<TrainingSessionAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // Crea un nuovo view binding
        val binding = ItemTrainingSessionBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val session = sessions[position]
        holder.bind(session)

        holder.binding.buttonDeleteTraining.setOnClickListener {
            onDeleteClick(session.sessionId) //Id dell'allenamento da eliminare
        }
    }

    override fun getItemCount(): Int = sessions.size

    inner class ViewHolder(val binding: ItemTrainingSessionBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(session: TrainingSession) {
            // Imposta i dati della sessione nei componenti della view
            binding.sessionIdTextView.text = context.getString(R.string.id_allenamento_view, session.sessionId)
            binding.sessionDateTextView.text = session.sessionDate
            binding.durationTextView.text = context.getString(R.string.durata_e_calorie, session.duration , session.sessionId, session.distance)
            binding.trainingTypeTextView.text = session.trainingType
        }
    }

}