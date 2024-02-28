package com.broccolistefanipss.esamedazero.global

object SqlTable {
    val Utente = """
        CREATE TABLE Utente (
            userName TEXT PRIMARY KEY,
            Sesso TEXT,
            Eta INTEGER,
            Altezza INTEGER,
            Peso INTEGER,
            Obiettivo TEXT
        )
    """.trimIndent()

    val TrainingSessions = """
        CREATE TABLE TrainingSessions (
            sessionId INTEGER PRIMARY KEY AUTOINCREMENT,
            userName TEXT,
            sessionDate TEXT,
            duration INTEGER,
            trainingType TEXT,
            FOREIGN KEY(userName) REFERENCES Utente(userName) ON DELETE CASCADE
        )
    """.trimIndent()
}
