package com.project.cashhere

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.SearchView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONArray
import org.json.JSONObject
import java.util.*

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
        val btnBackBayar = findViewById<ImageView>(R.id.btnBackBayar)

        btnBackBayar.setOnClickListener {
            onBackPressed()
            finish()
        }


        //VIEW ITEM FOOD
        //ARRAY LIST ITEM FOOD
        val listFood = ArrayList<ListItem>()
        val displayListFood = ArrayList<ListItem>()

        //ARRAY LIST ITEM FOOD
        val listDrink = ArrayList<ListItem>()
        val displayListDrink = ArrayList<ListItem>()

        //MENGAMBIL DATA ITEM FOOD DAN MEMASUKAN DATA KE ARRAYLIST
        //JUGA UNTUK MENAMPILKAN DATA KE RECYCLE_VIEW (LIST)
        getFoodData(listFood,displayListFood)
        getDrinkData(listDrink,displayListDrink)

        //SEARCH VIEW ITEM
        svBayar.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            @SuppressLint("NotifyDataSetChanged")
            override fun onQueryTextChange(newText: String?): Boolean {

                if(newText!!.isNotEmpty()){
                    displayListFood.clear()
                    displayListDrink.clear()
                    val search = newText.lowercase(Locale.getDefault())
                    listFood.forEach {
                        if(it.nama.lowercase(Locale.getDefault()).contains(search)
                            ||it.kode.lowercase(Locale.getDefault()).contains(search)
                            ||it.harga.lowercase(Locale.getDefault()).contains(search)) {
                            displayListFood.add(it)
                        }
                        rvBayarFood.adapter!!.notifyDataSetChanged()

                    }

                    listDrink.forEach {
                        if(it.nama.lowercase(Locale.getDefault()).contains(search)
                            ||it.kode.lowercase(Locale.getDefault()).contains(search)
                            ||it.harga.lowercase(Locale.getDefault()).contains(search)) {
                            displayListDrink.add(it)
                        }
                        rvBayarDrink.adapter!!.notifyDataSetChanged()
                    }

                }else{
                    displayListFood.clear()
                    displayListFood.addAll(listFood)
                    displayListDrink.clear()
                    displayListDrink.addAll(listDrink)
                    rvBayarFood.adapter!!.notifyDataSetChanged()
                    rvBayarDrink.adapter!!.notifyDataSetChanged()
                }
                return true;
            }
        })




        btnDropDownFood.setOnClickListener {
            btnDropDownFood.visibility = View.GONE
            btnDropUpFood.visibility = View.VISIBLE
            rvBayarFood.visibility = View.VISIBLE
        }

        btnDropUpFood.setOnClickListener {
            btnDropDownFood.visibility = View.VISIBLE
            btnDropUpFood.visibility = View.GONE
            rvBayarFood.visibility = View.GONE

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


     fun getFoodData(listFood : MutableList<ListItem>,displayListFood : MutableList<ListItem>){
        val queue = Volley.newRequestQueue(this)
        val url = "http://192.168.43.55/cash_here/index.php?op=food_view"

        val stringRequest = StringRequest(
            Request.Method.GET,url,
            {
                response ->

                val rvFood = findViewById<RecyclerView>(R.id.rvFoodBayar)
                val strRespon = response.toString()
                val jsonObject = JSONObject(strRespon)
                val jsonArray: JSONArray = jsonObject.getJSONArray("food")

                for(i in 0 until jsonArray.length()){
                    val jsonInner : JSONObject = jsonArray.getJSONObject(i)
                    val kode = jsonInner.get("kode").toString()
                    val nama =  jsonInner.get("nama").toString()
                    val harga = jsonInner.get("harga").toString()
                    listFood.add(ListItem(kode,nama,harga))
                }

                displayListFood.addAll(listFood)
                val adapter  = AdapterRecycleViewBayar(displayListFood)
                rvFood.adapter = adapter
                rvFood.layoutManager = GridLayoutManager(this,2)
                rvFood.setHasFixedSize(true)


            }, {})
            queue.add(stringRequest)
    }


    fun getDrinkData(listDrink : MutableList<ListItem>, displayListDrink : MutableList<ListItem>){
        val queue = Volley.newRequestQueue(this)
        val url = "http://192.168.43.55/cash_here/index.php?op=drink_view"

        val stringRequest = StringRequest(Request.Method.GET,url,
            {          response ->

                val rvDrink = findViewById<RecyclerView>(R.id.rvDrinkBayar)
                val strResponse = response.toString()
                val jsonObject = JSONObject(strResponse)
                val jsonArray:JSONArray = jsonObject.getJSONArray("drink")

                for(i in 0 until jsonArray.length()){
                    val jsonInner : JSONObject = jsonArray.getJSONObject(i)
                    val kode = jsonInner.get("kode").toString()
                    val nama =  jsonInner.get("nama").toString()
                    val harga = jsonInner.get("harga").toString()
                    listDrink.add(ListItem(kode,nama,harga))
                }

                displayListDrink.addAll(listDrink)
                val adapter  = AdapterRecycleViewBayar(displayListDrink)
                rvDrink.adapter = adapter
                rvDrink.layoutManager = GridLayoutManager(this,2)
                rvDrink.setHasFixedSize(true)

            }, {})
        queue.add(stringRequest)
    }
}