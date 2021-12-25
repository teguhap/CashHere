package com.project.cashhere

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import java.util.*


class AdapterRecycleViewBayar(val listData : List<ListItem>) : RecyclerView.Adapter<AdapterRecycleViewBayar.ViewHolderView>() {

    inner class ViewHolderView(itemView : View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderView {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.bg_list_item_bayar,parent,false)
        return ViewHolderView(view)
    }

    override fun onBindViewHolder(holder: ViewHolderView, position: Int) {
        holder.itemView.apply {
            val tvNama = findViewById<TextView>(R.id.tvNamaListBayar)
            val tvKode = findViewById<TextView>(R.id.tvKodeListBayar)
            val tvHarga = findViewById<TextView>(R.id.tvHargaListBayar)
            val btnUpdate = findViewById<Button>(R.id.btnUpdate)

            btnUpdate.setOnClickListener {
                Toast.makeText(context,"Fitur sedang dibuat",Toast.LENGTH_SHORT).show()
            }

            val curItem = listData[position]
            tvKode.text = curItem.kode
            tvNama.text = curItem.nama
            tvHarga.text = curItem.harga
        }
    }

    override fun getItemCount(): Int {

        return  listData.size
    }


}