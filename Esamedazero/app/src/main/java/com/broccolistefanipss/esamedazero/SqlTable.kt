package com.broccolistefanipss.esamedazero

object SqlTable {//Bisogna aggiungere il sesso ma non swo di che tipo metterlo
    const val Utente = "CREATE TABLE Utente(ID INTEGER PRIMARY KEY AUTOINCREMENT, Nome TEXT DEFAULT '', Cognome TEXT DEFAULT '',Eta INTEGER, Altezza INTEGER, Peso INTEGER, Obiettivo TEXT DEFAULT '')"
}