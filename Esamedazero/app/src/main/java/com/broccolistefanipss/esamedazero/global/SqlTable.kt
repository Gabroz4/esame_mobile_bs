package com.broccolistefanipss.esamedazero.global

object SqlTable {
    val User = """
        CREATE TABLE User (
            userName TEXT PRIMARY KEY,
            Sesso TEXT,
            Eta INTEGER,
            Altezza INTEGER,
            Peso INTEGER,
            Obiettivo TEXT
        )
    """.trimIndent() //pulizia dati in input

    val TrainingSessions = """
        CREATE TABLE TrainingSessions (
            sessionId INTEGER PRIMARY KEY AUTOINCREMENT,
            userName TEXT,
            sessionDate TEXT,
            duration INTEGER,
            trainingType TEXT,
            FOREIGN KEY(userName) REFERENCES User(userName) ON DELETE CASCADE
        )
    """.trimIndent()
}
