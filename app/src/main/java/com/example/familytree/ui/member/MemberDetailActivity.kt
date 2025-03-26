package com.example.familytree.ui.member

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import com.example.familytree.R
import com.example.familytree.data.model.Gender
import com.example.familytree.databinding.FragmentMemberDetailBinding
import com.example.familytree.ui.familytree.FamilyTreeActivity
import com.example.familytree.util.DateUtils

class MemberDetailActivity : AppCompatActivity() {
    
    private lateinit var binding: FragmentMemberDetailBinding
    
    private val viewModel: MemberDetailViewModel by viewModels { 
        MemberDetailViewModelFactory(application) 
    }
    
    companion object {
        private const val EXTRA_MEMBER_ID = "extra_member_id"
        
        fun newIntent(context: Context, memberId: String): Intent {
            return Intent(context, MemberDetailActivity::class.java).apply {
                putExtra(EXTRA_MEMBER_ID, memberId)
            }
        }
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentMemberDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        val memberId = intent.getStringExtra(EXTRA_MEMBER_ID) ?: return
        
        // 设置返回按钮
        binding.toolbar.setNavigationOnClickListener {
            finish()
        }
        
        // 加载成员信息
        viewModel.loadMember(memberId)
        
        // 设置编辑按钮
        binding.fabEdit.setOnClickListener {
            // TODO: 跳转到编辑页面
        }
        
        // 设置树状图按钮
        binding.buttonTree.setOnClickListener {
            startActivity(FamilyTreeActivity.newIntent(this, memberId))
        }
        
        // 设置故事按钮
        binding.buttonStories.setOnClickListener {
            // TODO: 跳转到与该成员相关的故事页面
        }
        
        // 设置相册按钮
        binding.buttonPhotos.setOnClickListener {
            // TODO: 跳转到与该成员相关的相册页面
        }
        
        // 观察成员信息
        viewModel.member.observe(this) { member ->
            if (member != null) {
                // 设置基本信息
                binding.toolbar.title = member.name
                binding.textName.text = member.name
                binding.textGeneration.text = getString(R.string.generation_format, member.generation)
                
                // 设置生存状态
                val statusText = if (member.isAlive) "在世" else "已故"
                binding.textStatus.text = statusText
                
                // 设置生平年份
                val lifeSpan = when {
                    member.birthDate != null && member.deathDate != null -> 
                        "${DateUtils.formatYear(member.birthDate)} - ${DateUtils.formatYear(member.deathDate)}"
                    member.birthDate != null -> 
                        "${DateUtils.formatYear(member.birthDate)} - 至今"
                    else -> "不详"
                }
                binding.textLifespan.text = lifeSpan
                
                // 设置性别
                val genderText = when (member.gender) {
                    Gender.MALE -> "男"
                    Gender.FEMALE -> "女"
                    else -> "未知"
                }
                binding.textGender.text = genderText
                
                // 设置出生日期
                binding.textBirthDate.text = member.birthDate?.let { DateUtils.formatDate(it) } ?: "不详"
                
                // 设置去世日期
                if (member.isAlive) {
                    binding.layoutDeathDate.visibility = View.GONE
                } else {
                    binding.layoutDeathDate.visibility = View.VISIBLE
                    binding.textDeathDate.text = member.deathDate?.let { DateUtils.formatDate(it) } ?: "不详"
                }
                
                // 设置职业
                binding.textOccupation.text = member.occupation ?: "不详"
                
                // 设置传记
                binding.textBiography.text = member.biography ?: "暂无传记"
                
                // 观察父母信息
                viewModel.parent.observe(this) { parent ->
                    if (parent != null) {
                        binding.textParent.text = parent.name
                        binding.layoutParent.setOnClickListener {
                            startActivity(newIntent(this, parent.id))
                        }
                    } else {
                        binding.textParent.text = "不详"
                    }
                }
                
                // 观察配偶信息
                viewModel.spouses.observe(this) { spouses ->
                    if (spouses.isNotEmpty()) {
                        // 显示第一个配偶（如果需要显示多个配偶，需要修改UI布局）
                        val firstSpouse = spouses[0]
                        binding.textSpouse.text = firstSpouse.name
                        binding.layoutSpouse.setOnClickListener {
                            startActivity(newIntent(this, firstSpouse.id))
                        }
                    } else {
                        binding.textSpouse.text = "不详"
                    }
                }
                
                // 观察子女信息
                viewModel.children.observe(this) { children ->
                    if (children.isNotEmpty()) {
                        // 创建子女列表
                        binding.layoutChildren.removeAllViews()
                        for (child in children) {
                            val childView = layoutInflater.inflate(
                                R.layout.item_related_member,
                                binding.layoutChildren,
                                false
                            )
                            
                            val textName = childView.findViewById<android.widget.TextView>(R.id.text_name)
                            textName.text = child.name
                            
                            childView.setOnClickListener {
                                startActivity(newIntent(this, child.id))
                            }
                            
                            binding.layoutChildren.addView(childView)
                        }
                    } else {
                        val emptyView = layoutInflater.inflate(
                            R.layout.item_empty_related,
                            binding.layoutChildren,
                            false
                        )
                        binding.layoutChildren.addView(emptyView)
                    }
                }
            }
        }
    }
} 