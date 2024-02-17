package com.broccolistefanipss.esamedazero

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.broccolistefanipss.esamedazero.activity.WelcomeActivity

class MainActivity : AppCompatActivity() {
    private var delayHandler : Handler? = null
    private val loading_delay: Long = 2000 //2 sec
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        delayHandler?.postDelayed(mRunnable, loading_delay)

    }
    private val mRunnable : Runnable = Runnable {
        val intent = Intent(this, WelcomeActivity::class.java)
        startActivity(intent)
        finish()
    }
    override fun onDestroy() {
        super.onDestroy()
        delayHandler?.removeCallbacks(mRunnable)
    }
}