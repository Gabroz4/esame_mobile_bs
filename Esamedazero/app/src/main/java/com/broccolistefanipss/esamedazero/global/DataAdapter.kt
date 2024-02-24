package com.broccolistefanipss.esamedazero.global

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.broccolistefanipss.esamedazero.R
import com.broccolistefanipss.esamedazero.model.Utente

class DataAdapter(private val dataList: List<Utente>) : RecyclerView.Adapter<DataAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val userNameTextView: TextView = itemView.findViewById(R.id.userName)
        val sessoTextView: TextView = itemView.findViewById(R.id.sesso)
        val etaTextView: TextView = itemView.findViewById(R.id.eta)
        val altezzaTextView: TextView = itemView.findViewById(R.id.altezza)
        val pesoTextView: TextView = itemView.findViewById(R.id.peso)
        val obiettivoTextView: TextView = itemView.findViewById(R.id.obiettivo)
        // Add other TextViews for other columns
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.activity_welcome, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = dataList[position]
        holder.userNameTextView.text = currentItem.userName
        holder.sessoTextView.text = currentItem.sesso
        holder.etaTextView.text = currentItem.eta.toString()
        holder.altezzaTextView.text = currentItem.altezza.toString()
        holder.pesoTextView.text = currentItem.peso.toString()
        holder.obiettivoTextView.text = currentItem.obiettivo
        // Bind other data to TextViews
    }

    override fun getItemCount(): Int {
        return dataList.size
    }
}
