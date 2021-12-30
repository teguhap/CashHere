package com.project.cashhere

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONArray
import org.json.JSONObject
import java.util.ArrayList

class HistoryActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)

        window.decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR)
        window.statusBarColor = ContextCompat.getColor(this,R.color.white)
        window.setBackgroundDrawableResource(R.drawable.bg_dashboard);

        val btnBack = findViewById<ImageView>(R.id.btnBackHistory)
        btnBack.setOnClickListener {
            onBackPressed()
            finish()
        }



        val listHistory = ArrayList<ListHistory>()

        getHistoryData(listHistory)


        mMessageReceiver =  object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                listHistory.clear()
                getHistoryData(listHistory)
            }
        }


        //Broadcast dari RecycleAdapter
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,
            IntentFilter("delete_history")
        )


    }

    lateinit var mMessageReceiver: BroadcastReceiver


    fun getHistoryData(listHistory : MutableList<ListHistory>){
        val queue = Volley.newRequestQueue(this)
        val url = "http://192.168.43.55/cash_here/index.php?op=history_view"

        val stringRequest = StringRequest(
            Request.Method.GET,url,
            {
                    response ->

                val rvHistory = findViewById<RecyclerView>(R.id.rvHistory)
                var listPesanan = ""
                val strRespon = response.toString()
                val jsonObject = JSONObject(strRespon)
                val jsonArray: JSONArray = jsonObject.getJSONArray("history")
                for(i in 0 until jsonArray.length()){
                    val jsonInner : JSONObject = jsonArray.getJSONObject(i)
                    val kode = jsonInner.get("kode").toString()
                    val pesanan =  jsonInner.get("pesanan").toString()
                    val metodeBayar= jsonInner.get("metode_bayar").toString()
                    val totalBayar= jsonInner.get("total_bayar").toString()
                    listHistory.add(ListHistory(kode,pesanan ,metodeBayar,totalBayar))
                }

                val adapter  = AdapterRecycleViewHistory(listHistory)
                rvHistory.adapter = adapter
                rvHistory.layoutManager = LinearLayoutManager(this)
                rvHistory.setHasFixedSize(true)


            }, {})
        queue.add(stringRequest)
    }
}