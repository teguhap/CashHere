package com.project.cashhere

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat

class DisplayPesananActivity : AppCompatActivity() {
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_display_pesanan)

        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        window.statusBarColor = ContextCompat.getColor(this,R.color.white)
        window.setBackgroundDrawableResource(R.drawable.bg_dashboard);

        val tvListPesanan = findViewById<TextView>(R.id.listPesananBayar)
        val tvTotalBayar = findViewById<TextView>(R.id.totalBayarStruk)
        val tvMetodeBayar = findViewById<TextView>(R.id.tvMetodeBayarStruk)
        val tvKode = findViewById<TextView>(R.id.kodePesananBayar)
        val btnBack = findViewById<ImageView>(R.id.btnBackBayar2)

        btnBack.setOnClickListener {
            onBackPressed()
            finish()
        }


        val listPesanan = intent.getStringExtra("listPesanan")
        val metodeBayar = intent.getStringExtra("metodeBayar")
        val totalBayar = intent.getStringExtra("totalBayar")
        val kode = intent.getStringExtra("kode")

        tvListPesanan.text = listPesanan
        tvTotalBayar.text = "Rp$totalBayar"
        tvMetodeBayar.text = metodeBayar
        tvKode.text = "||$kode||"






    }
}