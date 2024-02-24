package com.broccolistefanipss.esamedazero.global

object SqlTable {
    const val Utente = "CREATE TABLE Utente " +
            " (userName TEXT PRIMARY KEY DEFAULT ''," +
            " Sesso TEXT DEFAULT ''," +
            " Eta INTEGER," +
            " Altezza INTEGER," +
            " Peso INTEGER," +
            " Obiettivo TEXT DEFAULT '')"
}
