package com.broccolistefanipss.esamedazero
import android.content.Intent
import android.media.session.MediaSessionManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.se.omapi.Session
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.broccolistefanipss.esamedazero.activity.WelcomeActivity
import com.broccolistefanipss.esamedazero.global.DB
import com.broccolistefanipss.esamedazero.manager.SessionManager
import com.broccolistefanipss.esamedazero.model.Utente

class SplashActivity : AppCompatActivity() {

    private var user: Utente = Utente()
    private val loadingDelay: Long = 2000 // 2 seconds
    private val handler = Handler(Looper.getMainLooper())
    var db: DB?=null
    var session: SessionManager?=null

    var user.userName = findViewById<TextView>(R.id.userName)
    var userSesso = findViewById<TextView>(R.id.sesso)
    var userName = findViewById<TextView>(R.id.userName)
    var userName = findViewById<TextView>(R.id.userName)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        db = DB(this)
        db?.insertData() //inserire dati da schermata welcome
        db?.getData()
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
