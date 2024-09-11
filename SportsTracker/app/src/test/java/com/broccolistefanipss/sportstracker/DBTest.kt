package com.broccolistefanipss.sportstracker

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.broccolistefanipss.sportstracker.global.DB
import org.junit.After
import org.junit.Test
import org.junit.Assert.*
import org.junit.Before
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

/**
 Mockito: simula oggetti complessi (ad esempio DB) senza dover ricreare tutti i metodi a mano
 Roboelectric: testare componenti di Android (context, activity, sharedpreferences)
 Espresso: testing dell'UI
 */
@Config(manifest = Config.NONE)
@RunWith(RobolectricTestRunner::class)
class DBTest {

    private lateinit var context: Context
    private lateinit var db: DB

    @Before
    fun setup() {
        context = ApplicationProvider.getApplicationContext()
        db = DB(context)

    }

    @Test
    fun testInsertUser_success() {
        val result = db.insertUser("Giorgio", "1234", "Maschio", 25, 180, 75, "Migliorare resistenza")
        assertTrue(result) // Verifica che l'utente è stato inserito correttamente
        db.close()
    }

    @Test
    fun testInsertUser_alreadyExists() {
        // Inserisci lo stesso utente due volte
        db.insertUser("Giuseppe", "1234", "Maschio", 25, 180, 75, "Migliorare resistenza")
        val result = db.insertUser("Giuseppe", "1234", "Maschio", 25, 180, 75, "Migliorare resistenza")
        assertFalse(result) // Il secondo tentativo dovrebbe fallire
        db.close()
    }

    @Test
    fun testGetUserData_success() {
        db.insertUser("Alfredo", "1234", "Maschio", 25, 180, 75, "Dimagrire")
        val user = db.getUserData("Alfredo")
        assertNotNull(user)
        assertEquals("Alfredo", user?.userName)
        db.close()
    }

    @Test
    fun testInsertTrainingSession() {
        val db = DB(context)

        db.insertUser("Mauro", "1234", "Maschio", 25, 180, 75, "Dimagrire")

        // Inserisci un allenamento per un utente fittizio
        val sessionId = db.insertTrainingSession(
            userName = "Mauro",
            sessionDate = "10-09-2024",
            duration = 60,
            distance = 10.5f,
            trainingType = "corsa",
            burntCalories = 500
        )

        // Verifica che l'inserimento sia andato a buon fine
        assertTrue(sessionId > 0)

        // Recupera l'allenamento inserito
        val trainings = db.getUserTrainingSessions("Mauro")

        // Verifica che l'allenamento sia stato inserito correttamente
        assertEquals(1, trainings.size)
        assertEquals("corsa", trainings[0].trainingType)

        db.close()
    }

    @Test
    fun testDeleteTrainingSession() {
        val db = DB(context)

        // Inserisci un allenamento di test
        val sessionId = db.insertTrainingSession(
            userName = "Mauro",
            sessionDate = "12-09-2024",
            duration = 60,
            distance = 10.5f,
            trainingType = "corsa",
            burntCalories = 500
        )

        // Verifica che l'allenamento sia stato inserito
        assertTrue(sessionId > 0)

        // Rimuovi l'allenamento
        val success = db.deleteTrainingSession(sessionId.toInt())

        // Verifica che la rimozione sia andata a buon fine
        assertTrue(success)

        // Verifica che l'allenamento non esista più
        val trainings = db.getUserTrainingSessions("Mauro")
        assertTrue(trainings.isEmpty())

        db.close()
    }
    @Test
    fun testUpdateUser() {
        val db = DB(context)

        // Inserisci un utente di test
        db.insertUser("Lorenzo", "1234", "Maschio", 25, 180, 75, "Dimagrire")

        // Modifica i dati dell'utente
        val success = db.updateUser(
            currentUserName = "Lorenzo",
            newEta = 26,
            newAltezza = 182,
            newPeso = 78.5,
            newObiettivo = "Migliorare forma fisica",
            newSesso = "Femmina"
        )

        // Verifica che la modifica sia andata a buon fine
        assertTrue(success)

        // Recupera l'utente e verifica che i dati siano aggiornati
        val user = db.getUserData("Lorenzo")
        assertNotNull(user)
        assertEquals(26, user?.eta)
        assertEquals(182, user?.altezza)
        user?.peso?.let { assertEquals(78.5, it, 0.01) }
        assertEquals("Migliorare forma fisica", user?.obiettivo)
        assertEquals("Femmina", user?.sesso)

        db.close()
    }
    @After
    fun tearDown() {
        val db = DB(context)
        db.writableDatabase.use {
            it.execSQL("DELETE FROM TrainingSessions")
            it.execSQL("DELETE FROM Location")
            it.execSQL("DELETE FROM User")
            it.execSQL("DELETE FROM CalendarTraining")
        }
    }

}
