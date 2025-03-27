package com.fcz.genealogy.ui.familytree.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.fcz.genealogy.R
import com.fcz.genealogy.data.model.FamilyMember
import com.fcz.genealogy.data.model.Gender
import com.fcz.genealogy.databinding.ItemMemberBinding
import com.fcz.genealogy.util.DateUtils

class MemberAdapter(
    private val onMemberClick: (FamilyMember) -> Unit
) : ListAdapter<FamilyMember, MemberAdapter.MemberViewHolder>(MemberDiffCallback()) {
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MemberViewHolder {
        val binding = ItemMemberBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return MemberViewHolder(binding)
    }
    
    override fun onBindViewHolder(holder: MemberViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
    
    inner class MemberViewHolder(
        private val binding: ItemMemberBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        
        fun bind(member: FamilyMember) {
            // 设置姓名和世代标签
            binding.textName.text = member.name
            binding.textGeneration.text = binding.root.context.getString(
                R.string.generation_format_simple,
                member.generation
            )
            
            // 设置生存状态
            binding.textStatus.text = if (member.isAlive) "在世" else "已故"
            binding.textStatus.setTextColor(
                ContextCompat.getColor(
                    binding.root.context,
                    if (member.isAlive) R.color.status_living else R.color.status_deceased
                )
            )
            
            // 设置生平年份
            val lifeSpan = when {
                member.birthDate != null && member.deathDate != null -> 
                    "${DateUtils.formatYear(member.birthDate!!)} - ${DateUtils.formatYear(member.deathDate!!)}"
                member.birthDate != null -> 
                    "${DateUtils.formatYear(member.birthDate!!)} - 至今"
                else -> "不详"
            }
            binding.textLifespan.text = lifeSpan
            
            // 设置职业
            binding.textOccupation.text = member.occupation ?: "职业不详"
            
            // 设置背景颜色
            val bgColorRes = when (member.gender) {
                Gender.MALE -> R.color.male_bg
                Gender.FEMALE -> R.color.female_bg
                else -> R.color.gray_bg
            }
            binding.cardMember.setCardBackgroundColor(
                ContextCompat.getColor(binding.root.context, bgColorRes)
            )
            
            // 设置点击事件
            binding.root.setOnClickListener {
                onMemberClick(member)
            }
        }
    }
    
    class MemberDiffCallback : DiffUtil.ItemCallback<FamilyMember>() {
        override fun areItemsTheSame(oldItem: FamilyMember, newItem: FamilyMember): Boolean {
            return oldItem.id == newItem.id
        }
        
        override fun areContentsTheSame(oldItem: FamilyMember, newItem: FamilyMember): Boolean {
            return oldItem == newItem
        }
    }
} 