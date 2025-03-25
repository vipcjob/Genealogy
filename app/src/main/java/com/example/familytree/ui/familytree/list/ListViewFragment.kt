package com.example.familytree.ui.familytree.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.familytree.databinding.FragmentListViewBinding
import com.example.familytree.ui.familytree.FamilyTreeViewModel
import com.example.familytree.ui.familytree.FamilyTreeViewModelFactory

class ListViewFragment : Fragment() {
    
    private var _binding: FragmentListViewBinding? = null
    private val binding get() = _binding!!
    
    private val viewModel: FamilyTreeViewModel by activityViewModels { 
        FamilyTreeViewModelFactory(requireActivity().application) 
    }
    
    private lateinit var adapter: MemberAdapter
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListViewBinding.inflate(inflater, container, false)
        return binding.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        setupRecyclerView()
        setupGenerationNav()
        observeViewModel()
    }
    
    private fun setupRecyclerView() {
        adapter = MemberAdapter { member ->
            // 设置焦点成员并切换到树状图视图
            viewModel.setFocusMember(member.id)
            viewModel.setCurrentTab(0)
        }
        
        binding.recyclerViewMembers.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewMembers.adapter = adapter
    }
    
    private fun setupGenerationNav() {
        binding.chipAll.setOnClickListener {
            viewModel.setFilterGeneration(null)
        }
        
        // 动态填充世代导航
        viewModel.allMembers.observe(viewLifecycleOwner) { members ->
            if (members.isNotEmpty()) {
                // 获取所有不同的世代
                val generations = members.map { it.generation }.distinct().sorted()
                
                // 清除现有的世代芯片（除了"全部"）
                val chipCount = binding.chipGroupGenerations.childCount
                if (chipCount > 1) {
                    binding.chipGroupGenerations.removeViews(1, chipCount - 1)
                }
                
                // 为每个世代添加一个芯片
                for (gen in generations) {
                    val chip = layoutInflater.inflate(
                        com.google.android.material.R.layout.material_time_chip,
                        binding.chipGroupGenerations,
                        false
                    ) as com.google.android.material.chip.Chip
                    
                    chip.text = "第${gen}代"
                    chip.id = View.generateViewId()
                    chip.setOnClickListener {
                        viewModel.setFilterGeneration(gen)
                    }
                    
                    binding.chipGroupGenerations.addView(chip)
                }
            }
        }
    }
    
    private fun observeViewModel() {
        // 观察筛选后的成员列表
        viewModel.filteredMembers.observe(viewLifecycleOwner) { members ->
            adapter.submitList(members)
            
            // 更新统计信息
            binding.textMemberCount.text = "共 ${members.size} 人"
            
            if (members.isEmpty()) {
                binding.recyclerViewMembers.visibility = View.GONE
                binding.layoutEmpty.visibility = View.VISIBLE
            } else {
                binding.recyclerViewMembers.visibility = View.VISIBLE
                binding.layoutEmpty.visibility = View.GONE
            }
        }
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
} 