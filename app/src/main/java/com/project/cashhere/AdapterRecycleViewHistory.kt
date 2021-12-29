package com.project.cashhere

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
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




            val curItem = listData[position]
            kode.text = curItem.kode
            pesanan.text = curItem.pesanan
            metodeBayar.text = curItem.metodeBayar
            totalBayar.text = curItem.totalBayar
        }
    }

    override fun getItemCount(): Int {

        return  listData.size
    }


}