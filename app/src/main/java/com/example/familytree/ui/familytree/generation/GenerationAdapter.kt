package com.example.familytree.ui.familytree.generation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.familytree.R
import com.example.familytree.databinding.ItemGenerationBinding

class GenerationAdapter(
    private val onGenerationClick: (Int) -> Unit
) : ListAdapter<Pair<Int, Int>, GenerationAdapter.GenerationViewHolder>(GenerationDiffCallback()) {
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GenerationViewHolder {
        val binding = ItemGenerationBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return GenerationViewHolder(binding)
    }
    
    override fun onBindViewHolder(holder: GenerationViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
    
    inner class GenerationViewHolder(
        private val binding: ItemGenerationBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        
        fun bind(generationData: Pair<Int, Int>) {
            val (generation, count) = generationData
            
            binding.textGeneration.text = binding.root.context.getString(
                R.string.generation_format,
                generation
            )
            binding.textCount.text = "$count 人"
            
            // 设置进度条
            binding.progressBar.max = 100
            binding.progressBar.progress = getProgressValue(count)
            
            // 设置点击事件
            binding.root.setOnClickListener {
                onGenerationClick(generation)
            }
        }
        
        private fun getProgressValue(count: Int): Int {
            // 根据人数计算进度值，上限为100
            return minOf(count * 5, 100)
        }
    }
    
    class GenerationDiffCallback : DiffUtil.ItemCallback<Pair<Int, Int>>() {
        override fun areItemsTheSame(oldItem: Pair<Int, Int>, newItem: Pair<Int, Int>): Boolean {
            return oldItem.first == newItem.first
        }
        
        override fun areContentsTheSame(oldItem: Pair<Int, Int>, newItem: Pair<Int, Int>): Boolean {
            return oldItem == newItem
        }
    }
} 