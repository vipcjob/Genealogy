package com.example.familytree.ui.story

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.familytree.data.model.FamilyStory
import com.example.familytree.data.model.StoryType
import com.example.familytree.databinding.ItemStoryBinding
import com.example.familytree.util.DateUtils
import com.example.familytree.ui.story.StoryUiModel

class StoryAdapter(
    private val onStoryClick: (StoryUiModel) -> Unit
) : ListAdapter<StoryUiModel, StoryAdapter.StoryViewHolder>(StoryDiffCallback()) {
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoryViewHolder {
        val binding = ItemStoryBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return StoryViewHolder(binding)
    }
    
    override fun onBindViewHolder(holder: StoryViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
    
    inner class StoryViewHolder(
        private val binding: ItemStoryBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        
        fun bind(uiModel: StoryUiModel) {
            binding.textTitle.text = uiModel.title
            binding.textDate.text = DateUtils.formatDate(uiModel.publishDate)
            binding.textType.text = when(uiModel.storyType) {
                StoryType.FAMILY -> "家族故事"
                StoryType.HISTORY -> "家族历史"
                StoryType.BIOGRAPHY -> "人物传记"
                StoryType.EVENT -> "活动记录"
            }
            
            binding.textContent.text = if (uiModel.content.length > 150) {
                "${uiModel.content.substring(0, 150)}..."
            } else {
                uiModel.content
            }
            
            binding.textRelatedMembers.text = uiModel.relatedMemberNames.joinToString(", ")
            
            binding.root.setOnClickListener {
                onStoryClick(uiModel)
            }
        }
    }
    
    class StoryDiffCallback : DiffUtil.ItemCallback<StoryUiModel>() {
        override fun areItemsTheSame(oldItem: StoryUiModel, newItem: StoryUiModel): Boolean {
            return oldItem.id == newItem.id
        }
        
        override fun areContentsTheSame(oldItem: StoryUiModel, newItem: StoryUiModel): Boolean {
            return oldItem == newItem
        }
    }
} 