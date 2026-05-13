package com.gramaurja.gramaurja

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import kotlin.math.ceil

class PumpTimerFragment : Fragment() {

    // Water needed per acre in litres (base values)
    private val cropWaterPerAcre = mapOf(
        "Rice / Paddy"  to 1350,
        "Sugarcane"     to 1800,
        "Cotton"        to 900,
        "Wheat"         to 675,
        "Vegetables"    to 450,
        "Groundnut"     to 750,
        "Banana"        to 1125
    )

    // Flow rate in litres per minute for each pipe size
    private val pipeFlowRate = mapOf(
        "½ inch  (40 L/min)"    to 40,
        "1 inch  (150 L/min)"   to 150,
        "2 inch  (400 L/min)"   to 400,
        "3 inch  (800 L/min)"   to 800,
        "4 inch  (1500 L/min)"  to 1500
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_pump_timer, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val spinnerCrop     = view.findViewById<Spinner>(R.id.spinnerCrop)
        val spinnerPipe     = view.findViewById<Spinner>(R.id.spinnerPipeSize)
        val etAcres         = view.findViewById<EditText>(R.id.etAcres)
        val btnCalculate    = view.findViewById<Button>(R.id.btnCalculate)
        val cardResult      = view.findViewById<CardView>(R.id.cardResult)
        val tvPumpMinutes   = view.findViewById<TextView>(R.id.tvPumpMinutes)
        val tvResultCrop    = view.findViewById<TextView>(R.id.tvResultCrop)
        val tvResultAcres   = view.findViewById<TextView>(R.id.tvResultAcres)
        val tvResultPipe    = view.findViewById<TextView>(R.id.tvResultPipe)
        val tvResultFlow    = view.findViewById<TextView>(R.id.tvResultFlow)
        val tvResultWater   = view.findViewById<TextView>(R.id.tvResultWater)
        val tvPipeInfo      = view.findViewById<TextView>(R.id.tvPipeInfo)

        // Setup crop spinner
        val cropList = cropWaterPerAcre.keys.toList()
        spinnerCrop.adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            cropList
        ).also { it.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item) }

        // Setup pipe size spinner
        val pipeList = pipeFlowRate.keys.toList()
        spinnerPipe.adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            pipeList
        ).also { it.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item) }

        // Show flow rate info when pipe is selected
        spinnerPipe.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>, v: View?, pos: Int, id: Long
            ) {
                val selectedPipe = pipeList[pos]
                val flow = pipeFlowRate[selectedPipe] ?: 0
                tvPipeInfo.text = "ℹ️ Flow rate: $flow litres/min"
            }
            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        // Calculate button
        btnCalculate.setOnClickListener {
            val acresText = etAcres.text.toString().trim()

            if (acresText.isEmpty()) {
                Toast.makeText(
                    requireContext(),
                    "⚠️ Please enter land size in acres",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            val acres        = acresText.toFloatOrNull()
            if (acres == null || acres <= 0) {
                Toast.makeText(
                    requireContext(),
                    "⚠️ Please enter a valid number",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            val selectedCrop = spinnerCrop.selectedItem.toString()
            val selectedPipe = spinnerPipe.selectedItem.toString()

            val waterPerAcre  = cropWaterPerAcre[selectedCrop] ?: 900
            val flowRate      = pipeFlowRate[selectedPipe]     ?: 150

            // Core calculation
            val totalWaterLitres = waterPerAcre * acres
            val totalMinutes     = ceil(totalWaterLitres / flowRate).toInt()
            val hours            = totalMinutes / 60
            val mins             = totalMinutes % 60

            // Format time display
            val timeDisplay = when {
                hours > 0 && mins > 0 -> "$totalMinutes\nmins ($hours h $mins m)"
                hours > 0             -> "$totalMinutes\nmins ($hours hr)"
                else                  -> "$totalMinutes"
            }

            // Update result card
            tvPumpMinutes.text  = totalMinutes.toString()
            tvResultCrop.text   = "🌾 Crop: $selectedCrop"
            tvResultAcres.text  = "📐 Land: $acres acre(s)"
            tvResultPipe.text   = "🔧 Pipe: $selectedPipe"
            tvResultFlow.text   = "💧 Flow Rate: $flowRate L/min"
            tvResultWater.text  = "🪣 Total Water Needed: ${
                String.format("%.0f", totalWaterLitres)
            } Litres"

            cardResult.visibility = View.VISIBLE

            // Scroll to result
            Toast.makeText(
                requireContext(),
                "✅ Run pump for $totalMinutes minutes",
                Toast.LENGTH_LONG
            ).show()
        }
    }
}