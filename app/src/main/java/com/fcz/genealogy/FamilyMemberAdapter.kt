package com.fcz.genealogy.ui.familytree.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.fcz.genealogy.data.model.FamilyMember
import com.fcz.genealogy.data.model.Gender
import com.fcz.genealogy.R
import com.fcz.genealogy.databinding.ItemFamilyMemberBinding

class FamilyMemberAdapter(
    private val onItemClick: (FamilyMember) -> Unit
) : ListAdapter<FamilyMember, FamilyMemberAdapter.MemberViewHolder>(MemberDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MemberViewHolder {
        val binding = ItemFamilyMemberBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return MemberViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MemberViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class MemberViewHolder(
        private val binding: ItemFamilyMemberBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onItemClick(getItem(position))
                }
            }
        }

        fun bind(member: FamilyMember) {
            binding.textName.text = member.name
            binding.textBirth.text = "出生: ${member.birthYear ?: "不详"}"
            binding.textGeneration.text = "第${member.generation}代"
            
            // 设置性别图标
            when (member.gender) {
                Gender.MALE -> {
                    binding.imageMember.setImageResource(R.drawable.ic_male)
                }
                Gender.FEMALE -> {
                    binding.imageMember.setImageResource(R.drawable.ic_female)
                }
                else -> {
                    binding.imageMember.setImageResource(R.drawable.ic_person)
                }
            }
            
            // 设置生存状态
            if (member.isAlive) {
                binding.textDeathInfo.visibility = android.view.View.GONE
            } else {
                binding.textDeathInfo.visibility = android.view.View.VISIBLE
                binding.textDeathInfo.text = "去世: ${member.deathYear ?: "不详"}"
            }
        }
    }

    private class MemberDiffCallback : DiffUtil.ItemCallback<FamilyMember>() {
        override fun areItemsTheSame(oldItem: FamilyMember, newItem: FamilyMember): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: FamilyMember, newItem: FamilyMember): Boolean {
            return oldItem == newItem
        }
    }
} 