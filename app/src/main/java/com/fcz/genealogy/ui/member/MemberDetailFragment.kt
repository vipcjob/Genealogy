package com.fcz.genealogy.ui.member

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.fcz.genealogy.R
import com.fcz.genealogy.databinding.FragmentMemberDetailBinding
import com.fcz.genealogy.ui.familytree.FamilyTreeActivity
import com.fcz.genealogy.ui.helper.FragmentContainerActivity
import com.fcz.genealogy.util.DateUtils

class MemberDetailFragment : Fragment() {
    
    private var _binding: FragmentMemberDetailBinding? = null
    private val binding get() = _binding!!
    
    private val viewModel: MemberDetailViewModel by viewModels { 
        MemberDetailViewModelFactory(requireActivity().application) 
    }
    
    private lateinit var memberId: String
    
    companion object {
        private const val ARG_MEMBER_ID = "arg_member_id"
        
        fun newInstance(memberId: String): MemberDetailFragment {
            return MemberDetailFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_MEMBER_ID, memberId)
                }
            }
        }
    }
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMemberDetailBinding.inflate(inflater, container, false)
        return binding.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        // 获取成员ID
        memberId = arguments?.getString(ARG_MEMBER_ID) ?: return
        
        // 设置返回按钮
        binding.toolbar.setNavigationOnClickListener {
            requireActivity().finish()
        }
        
        // 加载成员信息
        viewModel.loadMember(memberId)
        
        // 设置编辑按钮
        binding.fabEdit.setOnClickListener {
            // TODO: 跳转到编辑页面
        }
        
        // 设置树状图按钮
        binding.buttonTree.setOnClickListener {
            startActivity(FamilyTreeActivity.newIntent(requireContext(), memberId))
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
        viewModel.member.observe(viewLifecycleOwner) { member ->
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
                        "${DateUtils.formatYear(member.birthDate!!)} - ${DateUtils.formatYear(member.deathDate!!)}"
                    member.birthDate != null -> 
                        "${DateUtils.formatYear(member.birthDate!!)} - 至今"
                    else -> "不详"
                }
                binding.textLifespan.text = lifeSpan
                
                // 设置性别
                val genderText = when (member.gender) {
                    com.fcz.genealogy.data.model.Gender.MALE -> "男"
                    com.fcz.genealogy.data.model.Gender.FEMALE -> "女"
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
                viewModel.parent.observe(viewLifecycleOwner) { parent ->
                    if (parent != null) {
                        binding.textParent.text = parent.name
                        binding.layoutParent.setOnClickListener {
                            // 使用FragmentContainerActivity启动MemberDetailFragment
                            val intent = FragmentContainerActivity.newIntent<MemberDetailFragment>(
                                requireContext(),
                                args = Bundle().apply { putString(ARG_MEMBER_ID, parent.id) },
                                title = parent.name
                            )
                            startActivity(intent)
                        }
                    } else {
                        binding.textParent.text = "不详"
                    }
                }
                
                // 观察配偶信息
                viewModel.spouses.observe(viewLifecycleOwner) { spouses ->
                    if (spouses.isNotEmpty()) {
                        // 显示第一个配偶（如果需要显示多个配偶，需要修改UI布局）
                        val firstSpouse = spouses[0]
                        binding.textSpouse.text = firstSpouse.name
                        binding.layoutSpouse.setOnClickListener {
                            // 使用FragmentContainerActivity启动MemberDetailFragment
                            val intent = FragmentContainerActivity.newIntent<MemberDetailFragment>(
                                requireContext(),
                                args = Bundle().apply { putString(ARG_MEMBER_ID, firstSpouse.id) },
                                title = firstSpouse.name
                            )
                            startActivity(intent)
                        }
                    } else {
                        binding.textSpouse.text = "不详"
                    }
                }
                
                // 观察子女信息
                viewModel.children.observe(viewLifecycleOwner) { children ->
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
                                // 使用FragmentContainerActivity启动MemberDetailFragment
                                val intent = FragmentContainerActivity.newIntent<MemberDetailFragment>(
                                    requireContext(),
                                    args = Bundle().apply { putString(ARG_MEMBER_ID, child.id) },
                                    title = child.name
                                )
                                startActivity(intent)
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
    
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
} 