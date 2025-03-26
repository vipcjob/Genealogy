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
import com.example.familytree.ui.member.MemberDetailActivity

class GenerationViewFragment : Fragment() {
    
    private var _binding: FragmentGenerationViewBinding? = null
    private val binding get() = _binding!!
    
    private val viewModel: FamilyTreeViewModel by activityViewModels { 
        FamilyTreeViewModelFactory(requireActivity().application) 
    }
    
    private lateinit var adapter: GenerationAdapter
    
    companion object {
        fun newInstance(): GenerationViewFragment {
            return GenerationViewFragment()
        }
    }
    
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
            // 按世代筛选并跳转到列表页
            viewModel.filterByGeneration(generation)
        }
        
        binding.recyclerGenerations.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerGenerations.adapter = adapter
    }
    
    private fun observeViewModel() {
        // 观察全部成员数据
        viewModel.allMembers.observe(viewLifecycleOwner) { members ->
            if (members.isEmpty()) {
                // 处理空数据状态
                binding.cardGenerationStats.visibility = View.GONE
            } else {
                binding.cardGenerationStats.visibility = View.VISIBLE
                
                // 统计信息
                val generations = members.groupBy { it.generation }
                binding.textGenerationCount.text = generations.size.toString()
                binding.textTotalCount.text = members.size.toString()
                binding.textLivingCount.text = members.count { it.isAlive }.toString()
                
                // 提交给适配器
                adapter.submitGenerations(generations)
            }
        }
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
} 