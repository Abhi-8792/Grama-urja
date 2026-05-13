package com.gramaurja.gramaurja

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment

class SearchFragment : Fragment() {

    private val zones = listOf(
        "Krishnapuram Zone A", "Krishnapuram Zone B",
        "Ramnagar Zone 1",     "Ramnagar Zone 2",
        "Sundarpur Main",      "Sundarpur East",
        "Lakshmipur Central",  "Lakshmipur West",
        "Govindpur Zone A",    "Govindpur Zone B",
        "Nandyal Zone 1",      "Nandyal Zone 2"
    )

    // Random power status for each zone
    private val zonePower = zones.associateWith { listOf(true, false).random() }

    companion object {
        var savedQuery: String = ""
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val etSearch     = view.findViewById<EditText>(R.id.etZoneSearch)
        val llResults    = view.findViewById<LinearLayout>(R.id.llSearchResults)

        // Show all zones first
        showZones(llResults, zones)

        // Restore previous search
        etSearch.setText(savedQuery)
        if (savedQuery.isNotEmpty()) {
            val filtered = zones.filter {
                it.lowercase().contains(savedQuery.lowercase())
            }
            showZones(llResults, filtered)
        }

        // Live search
        etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, st: Int, c: Int, a: Int) {}
            override fun onTextChanged(s: CharSequence?, st: Int, c: Int, a: Int) {}
            override fun afterTextChanged(s: Editable?) {
                val query = s.toString()
                savedQuery = query
                val filtered = if (query.isEmpty()) zones
                else zones.filter { it.lowercase().contains(query.lowercase()) }
                showZones(llResults, filtered)
            }
        })
    }

    private fun showZones(container: LinearLayout, zoneList: List<String>) {
        container.removeAllViews()
        if (zoneList.isEmpty()) {
            val tv = TextView(requireContext())
            tv.text = "No zones found"
            tv.textSize = 16f
            tv.setPadding(16, 24, 16, 24)
            container.addView(tv)
            return
        }
        zoneList.forEach { zone ->
            val isPowerOn = zonePower[zone] ?: true
            val row = LayoutInflater.from(requireContext())
                .inflate(R.layout.item_zone_search, container, false)

            row.findViewById<TextView>(R.id.tvZoneItemName).text  = zone
            val tvStatus = row.findViewById<TextView>(R.id.tvZoneItemStatus)
            if (isPowerOn) {
                tvStatus.text = "⚡ ON"
                tvStatus.setTextColor(android.graphics.Color.parseColor("#00C853"))
                row.setBackgroundColor(android.graphics.Color.parseColor("#F1F8E9"))
            } else {
                tvStatus.text = "🔴 OFF"
                tvStatus.setTextColor(android.graphics.Color.parseColor("#D50000"))
                row.setBackgroundColor(android.graphics.Color.parseColor("#FFF3F3"))
            }

            row.setOnClickListener {
                // Save selected zone
                val prefs = requireActivity()
                    .getSharedPreferences("GramaUrjaPrefs", Context.MODE_PRIVATE)
                prefs.edit().putString("zone_name", zone).apply()
                Toast.makeText(requireContext(),
                    "✅ Zone changed to: $zone", Toast.LENGTH_SHORT).show()
            }
            container.addView(row)
        }
    }
}