package com.broccolistefanipss.esamedazero.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.broccolistefanipss.esamedazero.R
import com.broccolistefanipss.esamedazero.model.User

// Definisce un adapter per RecyclerView che gestisce oggetti di tipo User.
class DataAdapter(private val dataList: List<User>) : RecyclerView.Adapter<DataAdapter.ViewHolder>() {

    // Definisce il ViewHolder che fornisce un riferimento alle viste per ogni elemento dati.
    // È una buona pratica per migliorare le performance del RecyclerView.
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // Inizializza le variabili per ogni elemento dell'interfaccia utente presente nell'item della lista.
        // Utilizza findViewById per trovare ogni componente nell'itemView.
        val userNameTextView: TextView = itemView.findViewById(R.id.userName)
        val sessoTextView: TextView = itemView.findViewById(R.id.sesso)
        val etaTextView: TextView = itemView.findViewById(R.id.eta)
        val altezzaTextView: TextView = itemView.findViewById(R.id.altezza)
        val pesoTextView: TextView = itemView.findViewById(R.id.peso)
        // val obiettivoTextView: TextView = itemView.findViewById(R.id.obiettivo)
        // Puoi aggiungere altri TextView per altre colonne se necessario.
    }

    // Crea nuove viste (invocato dal layout manager del RecyclerView).
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // Infla il layout dell'item usando LayoutInflater.
        // Il layout activity_welcome viene utilizzato qui, assicurati che sia corretto.
        val view = LayoutInflater.from(parent.context).inflate(R.layout.activity_welcome, parent, false)
        return ViewHolder(view)
    }

    // Sostituisce il contenuto di una vista (invocato dal layout manager).
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // Ottiene l'elemento dalla tua lista di dati alla posizione specificata e sostituisce il contenuto della vista con quell'elemento.
        val currentItem = dataList[position]
        holder.userNameTextView.text = currentItem.userName
        holder.sessoTextView.text = currentItem.sesso
        holder.etaTextView.text = currentItem.eta.toString()
        holder.altezzaTextView.text = currentItem.altezza.toString()
        holder.pesoTextView.text = currentItem.peso.toString()
        // holder.obiettivoTextView.text = currentItem.obiettivo
        // Imposta il testo per gli altri TextView come sopra.
    }

    // Ritorna la dimensione della lista di dati (invocato dal layout manager).
    override fun getItemCount(): Int {
        return dataList.size
    }
}