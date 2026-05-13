package com.gramaurja.gramaurja


import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class ZoneSelectionActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_zone_selection)

        val etZoneName = findViewById<EditText>(R.id.etZoneName)
        val btnContinue = findViewById<Button>(R.id.btnContinue)

        btnContinue.setOnClickListener {
            val zoneName = etZoneName.text.toString().trim()
            if (zoneName.isEmpty()) {
                Toast.makeText(this, "Please enter your zone name", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val prefs = getSharedPreferences("GramaUrjaPrefs", MODE_PRIVATE)
            prefs.edit().putString("zone_name", zoneName).apply()
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }
}