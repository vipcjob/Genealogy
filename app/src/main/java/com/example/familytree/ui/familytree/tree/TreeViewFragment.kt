package com.example.familytree.ui.familytree.tree

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.familytree.databinding.FragmentTreeViewBinding
import com.example.familytree.ui.familytree.FamilyTreeViewModel
import com.example.familytree.ui.familytree.FamilyTreeViewModelFactory

class TreeViewFragment : Fragment() {
    
    private var _binding: FragmentTreeViewBinding? = null
    private val binding get() = _binding!!
    
    private val viewModel: FamilyTreeViewModel by activityViewModels { 
        FamilyTreeViewModelFactory(requireActivity().application) 
    }
    
    private var scaleFactor = 1.0f
    
    companion object {
        private const val ARG_MEMBER_ID = "arg_member_id"
        
        fun newInstance(memberId: String? = null): TreeViewFragment {
            return TreeViewFragment().apply {
                arguments = Bundle().apply {
                    if (memberId != null) {
                        putString(ARG_MEMBER_ID, memberId)
                    }
                }
            }
        }
    }
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTreeViewBinding.inflate(inflater, container, false)
        return binding.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        // 获取成员ID并设置到ViewModel
        arguments?.getString(ARG_MEMBER_ID)?.let { memberId ->
            viewModel.setFocusMember(memberId)
        }
        
        setupObservers()
        setupZoomControls()
    }
    
    private fun setupObservers() {
        // 观察焦点成员
        viewModel.focusMember.observe(viewLifecycleOwner) { member ->
            if (member != null) {
                // 更新路径导航
                binding.textPath.text = buildPath(member)
                
                // 更新统计信息
                val descendants = viewModel.descendants.value ?: emptyList()
                binding.textDescendantsCount.text = "直系后代: ${descendants.size}人"
                
                val livingCount = descendants.count { it.isAlive }
                binding.textLivingCount.text = "在世: ${livingCount}人"
                
                val maleCount = descendants.count { it.gender == com.example.familytree.data.model.Gender.MALE }
                val femaleCount = descendants.count { it.gender == com.example.familytree.data.model.Gender.FEMALE }
                binding.textGenderRatio.text = "男:女 $maleCount:$femaleCount"
                
                // 更新家族树视图
                binding.familyTreeView.setFocusMember(member)
                binding.familyTreeView.setAncestors(viewModel.ancestors.value ?: emptyList())
                binding.familyTreeView.setDescendants(descendants)
                binding.familyTreeView.invalidate()
            }
        }
    }
    
    private fun setupZoomControls() {
        binding.buttonZoomIn.setOnClickListener {
            scaleFactor = (scaleFactor * 1.2f).coerceAtMost(3.0f)
            applyScale()
        }
        
        binding.buttonZoomOut.setOnClickListener {
            scaleFactor = (scaleFactor / 1.2f).coerceAtLeast(0.5f)
            applyScale()
        }
    }
    
    private fun applyScale() {
        binding.familyTreeView.scaleX = scaleFactor
        binding.familyTreeView.scaleY = scaleFactor
    }
    
    private fun buildPath(member: com.example.familytree.data.model.FamilyMember): String {
        val ancestors = viewModel.ancestors.value ?: emptyList()
        val path = mutableListOf<String>()
        
        // 从最远的祖先开始
        for (i in ancestors.size - 1 downTo 0) {
            path.add(ancestors[i].name)
        }
        
        // 添加当前成员
        path.add(member.name)
        
        return path.joinToString(" > ")
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
} 