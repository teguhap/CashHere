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
import com.project.cashhere.adapter.AdapterRecycleViewDrink
import com.project.cashhere.dataclass.ListItem
import org.json.JSONException
import java.util.*
import kotlin.collections.ArrayList

class DrinkActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_drink)

        window.decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR)
        window.statusBarColor = ContextCompat.getColor(this,R.color.white)
        window.setBackgroundDrawableResource(R.drawable.bg_dashboard);

        //INISIALISASI DARI LAYOUT
        val btnBack = findViewById<ImageView>(R.id.btnBackDrink)
        val btnAdd = findViewById<ImageView>(R.id.btnAddItemDrink)
        val btnCancel = findViewById<ImageView>(R.id.btnCancelAddItemDrink)
        val btnSimpan = findViewById<Button>(R.id.btnSimpanDrink)
        val svDrink = findViewById<SearchView>(R.id.svDrink)
        val rvDrink = findViewById<RecyclerView>(R.id.rv_Drink)
        val llDrink = findViewById<LinearLayout>(R.id.llAddItemDrink)


        //INISIALISASI DARI LAYOUT (Tambah Menu)
        val etKode = findViewById<EditText>(R.id.etKodeDrink)
        val etNama = findViewById<EditText>(R.id.etNamaDrink)
        val etHarga = findViewById<EditText>(R.id.etHargaDrink)


//VIEW ITEM FOOD
        //ARRAY LIST ITEM FOOD
        val listDrink = ArrayList<ListItem>()
        val displayListDrink = ArrayList<ListItem>()

        //MENGAMBIL DATA ITEM FOOD DAN MEMASUKAN DATA KE ARRAYLIST
        //JUGA UNTUK MENAMPILKAN DATA KE RECYCLE_VIEW (LIST)
        getDrinkData(listDrink,displayListDrink)

        //SEARCH VIEW ITEM
        svDrink.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            @SuppressLint("NotifyDataSetChanged")
            override fun onQueryTextChange(newText: String?): Boolean {

                if(newText!!.isNotEmpty()){
                    displayListDrink.clear()
                    val search = newText.lowercase(Locale.getDefault())
                    listDrink.forEach {
                        if(it.nama.lowercase(Locale.getDefault()).contains(search)
                                ||it.kode.lowercase(Locale.getDefault()).contains(search)
                                ||it.harga.lowercase(Locale.getDefault()).contains(search)) {
                            displayListDrink.add(it)
                        }
                        rvDrink.adapter!!.notifyDataSetChanged()
                    }

                }else{
                    displayListDrink.clear()
                    displayListDrink.addAll(listDrink)
                    rvDrink.adapter!!.notifyDataSetChanged()

                }
                return true;
            }
        })


//ADD ITEM FOOD

        btnSimpan.setOnClickListener {
            val kode = etKode.text.toString()
            val nama = etNama.text.toString()
            val harga = etHarga.text.toString()

            addDrinkData(kode,nama,harga)

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
            rvDrink.visibility = View.GONE
            svDrink.visibility = View.GONE
            llDrink.visibility = View.VISIBLE
            btnAdd.visibility = View.GONE
            btnCancel.visibility = View.VISIBLE
            etNama.text.clear()
            etHarga.text.clear()
        }

        //BUTTON CANCEL DI TOOLBAR
        btnCancel.setOnClickListener {
            rvDrink.visibility = View.VISIBLE
            svDrink.visibility = View.VISIBLE
            llDrink.visibility = View.GONE
            btnAdd.visibility = View.VISIBLE
            btnCancel.visibility = View.GONE

            listDrink.clear()
            displayListDrink.clear()
            getDrinkData(listDrink,displayListDrink)
        }




        val mMessageReceiver = object : BroadcastReceiver(){
            override fun onReceive(context: Context?, intent: Intent?) {
                val kode = intent?.getStringExtra("kode")
                val nama = intent?.getStringExtra("nama")
                val harga = intent?.getStringExtra("harga")

                addDrinkData(kode!!,nama!!,harga!!)
            getDrinkData(listDrink, displayListDrink)

            }
        }

        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,
            IntentFilter("dataUpdate")
        )


    }




    //FUNCTION MENGAMBIL DATA ITEM FOOD DARI DATABASE
    fun getDrinkData(listDrink : MutableList<ListItem>, displayListDrink : MutableList<ListItem>){
        val queue = Volley.newRequestQueue(this)
        val url = "http://192.168.43.55/cash_here/index.php?op=drink_view"

        val stringRequest = StringRequest(Request.Method.GET,url,
            {          response ->

                val rvDrink = findViewById<RecyclerView>(R.id.rv_Drink)
                val strResponse = response.toString()
                val jsonObject = JSONObject(strResponse)
                val jsonArray:JSONArray = jsonObject.getJSONArray("drink")
                listDrink.clear()
                displayListDrink.clear()
                for(i in 0 until jsonArray.length()){
                    val jsonInner : JSONObject = jsonArray.getJSONObject(i)
                    val kode = jsonInner.get("kode").toString()
                    val nama =  jsonInner.get("nama").toString()
                    val harga = jsonInner.get("harga").toString()
                    listDrink.add(ListItem(kode,nama,harga))
                }

                displayListDrink.addAll(listDrink)
                displayListDrink.sortBy { it.nama }
                val adapter  = AdapterRecycleViewDrink(displayListDrink)
                rvDrink.adapter = adapter
                rvDrink.layoutManager = LinearLayoutManager(this)
                rvDrink.setHasFixedSize(true)

            }, {})
        queue.add(stringRequest)
    }


    //FUNCTION MENAMBAH DATA ITEM FOOD KE DATABASE
    fun addDrinkData(kode:String, nama:String, harga:String){
        val BASE_URL = "http://192.168.43.55/cash_here/index.php?op="
        val ACTION = BASE_URL+"drink_create&kode=$kode&nama=$nama&harga=$harga"

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