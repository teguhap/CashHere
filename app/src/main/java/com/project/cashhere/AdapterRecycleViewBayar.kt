package com.project.cashhere

import android.content.Context
import android.content.Intent
import android.view.ContextMenu
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.RecyclerView


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
            val btnTambahMenu = findViewById<Button>(R.id.btnTambah)
            val cardView = findViewById<CardView>(R.id.cvTambahItem)
            val btnTambahJumlah = findViewById<ImageView>(R.id.btnAdd)
            val btnKurangJumlah = findViewById<ImageView>(R.id.btnMin)
            val tvJumlahItem = findViewById<TextView>(R.id.tvJumlahItem)

            val intent = Intent("totalItemMenu")


            val fadeInAnim = AnimationUtils.loadAnimation(context,R.anim.fade_in)

            var jumlahItem = 1

            val curItem = listData[position]
            tvKode.text = curItem.kode
            tvNama.text = curItem.nama
            tvHarga.text = curItem.harga

            val kode = tvKode.text.toString()
            val nama = tvNama.text.toString()
            val harga = tvHarga.text.toString()
            val jmlItem = tvJumlahItem.text.toString().toInt()

            btnTambahMenu.setOnClickListener {
                cardView.visibility = View.VISIBLE
                cardView.animation = fadeInAnim
                cardView.animation.start()
                btnTambahMenu.visibility = View.GONE
                jumlahItem = 1
                tvJumlahItem.text = jumlahItem.toString()

                intent.apply {
                    putExtra("kode",kode)
                    putExtra("nama",nama)
                    putExtra("harga",harga)
                    putExtra("jumlah",jmlItem)
                }
                LocalBroadcastManager.getInstance(context).sendBroadcast(intent)
            }

            btnTambahJumlah.setOnClickListener {
                jumlahItem+=1
                tvJumlahItem.text = jumlahItem.toString()
            }

            btnKurangJumlah.setOnClickListener {

                if(jumlahItem==1){
                    cardView.visibility = View.GONE
                    btnTambahMenu.visibility = View.VISIBLE
                    btnTambahMenu.animation = fadeInAnim
                    btnTambahMenu.animation.start()
                }
                jumlahItem-=1
                tvJumlahItem.text = jumlahItem.toString()

            }


        }
    }

    override fun getItemCount(): Int {

        return  listData.size
    }


}