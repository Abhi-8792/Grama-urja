package com.gramaurja.gramaurja

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        Handler(Looper.getMainLooper()).postDelayed({
            val prefs = getSharedPreferences("GramaUrjaPrefs", MODE_PRIVATE)
            val savedZone = prefs.getString("zone_name", null)

            if (savedZone != null) {
                startActivity(Intent(this, MainActivity::class.java))
            } else {
                startActivity(Intent(this, ZoneSelectionActivity::class.java))
            }
            finish()
        }, 2000)
    }
}