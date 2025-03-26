package com.example.familytree.ui.activity

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.familytree.data.model.FamilyActivity
import com.example.familytree.databinding.ItemOngoingActivityBinding
import java.text.SimpleDateFormat
import java.util.Locale

class OngoingActivityAdapter(
    private val onItemClick: (FamilyActivity) -> Unit
) : ListAdapter<FamilyActivity, OngoingActivityAdapter.ActivityViewHolder>(ActivityDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActivityViewHolder {
        val binding = ItemOngoingActivityBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ActivityViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ActivityViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ActivityViewHolder(
        private val binding: ItemOngoingActivityBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onItemClick(getItem(position))
                }
            }
        }

        fun bind(activity: FamilyActivity) {
            binding.textTitle.text = activity.title
            binding.textOrganizer.text = "组织者: ${activity.organizer}"
            
            // 格式化开始日期
            val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
            val startDateStr = dateFormat.format(activity.startDate)
            
            // 格式化结束日期（如果有）
            val endDateStr = activity.endDate?.let { dateFormat.format(it) } ?: "未指定"
            
            binding.textDate.text = "$startDateStr ~ $endDateStr"
            binding.textLocation.text = activity.location
            binding.textDescription.text = activity.description
            
            // 设置按钮点击事件
            binding.buttonDetails.setOnClickListener {
                onItemClick(activity)
            }
            
            // 如果有设置进度，可以更新进度条
            // binding.progressBar.progress = activity.progress
            // binding.textProgress.text = "${activity.progress}%"
        }
    }

    private class ActivityDiffCallback : DiffUtil.ItemCallback<FamilyActivity>() {
        override fun areItemsTheSame(oldItem: FamilyActivity, newItem: FamilyActivity): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: FamilyActivity, newItem: FamilyActivity): Boolean {
            return oldItem == newItem
        }
    }
} 