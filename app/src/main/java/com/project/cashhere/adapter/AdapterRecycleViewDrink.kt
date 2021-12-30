package com.project.cashhere.adapter

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.inflate
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import com.project.cashhere.dataclass.ListItem
import com.project.cashhere.R
import com.project.cashhere.Sender
import org.json.JSONException
import org.json.JSONObject
import java.util.*
import java.util.logging.Handler


class AdapterRecycleViewDrink(val listData : List<ListItem>) : RecyclerView.Adapter<AdapterRecycleViewDrink.ViewHolderView>() {

    inner class ViewHolderView(itemView : View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderView {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.bg_list_item,parent,false)
        return ViewHolderView(view)
    }

    override fun onBindViewHolder(holder: ViewHolderView, position: Int) {
        holder.itemView.apply {
            val kode = findViewById<TextView>(R.id.tvKodeList)
            val nama = findViewById<TextView>(R.id.tvNamaList)
            val harga = findViewById<TextView>(R.id.tvHargaList)
            val btnUpdate = findViewById<Button>(R.id.btnUpdate)

            val curItem = listData[position]
            kode.text = curItem.kode
            nama.text = curItem.nama
            harga.text = curItem.harga






             //UPDATE DAN DELETE PROSSES
            val dialog = AlertDialog.Builder(context).create()
            val dialogView = inflate(context, R.layout.bg_update_delete,null)
            val etKodeUpdate = dialogView.findViewById<EditText>(R.id.etKodeUpdate)
            val etNamaUpdate = dialogView.findViewById<EditText>(R.id.etNamaUpdate)
            val etHargaUpdate = dialogView.findViewById<EditText>(R.id.etHargaUpdate)
            val btnDelete = dialogView.findViewById<Button>(R.id.btnDelete)
            val btnUpdateDialog = dialogView.findViewById<Button>(R.id.btnUpdate)

            dialog.setView(dialogView)
            dialog.setCancelable(true)

            etKodeUpdate.setText(kode.text)
            etNamaUpdate.setText(nama.text)
            etHargaUpdate.setText(harga.text)

            btnDelete.setOnClickListener {
                    deleteDrinkData(etKodeUpdate.text.toString())
                    dialog.dismiss()

                Intent("dataUpdate").also{
                    LocalBroadcastManager.getInstance(context).sendBroadcast(it)
                }
            }

            btnUpdateDialog.setOnClickListener {
                val kodeUpdate = etKodeUpdate.text.toString()
                val namaUpdate = etNamaUpdate.text.toString()
                val hargaUpdate = etHargaUpdate.text.toString()

                Intent("dataUpdate").also{
                    it.putExtra("kode",kodeUpdate)
                    it.putExtra("nama",namaUpdate)
                    it.putExtra("harga",hargaUpdate)
                    LocalBroadcastManager.getInstance(context).sendBroadcast(it)
                }
                deleteDrinkData(kodeUpdate)
                dialog.dismiss()
            }



            btnUpdate.setOnClickListener {
                dialog.show()
            }

        }
    }

    override fun getItemCount(): Int {

        return  listData.size
    }


    fun deleteDrinkData(kode: String) {
        val BASE_URL = "http://192.168.43.55/cash_here/index.php?op="
        val ACTION = BASE_URL+"drink_delete&kode=$kode"

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

    fun updateDrinkData(kode:String,nama:String,harga:String){
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
    }

}