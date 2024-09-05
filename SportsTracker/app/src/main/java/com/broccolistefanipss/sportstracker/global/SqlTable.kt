package com.broccolistefanipss.sportstracker.global

object SqlTable {
    val User = """
        CREATE TABLE User (
            userName TEXT PRIMARY KEY,
            password TEXT,
            sesso TEXT,
            eta INTEGER,
            altezza INTEGER,
            peso INTEGER,
            obiettivo TEXT
        )
    """.trimIndent() //pulizia dati in input

    val TrainingSessions = """
    CREATE TABLE TrainingSessions (
        sessionId INTEGER PRIMARY KEY AUTOINCREMENT,
        userName TEXT,
        sessionDate TEXT,
        duration INTEGER,
        trainingType TEXT,
        burntCalories INTEGER,
        FOREIGN KEY(userName) REFERENCES User(userName) ON DELETE CASCADE
    )
""".trimIndent()

    val CalendarTraining = """
    CREATE TABLE CalendarTraining (
        userName TEXT,
        date TEXT,
        description TEXT,
        FOREIGN KEY(userName) REFERENCES User(userName) ON DELETE CASCADE
    )
""".trimIndent()
}