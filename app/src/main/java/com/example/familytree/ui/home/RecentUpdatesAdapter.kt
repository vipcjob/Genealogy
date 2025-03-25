package com.example.familytree.ui.home

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.familytree.data.model.RecentUpdate
import com.example.familytree.databinding.ItemRecentUpdateBinding
import java.text.SimpleDateFormat
import java.util.Locale

class RecentUpdatesAdapter : ListAdapter<RecentUpdate, RecentUpdatesAdapter.UpdateViewHolder>(UpdateDiffCallback()) {
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UpdateViewHolder {
        val binding = ItemRecentUpdateBinding.inflate(
            LayoutInflater.from(parent.context), parent, false)
        return UpdateViewHolder(binding)
    }
    
    override fun onBindViewHolder(holder: UpdateViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
    
    class UpdateViewHolder(private val binding: ItemRecentUpdateBinding) :
        RecyclerView.ViewHolder(binding.root) {
        
        private val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
        
        fun bind(update: RecentUpdate) {
            binding.apply {
                textTitle.text = update.title
                textTime.text = dateFormat.format(update.timestamp)
                textContent.text = update.avatarText
                
                // 设置头像背景颜色
                try {
                    val color = Color.parseColor(update.avatarColor)
                    imageAvatar.setBackgroundColor(color)
                } catch (e: Exception) {
                    // 解析颜色失败，使用默认颜色
                    imageAvatar.setBackgroundColor(Color.GRAY)
                }
            }
        }
    }
    
    private class UpdateDiffCallback : DiffUtil.ItemCallback<RecentUpdate>() {
        override fun areItemsTheSame(oldItem: RecentUpdate, newItem: RecentUpdate): Boolean {
            return oldItem.id == newItem.id
        }
        
        override fun areContentsTheSame(oldItem: RecentUpdate, newItem: RecentUpdate): Boolean {
            return oldItem == newItem
        }
    }
} 