package com.project.cashhere

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat

class DashboardActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)
        window.decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR)
        window.statusBarColor = ContextCompat.getColor(this,R.color.white)

        val cvFood = findViewById<CardView>(R.id.cv1)
        val cvDrink = findViewById<CardView>(R.id.cv2)
        val cvBayar = findViewById<CardView>(R.id.cvBayar)

        cvFood.setOnClickListener {
            Intent(this,FoodActivity :: class.java).also {
                startActivity(it)
            }
        }

        cvDrink.setOnClickListener {
            Intent(this,DrinkActivity :: class.java).also {
                startActivity(it)
            }
        }

        cvBayar.setOnClickListener {
            Intent(this,BayarActivity :: class.java).also {
                startActivity(it)
            }
        }

    }
}