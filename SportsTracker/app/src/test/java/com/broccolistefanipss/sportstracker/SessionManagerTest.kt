package com.broccolistefanipss.sportstracker

import android.content.Context
import android.content.SharedPreferences
import com.broccolistefanipss.sportstracker.manager.SessionManager
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.*
import org.mockito.Mock
import org.mockito.MockitoAnnotations

class SessionManagerTest {

    private lateinit var sessionManager: SessionManager

    @Mock
    private lateinit var sharedPreferences: SharedPreferences

    @Mock
    private lateinit var editor: SharedPreferences.Editor

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        `when`(sharedPreferences.edit()).thenReturn(editor)

        // simulazione del contesto
        val context = mock(Context::class.java)
        `when`(context.getSharedPreferences(SessionManager.PrefName, Context.MODE_PRIVATE)).thenReturn(sharedPreferences)

        sessionManager = SessionManager(context)
    }

    @Test
    fun `test isLoggedIn when not logged in`() {
        `when`(sharedPreferences.getBoolean(SessionManager.KeyIsLoggedIn, false)).thenReturn(false)
        assert(!sessionManager.isLoggedIn)
    }

    @Test
    fun `test setLogin updates login status`() {
        sessionManager.setLogin(true)
        verify(editor).putBoolean(SessionManager.KeyIsLoggedIn, true)
        verify(editor).apply()
    }

    @Test
    fun `test userName getter and setter`() {
        val testUsername = "testUser"
        sessionManager.userName = testUsername
        verify(editor).putString(SessionManager.KeyUserName, testUsername)
        verify(editor).apply()

        `when`(sharedPreferences.getString(SessionManager.KeyUserName, null)).thenReturn(testUsername)
        assert(sessionManager.userName == testUsername)
    }

    @Test
    fun `test userName returns null when not set`() {
        `when`(sharedPreferences.getString(SessionManager.KeyUserName, null)).thenReturn(null)
        assert(sessionManager.userName == null)
    }
}

