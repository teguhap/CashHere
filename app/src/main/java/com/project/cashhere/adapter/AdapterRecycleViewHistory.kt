package com.project.cashhere.adapter

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.AuthFailureError
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import com.project.cashhere.dataclass.ListHistory
import com.project.cashhere.R
import com.project.cashhere.Sender
import org.json.JSONException
import org.json.JSONObject
import java.util.*


class AdapterRecycleViewHistory(val listData : List<ListHistory>) : RecyclerView.Adapter<AdapterRecycleViewHistory.ViewHolderView>() {

    inner class ViewHolderView(itemView : View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderView {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.bg_list_history,parent,false)
        return ViewHolderView(view)
    }

    override fun onBindViewHolder(holder: ViewHolderView, position: Int) {
        holder.itemView.apply {
            val kode = findViewById<TextView>(R.id.kodePesananHistory)
            val pesanan = findViewById<TextView>(R.id.listPesananHistory)
            val metodeBayar = findViewById<TextView>(R.id.tvMetodeBayarHistory)
            val totalBayar = findViewById<TextView>(R.id.tvTotalBayarHistory)
            val btnDelete = findViewById<Button>(R.id.btnDeleteHistory)




            val curItem = listData[position]
            kode.text = curItem.kode
            pesanan.text = curItem.pesanan
            metodeBayar.text = curItem.metodeBayar
            totalBayar.text = curItem.totalBayar

            val intent = Intent("delete_history")

            btnDelete.setOnClickListener {
                deleteHistoryData(curItem.kode)
                LocalBroadcastManager.getInstance(context).sendBroadcast(intent)
                Toast.makeText(context,"History Terhapus",Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun getItemCount(): Int {

        return  listData.size
    }

    fun deleteHistoryData(kode:String){
        val BASE_URL = "http://192.168.43.55/cash_here/index.php?op="
        val ACTION = BASE_URL+"history_delete&kode=$kode"

        val stringRequest = object : StringRequest(
            Method.GET,ACTION,
            Response.Listener<String>{

                    response ->
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