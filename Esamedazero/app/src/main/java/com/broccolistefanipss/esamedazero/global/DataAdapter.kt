package com.broccolistefanipss.esamedazero.global

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.broccolistefanipss.esamedazero.R
import com.broccolistefanipss.esamedazero.model.Utente // Assuming you have a Utente model class

class DataAdapter(private val dataList: List<Utente>) : RecyclerView.Adapter<DataAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nomeTextView: TextView = itemView.findViewById(R.id.nomeTextView)
        // Add other TextViews for other columns
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.fragment_home, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = dataList[position]
        holder.nomeTextView.text = currentItem.username
        // Bind other data to TextViews
    }

    override fun getItemCount(): Int {
        return dataList.size
    }
}