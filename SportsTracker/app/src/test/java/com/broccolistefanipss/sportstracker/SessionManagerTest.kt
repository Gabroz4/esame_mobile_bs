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

    private val prefName = "AndroidLoginPref"
    private val keyUserName = "user_name"
    private val keyIsLoggedIn = "IsLoggedIn"


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
        `when`(context.getSharedPreferences(prefName, Context.MODE_PRIVATE)).thenReturn(sharedPreferences)

        sessionManager = SessionManager(context)
    }

    @Test
    fun testIsLoggedInWhenNotLoggedIn() {
        `when`(sharedPreferences.getBoolean(keyIsLoggedIn, false)).thenReturn(false)
        assert(!sessionManager.isLoggedIn)
    }

    @Test
    fun testSetLoginUpdatesLoginStatus() {
        sessionManager.setLogin(true)
        verify(editor).putBoolean(keyIsLoggedIn, true)
        verify(editor).apply()
    }

    @Test
    fun testUserNameGetterAndSetter() {
        val testUsername = "testUser"
        sessionManager.userName = testUsername
        verify(editor).putString(keyUserName, testUsername)
        verify(editor).apply()

        `when`(sharedPreferences.getString(keyUserName, null)).thenReturn(testUsername)
        assert(sessionManager.userName == testUsername)
    }

    @Test
    fun testUserNameReturnsNullWhenNotSet() {
        `when`(sharedPreferences.getString(keyUserName, null)).thenReturn(null)
        assert(sessionManager.userName == null)
    }
}

