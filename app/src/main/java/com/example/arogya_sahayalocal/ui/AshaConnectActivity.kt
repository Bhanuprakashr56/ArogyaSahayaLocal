package com.example.arogya_sahayalocal.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.arogya_sahayalocal.databinding.ActivityAshaConnectBinding
import com.example.arogya_sahayalocal.databinding.ItemAshaEventBinding
import java.text.SimpleDateFormat
import java.util.*

data class AshaEvent(val date: String, val title: String, val location: String, val time: String)

class AshaConnectActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAshaConnectBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAshaConnectBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = "ASHA Connect"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val events = generateSimulatedEvents()
        binding.rvAshaEvents.layoutManager = LinearLayoutManager(this)
        binding.rvAshaEvents.adapter = AshaEventAdapter(events)
    }

    private fun generateSimulatedEvents(): List<AshaEvent> {
        val sdf = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
        val cal = Calendar.getInstance()
        return listOf(
            AshaEvent(sdf.format(cal.apply { add(Calendar.DAY_OF_YEAR, 3) }.time),
                "Health Camp", "Gram Panchayat Hall", "10:00 AM – 1:00 PM"),
            AshaEvent(sdf.format(cal.apply { add(Calendar.DAY_OF_YEAR, 5) }.time),
                "ASHA Worker Round", "Village Center", "9:00 AM – 11:00 AM"),
            AshaEvent(sdf.format(cal.apply { add(Calendar.DAY_OF_YEAR, 4) }.time),
                "Free BP & Sugar Check", "Primary Health Centre", "8:00 AM – 12:00 PM"),
            AshaEvent(sdf.format(cal.apply { add(Calendar.DAY_OF_YEAR, 6) }.time),
                "Vaccination Drive", "Anganwadi Centre", "9:00 AM – 3:00 PM"),
            AshaEvent(sdf.format(cal.apply { add(Calendar.DAY_OF_YEAR, 8) }.time),
                "Eye Checkup Camp", "Community Hall", "10:00 AM – 2:00 PM"),
            AshaEvent(sdf.format(cal.apply { add(Calendar.DAY_OF_YEAR, 10) }.time),
                "Nutrition Awareness", "School Ground", "11:00 AM – 1:00 PM")
        )
    }

    override fun onSupportNavigateUp(): Boolean { finish(); return true }
}

class AshaEventAdapter(private val events: List<AshaEvent>) :
    RecyclerView.Adapter<AshaEventAdapter.VH>() {

    inner class VH(val binding: ItemAshaEventBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        VH(ItemAshaEventBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: VH, position: Int) {
        val e = events[position]
        holder.binding.tvEventDate.text     = e.date
        holder.binding.tvEventTitle.text    = e.title
        holder.binding.tvEventLocation.text = "📍 ${e.location}"
        holder.binding.tvEventTime.text     = "🕐 ${e.time}"
    }

    override fun getItemCount() = events.size
}
