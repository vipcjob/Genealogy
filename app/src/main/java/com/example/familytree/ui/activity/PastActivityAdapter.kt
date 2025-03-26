package com.example.familytree.ui.activity

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.familytree.data.model.FamilyActivity
import com.example.familytree.databinding.ItemPastActivityBinding
import java.text.SimpleDateFormat
import java.util.Locale

class PastActivityAdapter(
    private val onItemClick: (FamilyActivity) -> Unit
) : ListAdapter<FamilyActivity, PastActivityAdapter.ActivityViewHolder>(ActivityDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActivityViewHolder {
        val binding = ItemPastActivityBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ActivityViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ActivityViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ActivityViewHolder(
        private val binding: ItemPastActivityBinding
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
            
            // 格式化日期
            val dateFormat = SimpleDateFormat("yyyy年MM月dd日", Locale.getDefault())
            binding.textDate.text = dateFormat.format(activity.startDate)
            binding.textLocation.text = activity.location
            
            // 设置详情按钮
            binding.buttonDetails.setOnClickListener {
                onItemClick(activity)
            }
            
            // 设置相册按钮
            binding.buttonAlbum.setOnClickListener {
                // TODO: 跳转到活动相册
            }
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