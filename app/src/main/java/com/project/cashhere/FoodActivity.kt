package com.project.cashhere

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.*
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONArray
import org.json.JSONObject
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.AuthFailureError
import com.android.volley.Response
import com.android.volley.VolleyError
import com.project.cashhere.adapter.AdapterRecycleView
import com.project.cashhere.dataclass.ListItem
import org.json.JSONException
import java.util.*
import kotlin.collections.ArrayList

class FoodActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_food)

        window.decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR)
        window.statusBarColor = ContextCompat.getColor(this,R.color.white)
        window.setBackgroundDrawableResource(R.drawable.bg_dashboard);

        //INISIALISASI DARI LAYOUT
        val btnBack = findViewById<ImageView>(R.id.btnBackFood)
        val btnAdd = findViewById<ImageView>(R.id.btnAddItemFood)
        val btnCancel = findViewById<ImageView>(R.id.btnCancelAddItemFood)
        val btnSimpan = findViewById<Button>(R.id.btnSimpanFood)
        val svFood = findViewById<SearchView>(R.id.svFood)
        val rvFood = findViewById<RecyclerView>(R.id.rv_food)
        val llFood = findViewById<LinearLayout>(R.id.llAddItemFood)

        val slideInAnim = AnimationUtils.loadAnimation(this,R.anim.slide_in)



        //INISIALISASI DARI LAYOUT (Tambah Menu)
        val etKode = findViewById<EditText>(R.id.etKodeFood)
        val etNama = findViewById<EditText>(R.id.etNamaFood)
        val etHarga = findViewById<EditText>(R.id.etHargaFood)


//VIEW ITEM FOOD
        //ARRAY LIST ITEM FOOD
        val listFood = ArrayList<ListItem>()
        val displayListFood = ArrayList<ListItem>()

        //MENGAMBIL DATA ITEM FOOD DAN MEMASUKAN DATA KE ARRAYLIST
        //JUGA UNTUK MENAMPILKAN DATA KE RECYCLE_VIEW (LIST)
        getFoodData(listFood,displayListFood)

        //SEARCH VIEW ITEM
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


//ADD ITEM FOOD
        btnSimpan.setOnClickListener {
            val kode = etKode.text.toString()
            val nama = etNama.text.toString()
            val harga = etHarga.text.toString()

            addFoodData(kode,nama,harga)

            val kodeRandom = (100..999).random()
            etKode.setText(kodeRandom.toString())
            etNama.text.clear()
            etHarga.text.clear()

        }




        //BUTTON BACK DI TOOLBAR
        btnBack.setOnClickListener {
            onBackPressed()
            finish()
        }

        //BUTTON ADD DI TOOLBAR
        btnAdd.setOnClickListener {
            val kodeRandom = (100..999).random()
            etKode.setText(kodeRandom.toString())
            rvFood.visibility = View.GONE
            svFood.visibility = View.GONE
            llFood.visibility = View.VISIBLE
            llFood.animation = slideInAnim
            llFood.animation.start()
            btnAdd.visibility = View.GONE
            btnCancel.visibility = View.VISIBLE
            etNama.text.clear()
            etHarga.text.clear()
        }

        //BUTTON CANCEL DI TOOLBAR
        btnCancel.setOnClickListener {
            rvFood.visibility = View.VISIBLE
            svFood.visibility = View.VISIBLE
            llFood.visibility = View.GONE
            btnAdd.visibility = View.VISIBLE
            btnCancel.visibility = View.GONE

            listFood.clear()
            displayListFood.clear()
            getFoodData(listFood,displayListFood)
        }



        val mMessageReceiver = object : BroadcastReceiver(){
            override fun onReceive(context: Context?, intent: Intent?) {
                val kode = intent?.getStringExtra("kode")
                val nama = intent?.getStringExtra("nama")
                val harga = intent?.getStringExtra("harga")

                addFoodData(kode!!,nama!!,harga!!)
                getFoodData(listFood, displayListFood)
            }
        }

        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,
            IntentFilter("dataUpdate")
        )

    }



    //FUNCTION MENGAMBIL DATA ITEM FOOD DARI DATABASE
    fun getFoodData(listFood : MutableList<ListItem>, displayListFood : MutableList<ListItem>){
        val queue = Volley.newRequestQueue(this)
        val url = "https://cashhere.kspkitasemua.xyz/index.php?op=food_view"

        val stringRequest = StringRequest(Request.Method.GET,url,
            {
                response ->

                val rvFood = findViewById<RecyclerView>(R.id.rv_food)
                val strRespon = response.toString()
                val jsonObject = JSONObject(strRespon)
                val jsonArray:JSONArray = jsonObject.getJSONArray("food")
                listFood.clear()
                displayListFood.clear()
                for(i in 0 until jsonArray.length()){
                    val jsonInner : JSONObject = jsonArray.getJSONObject(i)
                    val kode = jsonInner.get("kode").toString()
                    val nama =  jsonInner.get("nama").toString()
                    val harga = jsonInner.get("harga").toString()
                    listFood.add(ListItem(kode,nama,harga))
                }

                displayListFood.addAll(listFood)
                displayListFood.sortBy { it.nama }
                val adapter  = AdapterRecycleView(displayListFood)
                rvFood.adapter = adapter
                rvFood.layoutManager = LinearLayoutManager(this)
                rvFood.setHasFixedSize(true)


            }, {})
            queue.add(stringRequest)
    }


    //FUNCTION MENAMBAH DATA ITEM FOOD KE DATABASE
    fun addFoodData(kode:String,nama:String,harga:String){
        val BASE_URL = "https://cashhere.kspkitasemua.xyz/index.php?op="
        val ACTION = BASE_URL+"food_create&kode=$kode&nama=$nama&harga=$harga"

        val stringRequest = object : StringRequest(Request.Method.GET,ACTION,
            Response.Listener<String>{ response ->
                try{
                    val obj = JSONObject(response)
                    Log.i("hasil",obj.getString("message"))
                }catch(e: JSONException){
                    e.printStackTrace()
                }
            },
            object : Response.ErrorListener{
                override fun onErrorResponse(error: VolleyError?) {
                    Log.e(
                        "hasil : ",error!!.message.toString()
                    )
                }
            }) {
            @Throws(AuthFailureError::class)
            override fun getParams(): MutableMap<String, String> {
                val params = HashMap<String,String>()
                return params
            }}

        Sender.instance!!.addToRequestQueue(stringRequest)

        Toast.makeText(this,"Data Berhasil Terimpan",Toast.LENGTH_SHORT).show()
    }






}