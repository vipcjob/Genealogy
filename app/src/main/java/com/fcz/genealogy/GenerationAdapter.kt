package com.fcz.genealogy.ui.familytree.generation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.fcz.genealogy.data.model.FamilyMember
import com.fcz.genealogy.databinding.ItemGenerationBinding

class GenerationAdapter(
    private val onGenerationClick: (Int) -> Unit
) : RecyclerView.Adapter<GenerationAdapter.GenerationViewHolder>() {
    
    private var generations: Map<Int, List<FamilyMember>> = emptyMap()
    private var generationKeys: List<Int> = emptyList()
    
    fun submitGenerations(generations: Map<Int, List<FamilyMember>>) {
        this.generations = generations
        this.generationKeys = generations.keys.toList().sorted()
        notifyDataSetChanged()
    }
    
    override fun getItemCount() = generationKeys.size
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GenerationViewHolder {
        val binding = ItemGenerationBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return GenerationViewHolder(binding)
    }
    
    override fun onBindViewHolder(holder: GenerationViewHolder, position: Int) {
        val generation = generationKeys[position]
        val members = generations[generation] ?: emptyList()
        holder.bind(generation, members)
    }
    
    inner class GenerationViewHolder(
        private val binding: ItemGenerationBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        
        init {
            binding.root.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onGenerationClick(generationKeys[position])
                }
            }
        }
        
        fun bind(generation: Int, members: List<FamilyMember>) {
            binding.textGeneration.text = "第${generation}代"
            binding.textCount.text = "${members.size}人"
            
            // 设置进度条
            val progress = minOf(members.size * 5, 100) // 简单计算进度值
            binding.progressBar.progress = progress
        }
    }
} 