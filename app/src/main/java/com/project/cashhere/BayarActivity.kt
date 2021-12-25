package com.project.cashhere

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.SearchView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.util.ArrayList

class BayarActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bayar)

        window.decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR)
        window.statusBarColor = ContextCompat.getColor(this,R.color.white)
        window.setBackgroundDrawableResource(R.drawable.bg_dashboard);

        val svBayar = findViewById<androidx.appcompat.widget.SearchView>(R.id.svBayar)
        val btnDropDownFood = findViewById<ImageView>(R.id.btnDropDown)
        val btnDropUpFood = findViewById<ImageView>(R.id.btnDropUp)
        val btnDropDownDrink = findViewById<ImageView>(R.id.btnDropDownDrink)
        val btnDropUpDrink = findViewById<ImageView>(R.id.btnDropUpDrink)
        val rvBayarFood = findViewById<RecyclerView>(R.id.rvFoodBayar)
        val rvBayarDrink = findViewById<RecyclerView>(R.id.rvDrinkBayar)


        val listFood = arrayListOf(
            ListItem("101","Nasi Goreng","13000"),
            ListItem("101","Nasi Goreng","13000"),
            ListItem("101","Nasi Goreng","13000"),
            ListItem("101","Nasi Goreng","13000"),
            ListItem("101","Nasi Goreng","13000"),
            ListItem("101","Nasi Goreng","13000"),
            ListItem("101","Nasi Goreng","13000")
        )

        rvBayarFood.adapter = AdapterRecycleViewBayar(listFood)
        rvBayarFood.layoutManager = GridLayoutManager(this,2)
        rvBayarFood.setHasFixedSize(true)

        rvBayarDrink.adapter = AdapterRecycleViewBayar(listFood)
        rvBayarDrink.layoutManager = GridLayoutManager(this,2)
        rvBayarDrink.setHasFixedSize(true)


        btnDropDownFood.setOnClickListener {
            rvBayarFood.visibility = View.VISIBLE
            btnDropDownFood.visibility = View.GONE
            btnDropUpFood.visibility = View.VISIBLE
        }

        btnDropUpFood.setOnClickListener {
            rvBayarFood.visibility = View.GONE
            btnDropDownFood.visibility = View.VISIBLE
            btnDropUpFood.visibility = View.GONE

        }

        btnDropDownDrink.setOnClickListener {
            rvBayarDrink.visibility = View.VISIBLE
            btnDropDownDrink.visibility = View.GONE
            btnDropUpDrink.visibility = View.VISIBLE
        }

        btnDropUpDrink.setOnClickListener {
            rvBayarDrink.visibility = View.GONE
            btnDropDownDrink.visibility = View.VISIBLE
            btnDropUpDrink.visibility = View.GONE
        }



    }
}