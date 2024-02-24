package com.broccolistefanipss.esamedazero.model

import android.widget.TextView

data class Utente(
    val userName: String,
    val sesso: String,
    val eta: Int,
    val altezza: Int,
    val peso: Double,
    val obiettivo: String
)