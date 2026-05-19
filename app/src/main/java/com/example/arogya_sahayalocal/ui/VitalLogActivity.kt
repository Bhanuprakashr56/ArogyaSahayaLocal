package com.example.arogya_sahayalocal.ui

import android.graphics.Color
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.arogya_sahayalocal.data.entity.VitalLogEntity
import com.example.arogya_sahayalocal.databinding.ActivityVitalLogBinding
import com.example.arogya_sahayalocal.ui.viewmodel.VitalViewModel
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter

class VitalLogActivity : AppCompatActivity() {

    private lateinit var binding: ActivityVitalLogBinding
    private lateinit var viewModel: VitalViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVitalLogBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = "Vital Log"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Fix: AndroidViewModel requires Application — use the correct factory
        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(application)
        )[VitalViewModel::class.java]

        setupChart()

        viewModel.last7Days.observe(this) { logs ->
            updateChart(logs)
            if (logs.isNotEmpty()) {
                val latest = logs.first()
                binding.tvLatestReading.text =
                    "Latest: BP ${latest.systolic}/${latest.diastolic} mmHg  |  HR ${latest.heartRate} bpm"
            }
        }

        binding.btnLogVitals.setOnClickListener {
            val sys = binding.etSystolic.text.toString().toIntOrNull()
            val dia = binding.etDiastolic.text.toString().toIntOrNull()
            val hr  = binding.etHeartRate.text.toString().toIntOrNull()

            if (sys == null || dia == null || hr == null) {
                Toast.makeText(this, "Please fill all fields with numbers", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (sys !in 60..250 || dia !in 40..150 || hr !in 30..220) {
                Toast.makeText(this, "Please enter realistic values", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            viewModel.logVitals(sys, dia, hr)
            binding.etSystolic.text?.clear()
            binding.etDiastolic.text?.clear()
            binding.etHeartRate.text?.clear()
            Toast.makeText(this, "✓ Vitals logged successfully", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupChart() {
        binding.lineChart.apply {
            description.isEnabled = false
            setTouchEnabled(true)
            isDragEnabled = true
            setScaleEnabled(true)
            setPinchZoom(true)
            setBackgroundColor(Color.TRANSPARENT)
            setNoDataText("Log your vitals to see the trend")
            setNoDataTextColor(Color.WHITE)

            xAxis.apply {
                position = XAxis.XAxisPosition.BOTTOM
                textColor = Color.WHITE
                textSize = 11f
                granularity = 1f
                setDrawGridLines(false)
            }
            axisLeft.apply {
                textColor = Color.WHITE
                textSize = 12f
                axisMinimum = 40f
                axisMaximum = 200f
            }
            axisRight.isEnabled = false

            legend.apply {
                textColor = Color.WHITE
                textSize = 13f
                form = Legend.LegendForm.LINE
            }
        }
    }

    private fun updateChart(logs: List<VitalLogEntity>) {
        if (logs.isEmpty()) return

        val reversed = logs.reversed()   // oldest → newest
        val labels   = reversed.map { it.date.takeLast(5) }   // MM-dd

        val sysList  = reversed.mapIndexed { i, l -> Entry(i.toFloat(), l.systolic.toFloat()) }
        val diaList  = reversed.mapIndexed { i, l -> Entry(i.toFloat(), l.diastolic.toFloat()) }
        val hrList   = reversed.mapIndexed { i, l -> Entry(i.toFloat(), l.heartRate.toFloat()) }

        fun dataset(entries: List<Entry>, label: String, color: Int): LineDataSet =
            LineDataSet(entries, label).apply {
                this.color = color
                lineWidth = 2.5f
                setCircleColor(color)
                circleRadius = 5f
                valueTextColor = Color.WHITE
                valueTextSize = 10f
                mode = LineDataSet.Mode.CUBIC_BEZIER
            }

        binding.lineChart.xAxis.valueFormatter = IndexAxisValueFormatter(labels)
        binding.lineChart.data = LineData(
            dataset(sysList, "Systolic",  Color.parseColor("#EF5350")),
            dataset(diaList, "Diastolic", Color.parseColor("#42A5F5")),
            dataset(hrList,  "Heart Rate",Color.parseColor("#66BB6A"))
        )
        binding.lineChart.invalidate()
    }

    override fun onSupportNavigateUp(): Boolean { finish(); return true }
}
