package com.fcz.genealogy.ui.activity.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.fcz.genealogy.data.model.FamilyActivity
import com.fcz.genealogy.databinding.ItemUpcomingActivityBinding
import java.text.SimpleDateFormat
import java.util.Locale

class UpcomingActivityAdapter(
    private val onItemClick: (FamilyActivity) -> Unit
) : ListAdapter<FamilyActivity, UpcomingActivityAdapter.ActivityViewHolder>(ActivityDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActivityViewHolder {
        val binding = ItemUpcomingActivityBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ActivityViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ActivityViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ActivityViewHolder(
        private val binding: ItemUpcomingActivityBinding
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
            val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
            binding.textDate.text = dateFormat.format(activity.startDate)
            
            binding.textLocation.text = activity.location
            binding.textDescription.text = activity.description
            
            // 设置参加按钮
            binding.buttonJoin.setOnClickListener {
                onItemClick(activity)
            }
            
            // 设置提醒按钮
            binding.buttonRemind.setOnClickListener {
                // TODO: 设置提醒逻辑
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