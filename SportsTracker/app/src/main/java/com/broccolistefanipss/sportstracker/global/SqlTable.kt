package com.broccolistefanipss.sportstracker.global

object SqlTable {
    val User = """
        CREATE TABLE IF NOT EXISTS User (
            userName TEXT PRIMARY KEY,
            password TEXT,
            sesso TEXT,
            eta INTEGER,
            altezza INTEGER,
            peso INTEGER,
            obiettivo TEXT
        )
    """.trimIndent() // pulizia dati in input

    val TrainingSessions = """
        CREATE TABLE IF NOT EXISTS TrainingSessions (
            sessionId INTEGER PRIMARY KEY AUTOINCREMENT,
            userName TEXT,
            sessionDate TEXT,
            duration INTEGER,
            trainingType TEXT,
            distance FLOAT,
            burntCalories INTEGER,
            FOREIGN KEY(userName) REFERENCES User(userName) ON DELETE CASCADE
        )
    """.trimIndent()

    val CalendarTraining = """
        CREATE TABLE IF NOT EXISTS CalendarTraining (
            userName TEXT,
            date TEXT,
            description TEXT,
            FOREIGN KEY(userName) REFERENCES User(userName) ON DELETE CASCADE
        )
    """.trimIndent()

    val Location = """
        CREATE TABLE IF NOT EXISTS Location (
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            trainingId INTEGER,
            latitude REAL,
            longitude REAL,
            timestamp INTEGER,
            FOREIGN KEY (trainingId) REFERENCES TrainingSessions(sessionId)
        )
    """.trimIndent()
}