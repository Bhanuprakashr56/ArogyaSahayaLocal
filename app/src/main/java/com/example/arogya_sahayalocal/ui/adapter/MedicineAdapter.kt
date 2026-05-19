package com.example.arogya_sahayalocal.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.arogya_sahayalocal.data.entity.MedicineEntity
import com.example.arogya_sahayalocal.databinding.ItemMedicineBinding

class MedicineAdapter(
    private val onDelete: (MedicineEntity) -> Unit
) : ListAdapter<MedicineEntity, MedicineAdapter.ViewHolder>(DIFF) {

    inner class ViewHolder(private val binding: ItemMedicineBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: MedicineEntity) {
            binding.tvMedicineName.text = item.name
            binding.tvDosage.text       = "Dosage: ${item.dosage}"
            binding.tvTiming.text       = "⏰ ${item.timing}"
            binding.btnDelete.setOnClickListener { onDelete(item) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(
            ItemMedicineBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bind(getItem(position))

    companion object {
        private val DIFF = object : DiffUtil.ItemCallback<MedicineEntity>() {
            override fun areItemsTheSame(a: MedicineEntity, b: MedicineEntity) = a.id == b.id
            override fun areContentsTheSame(a: MedicineEntity, b: MedicineEntity) = a == b
        }
    }
}
