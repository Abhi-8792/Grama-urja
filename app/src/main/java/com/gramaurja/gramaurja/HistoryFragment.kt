package com.gramaurja.gramaurja

import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class HistoryItem(
    val isPowerOn: Boolean,
    val timestamp: Long,
    val zoneName: String = "Zone A"
)

class HistoryFragment : Fragment() {

    private lateinit var adapter: HistoryAdapter
    private val fullList = mutableListOf<HistoryItem>()

    // Save search text across navigation
    companion object {
        var savedSearchText: String = ""
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_history, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val prefs = requireActivity().getSharedPreferences("GramaUrjaPrefs", 0)
        val zoneName = prefs.getString("zone_name", "Zone A") ?: "Zone A"

        // Build sample history list
        fullList.clear()
        fullList.addAll(listOf(
            HistoryItem(true,  System.currentTimeMillis() - 2   * 60000, zoneName),
            HistoryItem(false, System.currentTimeMillis() - 15  * 60000, zoneName),
            HistoryItem(true,  System.currentTimeMillis() - 45  * 60000, zoneName),
            HistoryItem(false, System.currentTimeMillis() - 90  * 60000, zoneName),
            HistoryItem(true,  System.currentTimeMillis() - 150 * 60000, zoneName),
            HistoryItem(false, System.currentTimeMillis() - 200 * 60000, zoneName),
            HistoryItem(true,  System.currentTimeMillis() - 300 * 60000, zoneName),
            HistoryItem(false, System.currentTimeMillis() - 400 * 60000, zoneName),
            HistoryItem(true,  System.currentTimeMillis() - 500 * 60000, zoneName),
            HistoryItem(false, System.currentTimeMillis() - 600 * 60000, zoneName)
        ))

        val rvHistory = view.findViewById<RecyclerView>(R.id.rvHistory)
        val etSearch  = view.findViewById<EditText>(R.id.etSearch)

        adapter = HistoryAdapter(fullList.toMutableList())
        rvHistory.layoutManager = LinearLayoutManager(requireContext())
        rvHistory.adapter = adapter

        // Restore previous search text
        etSearch.setText(savedSearchText)
        if (savedSearchText.isNotEmpty()) {
            filterList(savedSearchText)
        }

        // Live search filter
        etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, st: Int, c: Int, a: Int) {}
            override fun onTextChanged(s: CharSequence?, st: Int, c: Int, a: Int) {}
            override fun afterTextChanged(s: Editable?) {
                val query = s.toString()
                savedSearchText = query
                filterList(query)
            }
        })
    }

    private fun filterList(query: String) {
        val filtered = if (query.isEmpty()) {
            fullList.toMutableList()
        } else {
            fullList.filter { item ->
                val status = if (item.isPowerOn) "power on" else "power off"
                status.contains(query.lowercase()) ||
                        item.zoneName.lowercase().contains(query.lowercase())
            }.toMutableList()
        }
        adapter.updateList(filtered)
    }
}

class HistoryAdapter(
    private var items: MutableList<HistoryItem>
) : RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>() {

    fun updateList(newList: MutableList<HistoryItem>) {
        items = newList
        notifyDataSetChanged()
    }

    class HistoryViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvIcon:   TextView = view.findViewById(R.id.tvHistoryIcon)
        val tvStatus: TextView = view.findViewById(R.id.tvHistoryStatus)
        val tvTime:   TextView = view.findViewById(R.id.tvHistoryTime)
        val tvAgo:    TextView = view.findViewById(R.id.tvHistoryAgo)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_history, parent, false)
        return HistoryViewHolder(view)
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        val item = items[position]
        val sdf = SimpleDateFormat("dd MMM, hh:mm a", Locale.getDefault())
        val minutesAgo = ((System.currentTimeMillis() - item.timestamp) / 60000).toInt()

        if (item.isPowerOn) {
            holder.tvIcon.text   = "⚡"
            holder.tvStatus.text = "Power ON"
            holder.tvStatus.setTextColor(Color.parseColor("#00C853"))
        } else {
            holder.tvIcon.text   = "🔴"
            holder.tvStatus.text = "Power OFF"
            holder.tvStatus.setTextColor(Color.parseColor("#D50000"))
        }
        holder.tvTime.text = sdf.format(Date(item.timestamp))
        holder.tvAgo.text  = when {
            minutesAgo < 1  -> "Just now"
            minutesAgo < 60 -> "$minutesAgo mins ago"
            else            -> "${minutesAgo / 60} hrs ago"
        }
    }
}