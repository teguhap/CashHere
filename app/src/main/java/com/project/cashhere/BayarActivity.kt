package com.project.cashhere

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.project.cashhere.adapter.AdapterRecycleViewBayar
import com.project.cashhere.dataclass.ListItem
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.util.*

class BayarActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bayar)

        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        window.statusBarColor = ContextCompat.getColor(this,R.color.white)
        window.setBackgroundDrawableResource(R.drawable.bg_dashboard);

//        val svBayar = findViewById<androidx.appcompat.widget.SearchView>(R.id.svBayar)
        val btnDropDownFood = findViewById<ImageView>(R.id.btnDropDown)
        val btnDropUpFood = findViewById<ImageView>(R.id.btnDropUp)
        val btnDropDownDrink = findViewById<ImageView>(R.id.btnDropDownDrink)
        val btnDropUpDrink = findViewById<ImageView>(R.id.btnDropUpDrink)
        val rvBayarFood = findViewById<RecyclerView>(R.id.rvFoodBayar)
        val rvBayarDrink = findViewById<RecyclerView>(R.id.rvDrinkBayar)
        val btnBackBayar = findViewById<ImageView>(R.id.btnBackBayar)
        val tvTotalBayar = findViewById<TextView>(R.id.tvTotalBayar)
        val spMetodeBayar = findViewById<Spinner>(R.id.spMetodeBayar)
        val btnBayar = findViewById<Button>(R.id.btnBayar)
        val cvTotalBayar = findViewById<ConstraintLayout>(R.id.cvTotalBayar)


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






        var totalbayar = 0
        val listPesenan = ArrayList<String>()

        mMessageReceiver =  object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val nama = intent?.getStringExtra("nama")
            val harga = intent?.getStringExtra("harga")
            totalbayar+=harga.toString().toInt()
            tvTotalBayar.text = totalbayar.toString()
            listPesenan.add(nama!!)
            Log.d("PESANAN",listPesenan.toString())

          }
        }

        mMessageReceiverKurang =  object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                val nama = intent?.getStringExtra("nama")
                val harga = intent?.getStringExtra("harga")
                totalbayar-=harga.toString().toInt()
                tvTotalBayar.text = totalbayar.toString()
                listPesenan.remove(nama!!)
                Log.d("Pesanan",listPesenan.toString())

            }
        }


        //Broadcas dari RecycleAdapter
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,
            IntentFilter("totalItemMenu")
        )

        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiverKurang,
            IntentFilter("kurangItemMenu")
        )




        btnBayar.setOnClickListener {

            val kode = (1000..9999).random()
            var listPesananToStruk = ""
           listPesenan.sort()
            for(i in 0 until listPesenan.size){
                listPesananToStruk +=listPesenan[i]+", \n"
            }

            addHistoryPesanan(kode.toString(),listPesananToStruk,spMetodeBayar.selectedItem.toString(),tvTotalBayar.text.toString())

            Intent(this,DisplayPesananActivity ::class.java).also {
                it.putExtra("kode",kode.toString())
                it.putExtra("listPesanan",listPesananToStruk)
                it.putExtra("totalBayar",tvTotalBayar.text.toString())
                it.putExtra("metodeBayar",spMetodeBayar.selectedItem.toString())
                startActivity(it)
                finish()
            }


        }



        cvTotalBayar.setOnClickListener {
            val rincianView = layoutInflater.inflate(R.layout.bg_rincian_pesanan,null)
            val tvRincianPesan = rincianView.findViewById<TextView>(R.id.tvRincianPesanan)
            val tvTotalRincian = rincianView.findViewById<TextView>(R.id.tvTotalRincian)

            listPesenan.sort()
            var listPesananToRincian = ""
            listPesananToRincian = ""
            for(i in 0 until listPesenan.size){
                listPesananToRincian +=listPesenan[i]+", \n"
            }

            tvRincianPesan.text = listPesananToRincian
            tvTotalRincian.text = tvTotalBayar.text.toString()

            val dialog = AlertDialog.Builder(this)
            dialog.setView(rincianView)
            dialog.setCancelable(true)
            dialog.create()

            dialog.show()



        }


    }

    lateinit var mMessageReceiver: BroadcastReceiver
    lateinit var mMessageReceiverKurang: BroadcastReceiver



    fun getFoodData(listFood : MutableList<ListItem>, displayListFood : MutableList<ListItem>){
        val queue = Volley.newRequestQueue(this)
        val url = "https://cashhere.kspkitasemua.xyz/index.php?op=food_view"

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
                listFood.sortBy { it.nama.toString() }
                displayListFood.sortBy { it.nama.toString() }
                val adapter  = AdapterRecycleViewBayar(displayListFood)
                rvFood.adapter = adapter
                rvFood.layoutManager = GridLayoutManager(this,2)
                rvFood.setHasFixedSize(true)


            }, {})
            queue.add(stringRequest)
    }


    fun getDrinkData(listDrink : MutableList<ListItem>, displayListDrink : MutableList<ListItem>){
        val queue = Volley.newRequestQueue(this)
        val url = "https://cashhere.kspkitasemua.xyz/index.php?op=drink_view"

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
                listDrink.sortBy { it.nama.toString() }
                displayListDrink.sortBy { it.nama.toString() }
                val adapter  = AdapterRecycleViewBayar(displayListDrink)
                rvDrink.adapter = adapter
                rvDrink.layoutManager = GridLayoutManager(this,2)
                rvDrink.setHasFixedSize(true)

            }, {})
        queue.add(stringRequest)
    }

    //FUNCTION MENAMBAH DATA BAYAR PESANAN F
    // OOD KE DATABASE
    fun addHistoryPesanan(kode:String,pesanan:String,metodeBayar:String,total : String){
        val BASE_URL = "https://cashhere.kspkitasemua.xyz/index.php?op="
        val ACTION = BASE_URL+"history_create&kode=$kode&pesanan=$pesanan&metode_bayar=$metodeBayar&total_bayar=$total"

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
    }
}