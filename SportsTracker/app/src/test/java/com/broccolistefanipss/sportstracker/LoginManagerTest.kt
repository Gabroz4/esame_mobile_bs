package com.broccolistefanipss.sportstracker

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.broccolistefanipss.sportstracker.manager.LoginManager
import com.broccolistefanipss.sportstracker.global.DB
import com.broccolistefanipss.sportstracker.manager.SessionManager
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.*
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@Config(manifest = Config.NONE)
@RunWith(RobolectricTestRunner::class)
class LoginManagerTest {

    private lateinit var loginManager: LoginManager
    private lateinit var context: Context
    private lateinit var db: DB

    @Before
    fun setUp() {
        context = ApplicationProvider.getApplicationContext()
        loginManager = LoginManager(context)
        db = DB(context)

        db.insertUser("testUser", "testPassword", "Maschio", 25, 180, 75, "Migliorare resistenza")
    }

    @Test
    fun testSuccessfulLogin() {
        val result = loginManager.attemptLogin("testUser", "testPassword")
        assertTrue("Login riuscito", result)
    }

    @Test
    fun testFailedLogin() {
        val result = loginManager.attemptLogin("wrongUser", "wrongPassword")
        assertFalse("Login fallito", result)
    }

    @Test
    fun testLogout() {
        loginManager.attemptLogin("testUser", "testPassword")
        loginManager.logout()
        // Verifica logout
        val sessionManager = SessionManager(context)
        assertFalse("Logout utente", sessionManager.isLoggedIn)
    }

    @Test
    fun testSaveToSharedPreferences() {
        val result = loginManager.attemptLogin("testUser", "testPassword")
        assertTrue("Login riuscito", result)

        // Prendi username da SharedPreferences
        val sharedPreferences = context.getSharedPreferences("UserData", Context.MODE_PRIVATE)
        val savedUsername = sharedPreferences.getString("userName", null)

        // Controlla se è giusto
        assertTrue("username salvato dovrebbe essere uguale a quello loggato", savedUsername == "testUser")
    }
}