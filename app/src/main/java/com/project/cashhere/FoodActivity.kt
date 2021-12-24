package com.project.cashhere

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONArray
import org.json.JSONObject
import androidx.recyclerview.widget.RecyclerView

class FoodActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_food)

        window.decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR)
        window.statusBarColor = ContextCompat.getColor(this,R.color.white)

        val btnBack = findViewById<ImageView>(R.id.btnBackFood)

        btnBack.setOnClickListener {
            onBackPressed()
            finish()
        }

        getFoodData()
    }

    fun getFoodData(){
        val queue = Volley.newRequestQueue(this)
        val url = "http://192.168.43.55/cash_here/index.php?op=food_view"

        val stringRequest = StringRequest(Request.Method.GET,url,
            {
                response ->

                val listFood = mutableListOf<ListItem>()
                val rvFood = findViewById<RecyclerView>(R.id.rv_food)

                val strRespon = response.toString()
                val jsonObject = JSONObject(strRespon)
                val jsonArray:JSONArray = jsonObject.getJSONArray("food")

                for(i in 0 until jsonArray.length()){
                    val jsonInner : JSONObject = jsonArray.getJSONObject(i)
                    val kode = jsonInner.get("kode").toString()
                    val nama =  jsonInner.get("nama").toString()
                    val harga = jsonInner.get("harga").toString()
                    listFood.add(ListItem(kode,nama,harga))
                }
                rvFood.adapter = AdapterRecycleView(listFood)
                rvFood.layoutManager = LinearLayoutManager(this)
                rvFood.setHasFixedSize(true)
            }, {})

            queue.add(stringRequest)



    }
}