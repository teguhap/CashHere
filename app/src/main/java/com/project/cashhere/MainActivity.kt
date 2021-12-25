package com.project.cashhere

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        window.statusBarColor = ContextCompat.getColor(this,R.color.black)

        Handler().postDelayed({ Intent(this,DashboardActivity:: class.java).also {
            startActivity(it)
            finish()
        } },3000L)
    }

}