package com.project.cashhere

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONArray
import org.json.JSONObject
import androidx.recyclerview.widget.RecyclerView
import java.util.*
import kotlin.collections.ArrayList

class FoodActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_food)

        window.decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR)
        window.statusBarColor = ContextCompat.getColor(this,R.color.white)
        window.setBackgroundDrawableResource(R.drawable.bg_dashboard);

        val btnBack = findViewById<ImageView>(R.id.btnBackFood)
        val svFood = findViewById<SearchView>(R.id.svFood)
        val rvFood = findViewById<RecyclerView>(R.id.rv_food)

        btnBack.setOnClickListener {
            onBackPressed()
            finish()
        }

        val listFood = ArrayList<ListItem>()
        val displayListFood = ArrayList<ListItem>()

        getFoodData(listFood,displayListFood)

        svFood.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            @SuppressLint("NotifyDataSetChanged")
            override fun onQueryTextChange(newText: String?): Boolean {

                if(newText!!.isNotEmpty()){
                    displayListFood.clear()
                    val search = newText.lowercase(Locale.getDefault())
                    listFood.forEach {
                        if(it.nama.lowercase(Locale.getDefault()).contains(search)
                                ||it.kode.lowercase(Locale.getDefault()).contains(search)
                                ||it.harga.lowercase(Locale.getDefault()).contains(search)) {
                            displayListFood.add(it)
                        }
                        rvFood.adapter!!.notifyDataSetChanged()
                    }

                }else{
                    displayListFood.clear()
                    displayListFood.addAll(listFood)
                    rvFood.adapter!!.notifyDataSetChanged()

                }
                return true;
            }
        })
    }



    fun getFoodData(listFood : MutableList<ListItem>,displayListFood : MutableList<ListItem>){
        val queue = Volley.newRequestQueue(this)
        val url = "http://192.168.43.55/cash_here/index.php?op=food_view"

        val stringRequest = StringRequest(Request.Method.GET,url,
            {
                response ->

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

                displayListFood.addAll(listFood)
                val adapter  = AdapterRecycleView(displayListFood)
                rvFood.adapter = adapter
                rvFood.layoutManager = LinearLayoutManager(this)
                rvFood.setHasFixedSize(true)

            }, {})
            queue.add(stringRequest)
    }
}