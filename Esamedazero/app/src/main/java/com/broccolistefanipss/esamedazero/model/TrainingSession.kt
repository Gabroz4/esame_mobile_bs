package com.broccolistefanipss.esamedazero.model

data class TrainingSession(
    val sessionId: Int,
    val userName: String,
    val sessionDate: String,
    val duration: Int,
    val trainingType: String
)