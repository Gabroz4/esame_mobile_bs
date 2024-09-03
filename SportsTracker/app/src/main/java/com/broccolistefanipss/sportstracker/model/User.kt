package com.broccolistefanipss.sportstracker.model

data class User (
    val userName: String,
    val password: String,
    val sesso: String,
    val eta: Int,
    val altezza: Int,
    val peso: Double,
    val obiettivo: String
)