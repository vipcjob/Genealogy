package com.example.familytree.ui.activity.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.familytree.data.model.FamilyActivity
import com.example.familytree.databinding.ItemPastActivityBinding
import com.example.familytree.util.DateUtils

class PastActivityAdapter(
    private val onActivityClick: (FamilyActivity) -> Unit
) : ListAdapter<FamilyActivity, PastActivityAdapter.ViewHolder>(ActivityDiffCallback()) {
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemPastActivityBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }
    
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
    
    inner class ViewHolder(
        private val binding: ItemPastActivityBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        
        fun bind(activity: FamilyActivity) {
            binding.textTitle.text = activity.title
            binding.textOrganizer.text = "组织者: ${activity.organizer}"
            
            val dateText = activity.startDate?.let { start ->
                if (activity.endDate != null && activity.endDate != activity.startDate) {
                    "${DateUtils.formatDate(start)} - ${DateUtils.formatDate(activity.endDate!!)}"
                } else {
                    DateUtils.formatDate(start)
                }
            } ?: "日期未定"
            binding.textDate.text = dateText
            
            binding.textLocation.text = activity.location ?: "地点未定"
            
            // 设置点击事件
            binding.root.setOnClickListener {
                onActivityClick(activity)
            }
            
            // 设置相册按钮点击事件
            binding.buttonAlbum.setOnClickListener {
                // TODO: 实现查看活动相册功能
            }
            
            binding.buttonDetails.setOnClickListener {
                onActivityClick(activity)
            }
        }
    }
    
    class ActivityDiffCallback : DiffUtil.ItemCallback<FamilyActivity>() {
        override fun areItemsTheSame(oldItem: FamilyActivity, newItem: FamilyActivity): Boolean {
            return oldItem.id == newItem.id
        }
        
        override fun areContentsTheSame(oldItem: FamilyActivity, newItem: FamilyActivity): Boolean {
            return oldItem == newItem
        }
    }
} 