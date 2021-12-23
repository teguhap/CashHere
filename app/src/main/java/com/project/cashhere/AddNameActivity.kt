package com.project.cashhere

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.core.content.ContextCompat

class AddNameActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_name)

        window.statusBarColor = ContextCompat.getColor(this,R.color.black)
        val btnSimpan = findViewById<Button>(R.id.btnAddName)

        btnSimpan.setOnClickListener {
            Intent(this,DashboardActivity :: class.java).also {
                startActivity(it)
            }
        }
    }
}