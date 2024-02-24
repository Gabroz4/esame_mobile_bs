package com.broccolistefanipss.esamedazero
import android.content.Intent
import android.media.session.MediaSessionManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.se.omapi.Session
import android.text.LoginFilter.UsernameFilterGMail
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.broccolistefanipss.esamedazero.activity.WelcomeActivity
import com.broccolistefanipss.esamedazero.global.DB
import com.broccolistefanipss.esamedazero.manager.SessionManager
import com.broccolistefanipss.esamedazero.model.Utente

class SplashActivity : AppCompatActivity() {


    private val loadingDelay: Long = 2000 // 2 seconds
    private val handler = Handler(Looper.getMainLooper())


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)





        delay()
    }

    private fun addUserData() {

    }

    private fun delay() {
        // Using Handler with lambda for delayed execution
        handler.postDelayed({
            val intent = Intent(this@SplashActivity, WelcomeActivity::class.java)
            startActivity(intent)
            finish()
        }, loadingDelay)
    }

    override fun onDestroy() {
        // Remove the callbacks when the activity is destroyed to avoid memory leaks
        handler.removeCallbacksAndMessages(null)
        super.onDestroy()
    }

}
