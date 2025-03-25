package com.example.familytree.ui.familytree.generation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.familytree.databinding.FragmentGenerationViewBinding
import com.example.familytree.ui.familytree.FamilyTreeViewModel
import com.example.familytree.ui.familytree.FamilyTreeViewModelFactory

class GenerationViewFragment : Fragment() {
    
    private var _binding: FragmentGenerationViewBinding? = null
    private val binding get() = _binding!!
    
    private val viewModel: FamilyTreeViewModel by activityViewModels { 
        FamilyTreeViewModelFactory(requireActivity().application) 
    }
    
    private lateinit var adapter: GenerationAdapter
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGenerationViewBinding.inflate(inflater, container, false)
        return binding.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        setupRecyclerView()
        observeViewModel()
    }
    
    private fun setupRecyclerView() {
        adapter = GenerationAdapter { generation ->
            // 按世代筛选
            viewModel.setFilterGeneration(generation)
            // 切换到列表视图
            viewModel.setCurrentTab(1)
        }
        
        binding.recyclerGenerations.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerGenerations.adapter = adapter
    }
    
    private fun observeViewModel() {
        viewModel.allMembers.observe(viewLifecycleOwner) { members ->
            if (members.isNotEmpty()) {
                // 按世代分组
                val generationGroups = members.groupBy { it.generation }
                    .mapValues { entry -> entry.value.size }
                    .toList()
                    .sortedBy { (generation, _) -> generation }
                
                // 更新统计信息
                binding.textGenerationCount.text = "共${generationGroups.size}代"
                binding.textTotalCount.text = "总人数：${members.size}人"
                
                // 计算男女比例
                val maleCount = members.count { it.gender == com.example.familytree.data.model.Gender.MALE }
                val femaleCount = members.count { it.gender == com.example.familytree.data.model.Gender.FEMALE }
                //binding.textGenderRatio.text = "男女比例：${maleCount}:${femaleCount}"
                
                // 更新适配器
                adapter.submitList(generationGroups)
            }
        }
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
} 