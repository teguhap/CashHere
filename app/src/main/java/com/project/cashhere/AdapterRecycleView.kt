package com.project.cashhere

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView


class AdapterRecycleView(val listData : List<ListItem>) : RecyclerView.Adapter<AdapterRecycleView.ViewHolderView>() {

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

            btnUpdate.setOnClickListener {
                Toast.makeText(context,"Fitur sedang dibuat",Toast.LENGTH_SHORT).show()
            }

            val curItem = listData[position]
            kode.text = curItem.kode
            nama.text = curItem.nama
            harga.text = curItem.harga
        }
    }

    override fun getItemCount(): Int {

        return  listData.size
    }
}