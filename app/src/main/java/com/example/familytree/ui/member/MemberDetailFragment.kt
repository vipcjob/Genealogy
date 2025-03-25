package com.example.familytree.ui.member

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.familytree.R
import com.example.familytree.databinding.FragmentMemberDetailBinding
import com.example.familytree.util.DateUtils

class MemberDetailFragment : Fragment() {
    
    private var _binding: FragmentMemberDetailBinding? = null
    private val binding get() = _binding!!
    
    private val viewModel: MemberDetailViewModel by viewModels { 
        MemberDetailViewModelFactory(requireActivity().application) 
    }
    
    private val args: MemberDetailFragmentArgs by navArgs()
    
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
        
        // 设置返回按钮
        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
        
        // 加载成员信息
        viewModel.loadMember(args.memberId)
        
        // 设置编辑按钮
        binding.fabEdit.setOnClickListener {
            // TODO: 跳转到编辑页面
        }
        
        // 设置树状图按钮
        binding.buttonTree.setOnClickListener {
            findNavController().navigate(
                MemberDetailFragmentDirections.actionMemberDetailToFamilyTree(args.memberId)
            )
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
                    com.example.familytree.data.model.Gender.MALE -> "男"
                    com.example.familytree.data.model.Gender.FEMALE -> "女"
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
                            findNavController().navigate(
                                MemberDetailFragmentDirections.actionMemberDetailSelf(parent.id)
                            )
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
                            findNavController().navigate(
                                MemberDetailFragmentDirections.actionMemberDetailSelf(firstSpouse.id)
                            )
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
                                findNavController().navigate(
                                    MemberDetailFragmentDirections.actionMemberDetailSelf(child.id)
                                )
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