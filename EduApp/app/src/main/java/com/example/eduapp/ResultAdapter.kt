package com.example.eduapp

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.eduapp.databinding.ItemResultBinding
import java.text.SimpleDateFormat
import java.util.*

class ResultAdapter : ListAdapter<Result, ResultAdapter.VH>(DIFF) {
    companion object {
        private val DIFF = object : DiffUtil.ItemCallback<Result>() {
            override fun areItemsTheSame(oldItem: Result, newItem: Result) = oldItem.id == newItem.id
            override fun areContentsTheSame(oldItem: Result, newItem: Result) = oldItem == newItem
        }
    }

    class VH(private val binding: ItemResultBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(r: Result) {
            binding.tvScore.text = "${r.score} / ${r.total}"
            val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
            binding.tvDate.text = sdf.format(Date(r.createdAt))
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val b = ItemResultBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VH(b)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bind(getItem(position))
    }
}

