package com.project.cashhere

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.Toast
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
        val cvDessert = findViewById<CardView>(R.id.cv3)
        val cvBayar = findViewById<CardView>(R.id.cvBayar)
        val cvHistory = findViewById<CardView>(R.id.cvHistory)
        val btnSetting = findViewById<ImageView>(R.id.btnSetting)

        btnSetting.setOnClickListener {
            Toast.makeText(this,"Fitur Mendatang",Toast.LENGTH_SHORT).show()
        }


        cvFood.setOnClickListener {
            Intent(this,FoodActivity :: class.java).also {
                startActivity(it)
            }
        }
        cvDessert.setOnClickListener {
            Toast.makeText(this,"Fitur Mendatang",Toast.LENGTH_SHORT).show()
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

        cvHistory.setOnClickListener {
            Intent(this,HistoryActivity :: class.java).also {
                startActivity(it)
            }
        }


    }


}