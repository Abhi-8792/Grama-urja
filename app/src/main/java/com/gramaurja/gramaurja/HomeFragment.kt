package com.gramaurja.gramaurja

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment

class HomeFragment : Fragment() {

    private var reportOnCount  = 12
    private var reportOffCount = 3
    private var currentPowerOn = true

    // Predictions based on time patterns
    private val predictions = listOf(
        "⚡ Power likely available 6AM–10AM and 4PM–8PM based on past patterns.",
        "⚡ High chance of power between 7AM–11AM today. Plan irrigation early.",
        "⚡ Power pattern suggests 3 hour window starting around 5PM.",
        "⚡ Unstable power expected today. Short windows of 1–2 hours likely.",
        "⚡ Good power day predicted — 8+ hours availability expected."
    )

    private val weatherData = listOf(
        "32°C • Partly Cloudy • Humidity 68%",
        "29°C • Sunny • Humidity 55%",
        "35°C • Hot & Dry • Humidity 40%",
        "27°C • Overcast • Humidity 75%",
        "31°C • Light Breeze • Humidity 62%"
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val prefs    = requireActivity().getSharedPreferences("GramaUrjaPrefs", 0)
        val zoneName = prefs.getString("zone_name", "Zone A") ?: "Zone A"

        // Bind views
        val tvPowerStatus:  TextView     = view.findViewById(R.id.tvPowerStatus)
        val tvLastUpdated:  TextView     = view.findViewById(R.id.tvLastUpdated)
        val tvStatusIcon:   TextView     = view.findViewById(R.id.tvStatusIcon)
        val tvZoneName:     TextView     = view.findViewById(R.id.tvZoneName)
        val layoutStatusBg: LinearLayout = view.findViewById(R.id.layoutStatusBg)
        val btnReportOn:    Button       = view.findViewById(R.id.btnReportOn)
        val btnReportOff:   Button       = view.findViewById(R.id.btnReportOff)
        val tvOnCount:      TextView     = view.findViewById(R.id.tvOnCount)
        val tvOffCount:     TextView     = view.findViewById(R.id.tvOffCount)
        val tvWeatherZone:  TextView     = view.findViewById(R.id.tvWeatherZone)
        val tvWeatherInfo:  TextView     = view.findViewById(R.id.tvWeatherInfo)
        val tvWeatherTip:   TextView     = view.findViewById(R.id.tvWeatherTip)
        val tvPrediction:   TextView     = view.findViewById(R.id.tvPrediction)
        val tvPredictionAcc:TextView     = view.findViewById(R.id.tvPredictionAccuracy)

        // Set initial data
        tvZoneName.text    = zoneName
        tvWeatherZone.text = "$zoneName Weather"
        tvOnCount.text     = reportOnCount.toString()
        tvOffCount.text    = reportOffCount.toString()

        // Random weather and prediction
        val randomWeather    = weatherData.random()
        val randomPrediction = predictions.random()
        val randomAccuracy   = (75..95).random()
        tvWeatherInfo.text       = randomWeather
        tvPrediction.text        = randomPrediction
        tvPredictionAcc.text     = "Prediction accuracy: $randomAccuracy%"

        // Weather tip based on temperature
        val temp = randomWeather.substringBefore("°").toIntOrNull() ?: 30
        tvWeatherTip.text = when {
            temp > 33 -> "🌡️ Hot day — irrigate early morning or evening"
            temp < 28 -> "✅ Cool weather — good time for irrigation"
            else      -> "💡 Good conditions for irrigation today"
        }

        // Start lightning animation
        startLightningAnimation(tvStatusIcon)

        // Show default ON state
        updateUI(true, tvPowerStatus, tvStatusIcon,
            layoutStatusBg, tvLastUpdated)

        // Auto-update the "X mins ago" text every 30 seconds
        val handler = Handler(Looper.getMainLooper())
        var secondsElapsed = 0
        val timeUpdater = object : Runnable {
            override fun run() {
                secondsElapsed += 30
                val mins = secondsElapsed / 60
                tvLastUpdated.text = when {
                    mins < 1  -> "Updated just now"
                    mins == 1 -> "Updated 1 min ago"
                    else      -> "Updated $mins mins ago"
                }
                handler.postDelayed(this, 30000)
            }
        }
        handler.postDelayed(timeUpdater, 30000)

        // Report ON button
        btnReportOn.setOnClickListener {
            if (!currentPowerOn) {
                reportOnCount++
                tvOnCount.text = reportOnCount.toString()
                animateCount(tvOnCount)
            }
            updateUI(true, tvPowerStatus, tvStatusIcon,
                layoutStatusBg, tvLastUpdated)
            secondsElapsed = 0
            Toast.makeText(requireContext(),
                "✅ Reported: Power ON — Thank you!", Toast.LENGTH_SHORT).show()
        }

        // Report OFF button
        btnReportOff.setOnClickListener {
            if (currentPowerOn) {
                reportOffCount++
                tvOffCount.text = reportOffCount.toString()
                animateCount(tvOffCount)
            }
            updateUI(false, tvPowerStatus, tvStatusIcon,
                layoutStatusBg, tvLastUpdated)
            secondsElapsed = 0
            Toast.makeText(requireContext(),
                "🔴 Reported: Power OFF — Alerting zone!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateUI(
        powerOn: Boolean,
        tvStatus: TextView,
        tvIcon: TextView,
        bg: LinearLayout,
        tvTime: TextView
    ) {
        currentPowerOn = powerOn
        if (powerOn) {
            tvStatus.text = "Power is ON"
            tvStatus.setTextColor(Color.parseColor("#00C853"))
            tvIcon.text   = "⚡"
            bg.setBackgroundColor(Color.parseColor("#E8F5E9"))
        } else {
            tvStatus.text = "Power is OFF"
            tvStatus.setTextColor(Color.parseColor("#D50000"))
            tvIcon.text   = "🔴"
            bg.setBackgroundColor(Color.parseColor("#FFEBEE"))
        }
        tvTime.text = "Updated just now"
        startLightningAnimation(tvIcon)
    }

    // Pulse animation on the status icon
    private fun startLightningAnimation(view: TextView) {
        val scaleX = ObjectAnimator.ofFloat(view, "scaleX", 1f, 1.3f, 1f)
        val scaleY = ObjectAnimator.ofFloat(view, "scaleY", 1f, 1.3f, 1f)
        scaleX.duration = 800
        scaleY.duration = 800
        scaleX.repeatCount = ValueAnimator.INFINITE
        scaleY.repeatCount = ValueAnimator.INFINITE
        scaleX.interpolator = AccelerateDecelerateInterpolator()
        scaleY.interpolator = AccelerateDecelerateInterpolator()
        scaleX.start()
        scaleY.start()
    }

    // Pop animation when count changes
    private fun animateCount(view: TextView) {
        val scaleUp = ObjectAnimator.ofFloat(view, "scaleX", 1f, 1.5f)
        scaleUp.duration = 200
        scaleUp.start()
        val scaleBack = ObjectAnimator.ofFloat(view, "scaleX", 1.5f, 1f)
        scaleBack.duration = 200
        scaleBack.startDelay = 200
        scaleBack.start()
    }
}