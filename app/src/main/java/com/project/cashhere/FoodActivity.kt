package com.project.cashhere

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.ListView
import androidx.core.content.ContextCompat
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONArray
import org.json.JSONObject
import android.R.layout.simple_list_item_1
import android.widget.Toast

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

                val list : ArrayList<String> = ArrayList()
                val list_food = findViewById<ListView>(R.id.lv_food)

                val strRespon = response.toString()
                val jsonObject = JSONObject(strRespon)
                val jsonArray:JSONArray = jsonObject.getJSONArray("food")
                var data_food = ""

                for(i in 0 until jsonArray.length()){
                    val jsonInner : JSONObject = jsonArray.getJSONObject(i)
                    data_food = ""+jsonInner.get("kode")+","+jsonInner.get("nama")+","+jsonInner.get("harga")
                    list.add(data_food)
                }
                list_food.adapter = ArrayAdapter(this, simple_list_item_1,list)
            }, {})

            queue.add(stringRequest)



    }
}