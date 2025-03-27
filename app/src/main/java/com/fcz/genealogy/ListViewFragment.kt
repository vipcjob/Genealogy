package com.fcz.genealogy.ui.familytree.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.fcz.genealogy.R
import com.fcz.genealogy.databinding.FragmentListViewBinding
import com.fcz.genealogy.ui.familytree.FamilyTreeViewModel
import com.fcz.genealogy.ui.familytree.FamilyTreeViewModelFactory
import com.fcz.genealogy.ui.member.MemberDetailActivity
import com.google.android.material.chip.Chip

class ListViewFragment : Fragment() {
    
    private var _binding: FragmentListViewBinding? = null
    private val binding get() = _binding!!
    
    private val viewModel: FamilyTreeViewModel by activityViewModels { 
        FamilyTreeViewModelFactory(requireActivity().application) 
    }
    
    private lateinit var adapter: FamilyMemberAdapter
    
    companion object {
        fun newInstance(): ListViewFragment {
            return ListViewFragment()
        }
    }
    
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
        setupGenerationChips()
        observeViewModel()
    }
    
    private fun setupRecyclerView() {
        adapter = FamilyMemberAdapter { member ->
            startActivity(MemberDetailActivity.newIntent(requireContext(), member.id))
        }
        
        binding.recyclerViewMembers.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewMembers.adapter = adapter
    }
    
    private fun setupGenerationChips() {
        binding.chipAll.setOnClickListener {
            viewModel.resetGenerationFilter()
        }
        
        // 观察所有成员数据以动态添加世代选择器
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
                    val chip = Chip(requireContext()).apply {
                        text = getString(R.string.generation_format_simple, gen)
                        id = View.generateViewId()
                        setOnClickListener {
                            viewModel.setGenerationFilter(gen)
                        }
                        isCheckable = true
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
            binding.textMemberCount.text = getString(R.string.member_count_format, members.size)
            
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